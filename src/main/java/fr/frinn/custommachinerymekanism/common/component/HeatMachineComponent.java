package fr.frinn.custommachinerymekanism.common.component;

import com.google.common.collect.Maps;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IDumpComponent;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.IMachineComponentTemplate;
import fr.frinn.custommachinery.api.component.ISerializableComponent;
import fr.frinn.custommachinery.api.component.ISideConfigComponent;
import fr.frinn.custommachinery.api.component.ITickableComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.network.DataType;
import fr.frinn.custommachinery.api.network.ISyncable;
import fr.frinn.custommachinery.api.network.ISyncableStuff;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import fr.frinn.custommachinery.impl.component.config.RelativeSide;
import fr.frinn.custommachinery.impl.component.config.ToggleSideConfig;
import fr.frinn.custommachinery.impl.component.config.ToggleSideMode;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.client.jei.heat.Heat;
import mekanism.api.heat.HeatAPI.HeatTransfer;
import mekanism.api.heat.IHeatCapacitor;
import mekanism.api.heat.IHeatHandler;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.heat.ITileHeatHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class HeatMachineComponent extends AbstractMachineComponent implements ISideConfigComponent, ITileHeatHandler, ISerializableComponent, ISyncableStuff, ITickableComponent, IDumpComponent {

    private final double baseTemp;
    private final ToggleSideConfig config;
    private final BasicHeatCapacitor capacitor;
    private final Map<Direction, BlockCapabilityCache<IHeatHandler, Direction>> neighbours = Maps.newEnumMap(Direction.class);
    private double lastEnvironmentalLoss;

    public HeatMachineComponent(IMachineComponentManager manager, double capacity, double baseTemp, double inverseConductionCoefficient, double inverseInsulationCoefficient, ToggleSideConfig.Template config) {
        super(manager, ComponentIOMode.BOTH);
        this.baseTemp = baseTemp;
        this.config = config.build(manager.facing());
        this.config.setCallback(this::onConfigChange);
        this.capacitor = BasicHeatCapacitor.create(capacity, inverseConductionCoefficient, inverseInsulationCoefficient, () -> baseTemp, this);
    }

    @Override
    public MachineComponentType<HeatMachineComponent> getType() {
        return Registration.HEAT_MACHINE_COMPONENT.get();
    }

    @Override
    public ToggleSideConfig getConfig() {
        return this.config;
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public void serverTick() {
        this.capacitor.update();
        this.updateNeighbours();
        HeatTransfer transfer = this.simulate();
        this.lastEnvironmentalLoss = transfer.environmentTransfer();
    }

    @Override
    public void dump(List<String> ids) {
        this.capacitor.setHeat(this.baseTemp);
    }

    public void handleHeatAndUpdate(double heat) {
        this.capacitor.handleHeat(heat);
        this.capacitor.update();
    }

    public double getLastEnvironmentalLoss() {
        return this.lastEnvironmentalLoss;
    }

    private void onConfigChange(RelativeSide side, ToggleSideMode old, ToggleSideMode now) {
        if(old != now)
            this.getManager().getTile().invalidateCapabilities();
    }

    private void updateNeighbours() {
        Level level = this.getManager().getLevel();
        BlockPos pos = this.getManager().getTile().getBlockPos();
        for(Direction side : Direction.values()) {
            if(this.neighbours.get(side) == null && level.getBlockEntity(pos.relative(side)) != null)
                this.neighbours.put(side, BlockCapabilityCache.create(Capabilities.HEAT, (ServerLevel) level, pos.relative(side), side.getOpposite(), () -> !this.getManager().getTile().isRemoved(), () -> this.neighbours.remove(side)));
        }
    }

    @Nullable
    public IHeatHandler getHeatHandler(@Nullable Direction side) {
        if(!this.config.getDirectionMode(side).isDisabled())
            return this;
        return null;
    }

    @Override
    public void serialize(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.putDouble("Heat", this.capacitor.getHeat());
        nbt.put("Config", this.config.serialize());
    }

    @Override
    public void deserialize(CompoundTag nbt, HolderLookup.Provider registries) {
        if(nbt.contains("Heat", Tag.TAG_DOUBLE))
            this.capacitor.setHeat(nbt.getDouble("Heat"));
        if(nbt.contains("Config", Tag.TAG_COMPOUND))
            this.config.deserialize(nbt.getCompound("Config"));
    }

    @Override
    public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {
        container.accept(DataType.createSyncable(ToggleSideConfig.class, this::getConfig, this.config::set));
        container.accept(DataType.createSyncable(Double.class, this.capacitor::getHeat, this.capacitor::setHeat));
        container.accept(DataType.createSyncable(Double.class, this::getLastEnvironmentalLoss, loss -> this.lastEnvironmentalLoss = loss));
    }

    /** HEAT HANDLER STUFF **/

    @Override
    public List<IHeatCapacitor> getHeatCapacitors(@Nullable Direction direction) {
        if(direction == null || this.config.getDirectionMode(direction).isEnabled())
            return Collections.singletonList(this.capacitor);
        else
            return Collections.emptyList();
    }

    @Override
    public void onContentsChanged() {
        getManager().markDirty();
    }

    @Nullable
    @Override
    public IHeatHandler getAdjacent(Direction side) {
        return this.neighbours.get(side) == null ? null : this.neighbours.get(side).getCapability();
    }

    public record Template(double capacity, double baseTemp, double inverseConductionCoefficient, double inverseInsulationCoefficient, ToggleSideConfig.Template config) implements IMachineComponentTemplate<HeatMachineComponent> {

        public static final NamedCodec<Template> CODEC = NamedCodec.record(templateInstance ->
                templateInstance.group(
                        NamedCodec.doubleRange(1.0D, Double.MAX_VALUE).optionalFieldOf("capacity", 1.0D).forGetter(template -> template.capacity),
                        NamedCodec.DOUBLE.optionalFieldOf("base_temp", 300.0D).forGetter(template -> template.baseTemp),
                        NamedCodec.DOUBLE.optionalFieldOf("conduction", 1.0D).forGetter(template -> template.inverseConductionCoefficient),
                        NamedCodec.DOUBLE.optionalFieldOf("insulation", 0.0D).forGetter(template -> template.inverseInsulationCoefficient),
                        ToggleSideConfig.Template.CODEC.optionalFieldOf("config", ToggleSideConfig.Template.DEFAULT_ALL_ENABLED).forGetter(template -> template.config)
                ).apply(templateInstance, Template::new), "Heat machine component"
        );

        @Override
        public MachineComponentType<HeatMachineComponent> getType() {
            return Registration.HEAT_MACHINE_COMPONENT.get();
        }

        @Override
        public String getId() {
            return "Heat";
        }

        @Override
        public boolean canAccept(Object ingredient, boolean isInput, IMachineComponentManager manager) {
            return ingredient instanceof Heat;
        }

        @Override
        public HeatMachineComponent build(IMachineComponentManager manager) {
            return new HeatMachineComponent(manager, this.capacity, this.baseTemp, this.inverseConductionCoefficient, this.inverseInsulationCoefficient, this.config);
        }
    }
}
