package fr.frinn.custommachinerymekanism.common.component.handler;

import com.google.common.collect.Maps;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IDumpComponent;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.ISerializableComponent;
import fr.frinn.custommachinery.api.component.ITickableComponent;
import fr.frinn.custommachinery.api.network.ISyncable;
import fr.frinn.custommachinery.api.network.ISyncableStuff;
import fr.frinn.custommachinery.impl.component.AbstractComponentHandler;
import fr.frinn.custommachinery.impl.component.config.SideMode;
import fr.frinn.custommachinerymekanism.common.component.ChemicalMachineComponent;
import mekanism.api.Action;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class ChemicalComponentHandler<C extends Chemical<C>, S extends ChemicalStack<C>, H extends IChemicalHandler<C, S>, T extends ChemicalMachineComponent<C, S>> extends AbstractComponentHandler<T> implements ISerializableComponent, ISyncableStuff, ITickableComponent, IDumpComponent {

    private final H generalHandler = createSidedHandler(null);
    private final Map<Direction, H> sidedHandlers = Maps.newEnumMap(Direction.class);

    private final List<T> inputs;
    private final List<T> outputs;
    private final Map<String, T> idMap;

    public ChemicalComponentHandler(IMachineComponentManager manager, List<T> components) {
        super(manager, components);
        Arrays.stream(Direction.values()).forEach(side ->
                this.sidedHandlers.put(side, createSidedHandler(side))
        );
        this.inputs = components.stream().filter(component -> component.getMode().isInput()).toList();
        this.outputs = components.stream().filter(component -> component.getMode().isOutput()).toList();
        this.idMap = components.stream().collect(Collectors.toMap(ChemicalMachineComponent::getId, Function.identity()));
    }

    public abstract BlockCapability<H, Direction> targetCap();

    public abstract H createSidedHandler(@Nullable Direction side);

    @NotNull
    public H getSidedHandler(@Nullable Direction side) {
        if(side == null)
            return this.generalHandler;
        return this.sidedHandlers.get(side);
    }

    public H getGeneralHandler() {
        return this.generalHandler;
    }

    @Override
    public Optional<T> getComponentForID(String id) {
        return Optional.ofNullable(this.idMap.get(id));
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
        nbt.put("gases", componentsNBT);
    }

    @Override
    public void deserialize(CompoundTag nbt, HolderLookup.Provider registries) {
        if(nbt.contains("fluids", Tag.TAG_LIST)) {
            ListTag componentsNBT = nbt.getList("gases", Tag.TAG_COMPOUND);
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

    private final Map<Direction, BlockCapabilityCache<H, Direction>> neighbourStorages = Maps.newEnumMap(Direction.class);
    @Override
    public void serverTick() {
        //I/O between the machine and neighbour blocks.
        for(Direction side : Direction.values()) {
            if(this.getComponents().stream().allMatch(component -> component.getConfig().getSideMode(side) == SideMode.NONE))
                continue;

            H neighbour;

            if(this.neighbourStorages.get(side) == null) {
                this.neighbourStorages.put(side, BlockCapabilityCache.create(this.targetCap(), (ServerLevel)this.getManager().getLevel(), this.getManager().getTile().getBlockPos().relative(side), side.getOpposite(), () -> !this.getManager().getTile().isRemoved(), () -> this.neighbourStorages.remove(side)));
                if(this.neighbourStorages.get(side) != null)
                    neighbour = this.neighbourStorages.get(side).getCapability();
                else
                    continue;
            }
            else
                neighbour = this.neighbourStorages.get(side).getCapability();

            if(neighbour == null)
                continue;

            for(T component : this.getComponents()) {
                if(component.getConfig().isAutoInput() && component.getConfig().getSideMode(side).isInput() && component.getStack().getAmount() < component.getCapacity()) {
                    S maxExtract = neighbour.extractChemical(Long.MAX_VALUE, Action.SIMULATE);

                    if(maxExtract.isEmpty())
                        continue;

                    S remaining = component.insert(maxExtract, Action.SIMULATE, false);

                    if(remaining.getAmount() >= maxExtract.getAmount())
                        continue;

                    component.insert(neighbour.extractChemical(Long.MAX_VALUE, Action.EXECUTE), Action.EXECUTE, false);
                }

                if(component.getConfig().isAutoOutput() && component.getConfig().getSideMode(side).isOutput() && component.getStack().getAmount() > 0) {
                    S maxExtract = component.extract(Long.MAX_VALUE, Action.SIMULATE, false);

                    if(maxExtract.isEmpty())
                        continue;

                    S remaining = neighbour.insertChemical(maxExtract, Action.SIMULATE);

                    if(remaining.getAmount() >= maxExtract.getAmount() && !remaining.isEmpty())
                        continue;

                    neighbour.insertChemical(component.extract(Long.MAX_VALUE, Action.EXECUTE, false), Action.EXECUTE);
                }
            }
        }
    }

    @Override
    public void dump(List<String> ids) {
        this.getComponents().stream()
                .filter(component -> ids.contains(component.getId()))
                .forEach(component -> component.setStack(component.empty()));
    }

    /** RECIPE STUFF **/

    public long getChemicalAmount(String tank, C chemical) {
        Predicate<T> tankPredicate = component -> tank.isEmpty() || component.getId().equals(tank);
        return this.inputs.stream().filter(component -> component.getStack().getChemical() == chemical && tankPredicate.test(component)).mapToLong(component -> component.getStack().getAmount()).sum();
    }

    public long getSpaceForChemical(String tank, C chemical) {
        Predicate<T> tankPredicate = component -> tank.isEmpty() || component.getId().equals(tank);
        return this.outputs.stream().filter(component -> component.isValid(component.createStack(chemical, 1)) && tankPredicate.test(component)).mapToLong(component -> component.getCapacity() - component.insert(component.createStack(chemical, component.getCapacity()), Action.SIMULATE, true).getAmount()).sum();
    }

    public void removeFromInputs(String tank, C chemical, long amount) {
        AtomicLong toRemove = new AtomicLong(amount);
        Predicate<T> tankPredicate = component -> tank.isEmpty() || component.getId().equals(tank);
        this.inputs.stream().filter(component -> component.getStack().getChemical() == chemical && tankPredicate.test(component)).forEach(component -> {
            long maxExtract = Math.min(component.getStack().getAmount(), toRemove.get());
            toRemove.addAndGet(-maxExtract);
            component.extract(maxExtract, Action.EXECUTE, true);
        });
    }

    public void addToOutputs(String tank, C chemical, long amount) {
        AtomicLong toAdd = new AtomicLong(amount);
        Predicate<T> tankPredicate = component -> tank.isEmpty() || component.getId().equals(tank);
        this.outputs.stream()
                .filter(component -> component.isValid(component.createStack(chemical, 1)) && tankPredicate.test(component))
                .sorted(Comparator.comparingInt(component -> component.getStack().getChemical() == chemical ? -1 : 1))
                .forEach(component -> {
                    long maxInsert = toAdd.get() - component.insert(component.createStack(chemical, toAdd.get()), Action.EXECUTE, true).getAmount();
                    toAdd.addAndGet(-maxInsert);
                });
    }
}
