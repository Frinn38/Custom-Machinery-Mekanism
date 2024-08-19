package fr.frinn.custommachinerymekanism.common.component;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IComparatorInputComponent;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.IMachineComponentTemplate;
import fr.frinn.custommachinery.api.component.ISerializableComponent;
import fr.frinn.custommachinery.api.component.ISideConfigComponent;
import fr.frinn.custommachinery.api.network.DataType;
import fr.frinn.custommachinery.api.network.ISyncable;
import fr.frinn.custommachinery.api.network.ISyncableStuff;
import fr.frinn.custommachinery.api.utils.Filter;
import fr.frinn.custommachinery.impl.codec.DefaultCodecs;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import fr.frinn.custommachinery.impl.component.config.SideConfig;
import fr.frinn.custommachinerymekanism.common.network.syncable.ChemicalStackSyncable;
import mekanism.api.Action;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class ChemicalMachineComponent<C extends Chemical<C>, S extends ChemicalStack<C>> extends AbstractMachineComponent implements ISerializableComponent, ISyncableStuff, IComparatorInputComponent, ISideConfigComponent {

    private final String id;
    private final long capacity;
    private final Filter<C> filter;
    private final long maxInput;
    private final long maxOutput;
    private final SideConfig config;
    private final boolean unique;

    private S stack = empty();

    public ChemicalMachineComponent(IMachineComponentManager manager, String id, long capacity, ComponentIOMode mode, Filter<C> filter, long maxInput, long maxOutput, SideConfig.Template config, boolean unique) {
        super(manager, mode);
        this.id = id;
        this.capacity = capacity;
        this.filter = filter;
        this.maxInput = maxInput;
        this.maxOutput = maxOutput;
        this.config = config.build(this);
        this.unique = unique;
    }

    public abstract S empty();

    public abstract S createStack(C type, long amount);

    public abstract S readFromNBT(CompoundTag nbt, HolderLookup.Provider registries);

    public S createStack(S stack, long amount) {
        return createStack(stack.getChemical(), amount);
    }

    public String getId() {
        return this.id;
    }

    public long getCapacity() {
        return this.capacity;
    }

    public S getStack() {
        return this.stack;
    }

    public void setStack(S stack) {
        this.stack = stack;
        getManager().markDirty();
    }

    public boolean isValid(S stack) {
        //Check unique
        if(this.unique && this.stack.isEmpty() && this.getManager()
                .getComponentHandler(getType())
                .stream()
                .flatMap(handler -> handler.getComponents().stream())
                .anyMatch(component -> component != this && component instanceof ChemicalMachineComponent<?, ?> chemical && stack.getChemical() == chemical.stack.getChemical()))
            return false;

        //Check filter
        if(!this.filter.test(stack.getChemical()))
            return false;

        //Check if same chemical
        return this.stack.isEmpty() || this.stack.is(stack.getChemical());
    }

    //Return the remaining that was not inserted
    public S insert(S stack, Action action, boolean byPassLimit) {
        if(!isValid(stack))
            return stack;

        if(!this.stack.isEmpty() && this.stack.getChemical() != stack.getChemical())
            return stack;

        long maxInsert = this.stack.isEmpty() ? Math.min(this.capacity, stack.getAmount()) : Math.min(this.capacity - this.stack.getAmount(), stack.getAmount());
        if(!byPassLimit)
            maxInsert = Math.min(maxInsert, this.maxInput);
        if(action.execute())
            setStack(createStack(stack, maxInsert + (this.stack.isEmpty() ? 0 : this.stack.getAmount())));
        return createStack(stack, stack.getAmount() - maxInsert);
    }

    //Return the extracted stack
    public S extract(long amount, Action action, boolean byPassLimit) {
        if(this.stack.isEmpty())
            return empty();

        long maxExtract = Math.min(this.stack.getAmount(), amount);
        if(!byPassLimit)
            maxExtract = Math.min(maxExtract, this.maxOutput);
        C type = this.stack.getChemical();
        if(action.execute()) {
            this.stack.shrink(maxExtract);
            getManager().markDirty();
        }
        return createStack(type, maxExtract);
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
            this.stack = readFromNBT(nbt.getCompound("stack"), registries);
        if(nbt.contains("config", Tag.TAG_COMPOUND))
            this.config.deserialize(nbt.getCompound("config"));
    }

    @Override
    public SideConfig getConfig() {
        return this.config;
    }

    @Override
    public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {
        container.accept(ChemicalStackSyncable.create(this::getStack, this::setStack));
        container.accept(DataType.createSyncable(SideConfig.class, this::getConfig, this.config::set));
    }

    public static abstract class Template<C extends Chemical<C>, S extends ChemicalStack<C>, T extends ChemicalMachineComponent<C, S>> implements IMachineComponentTemplate<T> {

        public interface Builder<C extends Chemical<C>, S extends ChemicalStack<C>, CM extends ChemicalMachineComponent<C, S>, T extends Template<C, S, CM>> {
            T build(String id, long capacity, ComponentIOMode mode, Filter<C> filter, long maxInput, long maxOutput, SideConfig.Template config, boolean unique);
        }

        public static <C extends Chemical<C>, S extends ChemicalStack<C>, CM extends ChemicalMachineComponent<C, S>, T extends Template<C, S, CM>> NamedCodec<T> makeCodec(Registry<C> registry, Builder<C, S, CM, T> builder) {
            return NamedCodec.record(templateInstance ->
                    templateInstance.group(
                            NamedCodec.STRING.fieldOf("id").forGetter(template -> template.id),
                            NamedCodec.LONG.fieldOf("capacity").forGetter(template -> template.capacity),
                            ComponentIOMode.CODEC.optionalFieldOf("mode", ComponentIOMode.BOTH).forGetter(template -> template.mode),
                            Filter.codec(DefaultCodecs.registryValueOrTag(registry)).orElse(Filter.empty()).forGetter(template -> template.filter),
                            NamedCodec.LONG.optionalFieldOf("max_input").forGetter(template -> template.maxInput == template.capacity ? Optional.empty() : Optional.of(template.maxInput)),
                            NamedCodec.LONG.optionalFieldOf("max_output").forGetter(template -> template.maxOutput == template.capacity ? Optional.empty() : Optional.of(template.maxOutput)),
                            SideConfig.Template.CODEC.optionalFieldOf("config").forGetter(template -> template.config == template.mode.getBaseConfig() ? Optional.empty() : Optional.of(template.config)),
                            NamedCodec.BOOL.optionalFieldOf("unique", false).forGetter(template -> template.unique)
                    ).apply(templateInstance, (id, capacity, mode, filter, maxInput, maxOutput, config, unique) ->
                            builder.build(id, capacity, mode, filter, maxInput.orElse(capacity), maxOutput.orElse(capacity), config.orElse(mode.getBaseConfig()), unique)
                    ), "Chemical machine component"
            );
        }

        public final String id;
        public final long capacity;
        public final ComponentIOMode mode;
        public final Filter<C> filter;
        public final long maxInput;
        public final long maxOutput;
        public final SideConfig.Template config;
        public final boolean unique;

        public Template(String id, long capacity, ComponentIOMode mode, Filter<C> filter, long maxInput, long maxOutput, SideConfig.Template config, boolean unique) {
            this.id = id;
            this.capacity = capacity;
            this.mode = mode;
            this.filter = filter;
            this.maxInput = maxInput;
            this.maxOutput = maxOutput;
            this.config = config;
            this.unique = unique;
        }

        public abstract boolean isSameType(ChemicalStack<?> stack);

        @Override
        public String getId() {
            return this.id;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean canAccept(Object ingredient, boolean isInput, IMachineComponentManager manager) {
            if(this.mode != ComponentIOMode.BOTH && isInput != this.mode.isInput())
                return false;
            if(ingredient instanceof ChemicalStack<?> stack && isSameType(stack)) {
                return this.filter.test((C)stack.getChemical());
            } else if(ingredient instanceof List<?> list) {
                return list.stream().allMatch(object -> {
                    if(object instanceof ChemicalStack<?> stack && isSameType(stack))
                        return this.filter.test((C)stack.getChemical());
                    return false;
                });
            }
            return false;
        }

        public abstract T build(IMachineComponentManager manager, String id, long capacity, ComponentIOMode mode, Filter<C> filter, long maxInput, long maxOutput, SideConfig.Template config, boolean unique);

        @Override
        public T build(IMachineComponentManager manager) {
            return build(manager, this.id, this.capacity, this.mode, this.filter, this.maxInput, this.maxOutput, this.config, this.unique);
        }
    }
}
