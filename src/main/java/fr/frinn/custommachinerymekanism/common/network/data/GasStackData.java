package fr.frinn.custommachinerymekanism.common.network.data;

import fr.frinn.custommachinery.api.network.DataType;
import fr.frinn.custommachinerymekanism.Registration;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class GasStackData extends ChemicalStackData<Gas, GasStack> {

    public GasStackData(short id, GasStack value) {
        super(id, value);
    }

    public GasStackData(short id, RegistryFriendlyByteBuf buf) {
        super(id, GasStack.OPTIONAL_STREAM_CODEC.decode(buf));
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, GasStack> codec() {
        return GasStack.OPTIONAL_STREAM_CODEC;
    }

    @Override
    public DataType<GasStackData, GasStack> getType() {
        return Registration.GAS_DATA.get();
    }
}
