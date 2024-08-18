package fr.frinn.custommachinerymekanism.common.network.syncable;

import fr.frinn.custommachinery.impl.network.AbstractSyncable;
import fr.frinn.custommachinerymekanism.common.network.data.ChemicalStackData;
import fr.frinn.custommachinerymekanism.common.network.data.GasStackData;
import fr.frinn.custommachinerymekanism.common.network.data.InfusionStackData;
import fr.frinn.custommachinerymekanism.common.network.data.PigmentStackData;
import fr.frinn.custommachinerymekanism.common.network.data.SlurryStackData;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.SlurryStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public abstract class ChemicalStackSyncable<C extends Chemical<C>, S extends ChemicalStack<C>, D extends ChemicalStackData<C, S>> extends AbstractSyncable<D, S> {

    @Override
    public D getData(short i) {
        S stack = get();
        if(stack instanceof GasStack gas)
            return (D) new GasStackData(i, gas);
        else if(stack instanceof SlurryStack slurry)
            return (D) new SlurryStackData(i, slurry);
        else if(stack instanceof InfusionStack infusion)
            return (D) new InfusionStackData(i, infusion);
        else if(stack instanceof PigmentStack pigment)
            return (D) new PigmentStackData(i, pigment);
        else
            throw new IllegalStateException("Can't get data for object: " + stack.getChemical().getClass().getSimpleName());
    }

    @Override
    public boolean needSync() {
        ChemicalStack<C> value = get();
        boolean needSync;
        if(this.lastKnownValue != null)
            needSync = !value.equals(this.lastKnownValue);
        else
            needSync = true;
        this.lastKnownValue = (S) value.copy();
        return needSync;
    }

    public static <C extends Chemical<C>, S extends ChemicalStack<C>, D extends ChemicalStackData<C, S>> ChemicalStackSyncable<C, S, D> create(Supplier<S> supplier, Consumer<S> consumer) {
        return new ChemicalStackSyncable<>() {

            @Override
            public S get() {
                return supplier.get();
            }

            @Override
            public void set(S stack) {
                consumer.accept(stack);
            }
        };
    }
}
