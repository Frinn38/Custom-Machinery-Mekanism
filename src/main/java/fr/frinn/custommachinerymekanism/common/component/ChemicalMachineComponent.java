package fr.frinn.custommachinerymekanism.common.component;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IComparatorInputComponent;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.IMachineComponentTemplate;
import fr.frinn.custommachinery.api.component.ISerializableComponent;
import fr.frinn.custommachinery.api.component.ISideConfigComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.network.DataType;
import fr.frinn.custommachinery.api.network.ISyncable;
import fr.frinn.custommachinery.api.network.ISyncableStuff;
import fr.frinn.custommachinery.api.utils.Filter;
import fr.frinn.custommachinery.impl.codec.DefaultCodecs;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import fr.frinn.custommachinery.impl.component.config.IOSideConfig;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.network.syncable.ChemicalStackSyncable;
import mekanism.api.Action;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ChemicalMachineComponent extends AbstractMachineComponent implements ISerializableComponent, ISyncableStuff, IComparatorInputComponent, ISideConfigComponent {

    private final String id;
    private final long capacity;
    private final Filter<Chemical> filter;
    private final long maxInput;
    private final long maxOutput;
    private final IOSideConfig config;
    private final boolean unique;

    private ChemicalStack stack = ChemicalStack.EMPTY;

    public ChemicalMachineComponent(IMachineComponentManager manager, String id, long capacity, ComponentIOMode mode, Filter<Chemical> filter, long maxInput, long maxOutput, IOSideConfig.Template config, boolean unique) {
        super(manager, mode);
        this.id = id;
        this.capacity = capacity;
        this.filter = filter;
        this.maxInput = maxInput;
        this.maxOutput = maxOutput;
        this.config = config.build(this);
        this.unique = unique;
    }

    @Override
    public MachineComponentType<ChemicalMachineComponent> getType() {
        return Registration.CHEMICAL_MACHINE_COMPONENT.get();
    }

    public String getId() {
        return this.id;
    }

    public long getCapacity() {
        return this.capacity;
    }

    public ChemicalStack getStack() {
        return this.stack;
    }

    public void setStack(ChemicalStack stack) {
        this.stack = stack;
        getManager().markDirty();
    }

    public boolean isValid(ChemicalStack stack) {
        //Check unique
        if(this.unique && this.stack.isEmpty() && this.getManager()
                .getComponentHandler(getType())
                .stream()
                .flatMap(handler -> handler.getComponents().stream())
                .anyMatch(component -> component != this && component instanceof ChemicalMachineComponent chemical && stack.getChemical() == chemical.stack.getChemical()))
            return false;

        //Check filter
        if(!this.filter.test(stack.getChemical()))
            return false;

        //Check if same chemical
        return this.stack.isEmpty() || this.stack.is(stack.getChemical());
    }

    //Return the remaining that was not inserted
    public ChemicalStack insert(ChemicalStack stack, Action action, boolean byPassLimit) {
        if(!isValid(stack))
            return stack;

        if(!this.stack.isEmpty() && this.stack.getChemical() != stack.getChemical())
            return stack;

        long maxInsert = this.stack.isEmpty() ? Math.min(this.capacity, stack.getAmount()) : Math.min(this.capacity - this.stack.getAmount(), stack.getAmount());
        if(!byPassLimit)
            maxInsert = Math.min(maxInsert, this.maxInput);
        if(action.execute())
            setStack(new ChemicalStack(stack.getChemical(), maxInsert + (this.stack.isEmpty() ? 0 : this.stack.getAmount())));
        return new ChemicalStack(stack.getChemical(), stack.getAmount() - maxInsert);
    }

    //Return the extracted stack
    public ChemicalStack extract(long amount, Action action, boolean byPassLimit) {
        if(this.stack.isEmpty())
            return ChemicalStack.EMPTY;

        long maxExtract = Math.min(this.stack.getAmount(), amount);
        if(!byPassLimit)
            maxExtract = Math.min(maxExtract, this.maxOutput);
        Chemical type = this.stack.getChemical();
        if(action.execute()) {
            this.stack.shrink(maxExtract);
            getManager().markDirty();
        }
        return new ChemicalStack(type, maxExtract);
    }

    @Override
    public int getComparatorInput() {
        return (int) (15 * ((double)this.stack.getAmount() / (double)this.capacity));
    }

    @Override
    public void serialize(CompoundTag nbt, HolderLookup.Provider registries) {
        if(!this.stack.isEmpty())
            nbt.put("stack", this.stack.save(registries));
        nbt.put("config", this.config.serialize());
    }

    @Override
    public void deserialize(CompoundTag nbt, HolderLookup.Provider registries) {
        if(nbt.contains("stack"))
            this.stack = ChemicalStack.parseOptional(registries, nbt.getCompound("stack"));
        if(nbt.contains("config", Tag.TAG_COMPOUND))
            this.config.deserialize(nbt.getCompound("config"));
    }

    @Override
    public IOSideConfig getConfig() {
        return this.config;
    }

    @Override
    public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {
        container.accept(ChemicalStackSyncable.create(this::getStack, this::setStack));
        container.accept(DataType.createSyncable(IOSideConfig.class, this::getConfig, this.config::set));
    }

    public record Template(String id, long capacity, ComponentIOMode mode, Filter<Chemical> filter, long maxInput, long maxOutput, IOSideConfig.Template config, boolean unique) implements IMachineComponentTemplate<ChemicalMachineComponent> {

        public static NamedCodec<Template> CODEC = NamedCodec.record(templateInstance ->
                templateInstance.group(
                        NamedCodec.STRING.fieldOf("id").forGetter(template -> template.id),
                        NamedCodec.LONG.fieldOf("capacity").forGetter(template -> template.capacity),
                        ComponentIOMode.CODEC.optionalFieldOf("mode", ComponentIOMode.BOTH).forGetter(template -> template.mode),
                        Filter.codec(DefaultCodecs.registryValueOrTag(MekanismAPI.CHEMICAL_REGISTRY)).orElse(Filter.empty()).forGetter(template -> template.filter),
                        NamedCodec.LONG.optionalFieldOf("max_input").forGetter(template -> template.maxInput == template.capacity ? Optional.empty() : Optional.of(template.maxInput)),
                        NamedCodec.LONG.optionalFieldOf("max_output").forGetter(template -> template.maxOutput == template.capacity ? Optional.empty() : Optional.of(template.maxOutput)),
                        IOSideConfig.Template.CODEC.optionalFieldOf("config").forGetter(template -> template.config == template.mode.getBaseConfig() ? Optional.empty() : Optional.of(template.config)),
                        NamedCodec.BOOL.optionalFieldOf("unique", false).forGetter(template -> template.unique)
                ).apply(templateInstance, (id, capacity, mode, filter, maxInput, maxOutput, config, unique) ->
                        new Template(id, capacity, mode, filter, maxInput.orElse(capacity), maxOutput.orElse(capacity), config.orElse(mode.getBaseConfig()), unique)
                ), "Chemical machine component"
        );

        @Override
        public MachineComponentType<ChemicalMachineComponent> getType() {
            return Registration.CHEMICAL_MACHINE_COMPONENT.get();
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public boolean canAccept(Object ingredient, boolean isInput, IMachineComponentManager manager) {
            if(this.mode != ComponentIOMode.BOTH && isInput != this.mode.isInput())
                return false;
            if(ingredient instanceof ChemicalStack stack) {
                return this.filter.test(stack.getChemical());
            } else if(ingredient instanceof List<?> list) {
                return list.stream().allMatch(object -> {
                    if(object instanceof ChemicalStack stack)
                        return this.filter.test(stack.getChemical());
                    return false;
                });
            }
            return false;
        }

        @Override
        public ChemicalMachineComponent build(IMachineComponentManager manager) {
            return new ChemicalMachineComponent(manager, this.id, this.capacity, this.mode, this.filter, this.maxInput, this.maxOutput, this.config, this.unique);
        }
    }
}
