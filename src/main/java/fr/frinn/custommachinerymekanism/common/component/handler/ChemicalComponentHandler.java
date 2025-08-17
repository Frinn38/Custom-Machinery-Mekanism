package fr.frinn.custommachinerymekanism.common.component.handler;

import com.google.common.collect.Maps;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IDumpComponent;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.ISerializableComponent;
import fr.frinn.custommachinery.api.component.ITickableComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.network.ISyncable;
import fr.frinn.custommachinery.api.network.ISyncableStuff;
import fr.frinn.custommachinery.impl.component.AbstractComponentHandler;
import fr.frinn.custommachinery.impl.component.config.IOSideMode;
import fr.frinn.custommachinery.impl.component.config.RelativeSide;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.component.ChemicalMachineComponent;
import fr.frinn.custommachinerymekanism.common.transfer.SidedChemicalTank;
import mekanism.api.Action;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ChemicalComponentHandler extends AbstractComponentHandler<ChemicalMachineComponent> implements ISerializableComponent, ISyncableStuff, ITickableComponent, IDumpComponent {

    private final IChemicalHandler generalHandler = new SidedChemicalTank(this, null);
    private final Map<Direction, IChemicalHandler> sidedHandlers = Maps.newEnumMap(Direction.class);

    private final List<ChemicalMachineComponent> inputs = new ArrayList<>();
    private final List<ChemicalMachineComponent> outputs = new ArrayList<>();

    public ChemicalComponentHandler(IMachineComponentManager manager, List<ChemicalMachineComponent> components) {
        super(manager, components);
        Arrays.stream(Direction.values()).forEach(side ->
                this.sidedHandlers.put(side, new SidedChemicalTank(this, side))
        );
        components.forEach(component -> {
            component.getConfig().setCallback(this::configChanged);
            if(component.getMode().isInput())
                this.inputs.add(component);
            if(component.getMode().isOutput())
                this.outputs.add(component);
        });
    }

    private void configChanged(RelativeSide side, IOSideMode oldMode, IOSideMode newMode) {
        if(oldMode.isNone() != newMode.isNone())
            this.getManager().getTile().invalidateCapabilities();
    }

    @Nullable
    public IChemicalHandler getSidedHandler(@Nullable Direction side) {
        if(side == null)
            return this.generalHandler;
        else if(this.getComponents().stream().anyMatch(component -> !component.getConfig().getDirectionMode(side).isNone()))
            return this.sidedHandlers.get(side);
        return null;
    }

    public IChemicalHandler getGeneralHandler() {
        return this.generalHandler;
    }

    @Override
    public MachineComponentType<ChemicalMachineComponent> getType() {
        return Registration.CHEMICAL_MACHINE_COMPONENT.get();
    }

    @Override
    public Optional<ChemicalMachineComponent> getComponentForID(String id) {
        return this.getComponents().stream().filter(component -> component.getId().equals(id)).findFirst();
    }

    @Override
    public ComponentIOMode getMode() {
        return ComponentIOMode.NONE;
    }

    @Override
    public void serialize(CompoundTag nbt, HolderLookup.Provider registries) {
        ListTag componentsNBT = new ListTag();
        this.getComponents().forEach(component -> {
            CompoundTag componentNBT = new CompoundTag();
            component.serialize(componentNBT, registries);
            componentNBT.putString("id", component.getId());
            componentsNBT.add(componentNBT);
        });
        nbt.put("chemicals", componentsNBT);
    }

    @Override
    public void deserialize(CompoundTag nbt, HolderLookup.Provider registries) {
        if(nbt.contains("chemicals", Tag.TAG_LIST)) {
            ListTag componentsNBT = nbt.getList("chemicals", Tag.TAG_COMPOUND);
            componentsNBT.forEach(inbt -> {
                if(inbt instanceof CompoundTag componentNBT) {
                    if(componentNBT.contains("id", Tag.TAG_STRING)) {
                        this.getComponents().stream().filter(component -> component.getId().equals(componentNBT.getString("id"))).findFirst().ifPresent(component -> component.deserialize(componentNBT, registries));
                    }
                }
            });
        }
    }

    @Override
    public void getStuffToSync(Consumer<ISyncable<?, ?>> consumer) {
        this.getComponents().forEach(component -> component.getStuffToSync(consumer));
    }

    private final Map<Direction, BlockCapabilityCache<IChemicalHandler, Direction>> neighbourStorages = Maps.newEnumMap(Direction.class);
    @Override
    public void serverTick() {
        //I/O between the machine and neighbour blocks.
        for(Direction side : Direction.values()) {
            if(this.getComponents().stream().allMatch(component -> component.getConfig().getDirectionMode(side) == IOSideMode.NONE))
                continue;

            IChemicalHandler neighbour;

            if(this.neighbourStorages.get(side) == null) {
                this.neighbourStorages.put(side, BlockCapabilityCache.create(Capabilities.CHEMICAL.block(), (ServerLevel)this.getManager().getLevel(), this.getManager().getTile().getBlockPos().relative(side), side.getOpposite(), () -> !this.getManager().getTile().isRemoved(), () -> this.neighbourStorages.remove(side)));
                if(this.neighbourStorages.get(side) != null)
                    neighbour = this.neighbourStorages.get(side).getCapability();
                else
                    continue;
            }
            else
                neighbour = this.neighbourStorages.get(side).getCapability();

            if(neighbour == null)
                continue;

            for(ChemicalMachineComponent component : this.getComponents()) {
                if(component.getConfig().isAutoInput() && component.getConfig().getDirectionMode(side).isInput() && component.getStack().getAmount() < component.getCapacity()) {
                    ChemicalStack maxExtract = neighbour.extractChemical(Long.MAX_VALUE, Action.SIMULATE);
                    if(!maxExtract.isEmpty()) {
                        ChemicalStack remaining = component.insert(maxExtract, Action.SIMULATE, false);
                        ChemicalStack toTransfer = maxExtract.copyWithAmount(maxExtract.getAmount() - remaining.getAmount());
                        if(!remaining.isEmpty())
                            toTransfer.setAmount(toTransfer.getAmount() - component.insert(toTransfer, Action.SIMULATE, false).getAmount());
                        if(!toTransfer.isEmpty()) {
                            neighbour.extractChemical(toTransfer, Action.EXECUTE);
                            component.insert(toTransfer, Action.EXECUTE, false);
                        }
                    }
                }

                if(component.getConfig().isAutoOutput() && component.getConfig().getDirectionMode(side).isOutput() && component.getStack().getAmount() > 0) {
                    ChemicalStack maxExtract = component.extract(Long.MAX_VALUE, Action.SIMULATE, false);
                    if(!maxExtract.isEmpty()) {
                        ChemicalStack remaining = neighbour.insertChemical(maxExtract, Action.SIMULATE);
                        ChemicalStack toTransfer = maxExtract.copyWithAmount(maxExtract.getAmount() - remaining.getAmount());
                        if(remaining.getAmount() != 0)
                            toTransfer = component.extract(toTransfer.getAmount(), Action.SIMULATE, false);
                        if(!toTransfer.isEmpty()) {
                            component.extract(toTransfer.getAmount(), Action.EXECUTE, false);
                            neighbour.insertChemical(toTransfer, Action.EXECUTE);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void dump(List<String> ids) {
        this.getComponents().stream()
                .filter(component -> ids.contains(component.getId()))
                .forEach(component -> component.setStack(ChemicalStack.EMPTY));
    }

    /** RECIPE STUFF **/

    public long getChemicalAmount(String tank, Chemical chemical) {
        Predicate<ChemicalMachineComponent> tankPredicate = component -> tank.isEmpty() || component.getId().equals(tank);
        return this.inputs.stream().filter(component -> component.getStack().getChemical() == chemical && tankPredicate.test(component)).mapToLong(component -> component.getStack().getAmount()).sum();
    }

    public long getSpaceForChemical(String tank, Chemical chemical) {
        Predicate<ChemicalMachineComponent> tankPredicate = component -> tank.isEmpty() || component.getId().equals(tank);
        return this.outputs.stream().filter(component -> component.isValid(new ChemicalStack(Holder.direct(chemical), 1)) && tankPredicate.test(component)).mapToLong(component -> component.getCapacity() - component.insert(new ChemicalStack(Holder.direct(chemical), component.getCapacity()), Action.SIMULATE, true).getAmount()).sum();
    }

    public void removeFromInputs(String tank, Chemical chemical, long amount) {
        AtomicLong toRemove = new AtomicLong(amount);
        Predicate<ChemicalMachineComponent> tankPredicate = component -> tank.isEmpty() || component.getId().equals(tank);
        this.inputs.stream().filter(component -> component.getStack().getChemical() == chemical && tankPredicate.test(component)).forEach(component -> {
            long maxExtract = Math.min(component.getStack().getAmount(), toRemove.get());
            toRemove.addAndGet(-maxExtract);
            component.extract(maxExtract, Action.EXECUTE, true);
        });
    }

    public void addToOutputs(String tank, Chemical chemical, long amount) {
        AtomicLong toAdd = new AtomicLong(amount);
        Predicate<ChemicalMachineComponent> tankPredicate = component -> tank.isEmpty() || component.getId().equals(tank);
        this.outputs.stream()
                .filter(component -> component.isValid(new ChemicalStack(Holder.direct(chemical), 1)) && tankPredicate.test(component))
                .sorted(Comparator.comparingInt(component -> component.getStack().getChemical() == chemical ? -1 : 1))
                .forEach(component -> {
                    long maxInsert = toAdd.get() - component.insert(new ChemicalStack(Holder.direct(chemical), toAdd.get()), Action.EXECUTE, true).getAmount();
                    toAdd.addAndGet(-maxInsert);
                });
    }
}
