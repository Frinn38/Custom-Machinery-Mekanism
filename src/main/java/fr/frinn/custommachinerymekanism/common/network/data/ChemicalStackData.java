package fr.frinn.custommachinerymekanism.common.network.data;

import fr.frinn.custommachinery.api.network.DataType;
import fr.frinn.custommachinery.api.network.IData;
import fr.frinn.custommachinerymekanism.Registration;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class ChemicalStackData implements IData<ChemicalStack> {

    private final short id;
    private final ChemicalStack value;

    public ChemicalStackData(short id, ChemicalStack value) {
        this.id = id;
        this.value = value;
    }

    public ChemicalStackData(short id, RegistryFriendlyByteBuf buf) {
        this.id = id;
        this.value = ChemicalStack.OPTIONAL_STREAM_CODEC.decode(buf);
    }

    @Override
    public DataType<ChemicalStackData, ChemicalStack> getType() {
        return Registration.CHEMICAL_DATA.get();
    }

    @Override
    public short getID() {
        return this.id;
    }

    @Override
    public ChemicalStack getValue() {
        return this.value;
    }

    @Override
    public void writeData(RegistryFriendlyByteBuf buffer) {
        IData.super.writeData(buffer);
        ChemicalStack.OPTIONAL_STREAM_CODEC.encode(buffer, this.value);
    }
}
