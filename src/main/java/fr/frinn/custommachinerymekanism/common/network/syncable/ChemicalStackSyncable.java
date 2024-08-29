package fr.frinn.custommachinerymekanism.common.network.syncable;

import fr.frinn.custommachinery.impl.network.AbstractSyncable;
import fr.frinn.custommachinerymekanism.common.network.data.ChemicalStackData;
import mekanism.api.chemical.ChemicalStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ChemicalStackSyncable extends AbstractSyncable<ChemicalStackData, ChemicalStack> {

    @Override
    public ChemicalStackData getData(short i) {
        ChemicalStack stack = get();
        return new ChemicalStackData(i, stack);
    }

    @Override
    public boolean needSync() {
        ChemicalStack value = get();
        boolean needSync;
        if(this.lastKnownValue != null)
            needSync = !value.equals(this.lastKnownValue);
        else
            needSync = true;
        this.lastKnownValue = value.copy();
        return needSync;
    }

    public static ChemicalStackSyncable create(Supplier<ChemicalStack> supplier, Consumer<ChemicalStack> consumer) {
        return new ChemicalStackSyncable() {

            @Override
            public ChemicalStack get() {
                return supplier.get();
            }

            @Override
            public void set(ChemicalStack stack) {
                consumer.accept(stack);
            }
        };
    }
}
