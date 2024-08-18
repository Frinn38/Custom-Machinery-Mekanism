package fr.frinn.custommachinerymekanism.common.network.data;

import fr.frinn.custommachinery.api.network.IData;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public abstract class ChemicalStackData<C extends Chemical<C>, S extends ChemicalStack<C>> implements IData<S> {

    private final short id;
    private final S value;

    public ChemicalStackData(short id, S value) {
        this.id = id;
        this.value = value;
    }

    public abstract StreamCodec<RegistryFriendlyByteBuf, S> codec();

    @Override
    public short getID() {
        return this.id;
    }

    @Override
    public S getValue() {
        return this.value;
    }

    @Override
    public void writeData(RegistryFriendlyByteBuf buffer) {
        IData.super.writeData(buffer);
        this.codec().encode(buffer, this.value);
    }
}
