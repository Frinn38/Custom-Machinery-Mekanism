package fr.frinn.custommachinerymekanism.common.network.data;

import fr.frinn.custommachinery.api.network.DataType;
import fr.frinn.custommachinerymekanism.Registration;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class PigmentStackData extends ChemicalStackData<Pigment, PigmentStack> {

    public PigmentStackData(short id, PigmentStack value) {
        super(id, value);
    }

    public PigmentStackData(short id, RegistryFriendlyByteBuf buf) {
        super(id, PigmentStack.OPTIONAL_STREAM_CODEC.decode(buf));
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, PigmentStack> codec() {
        return PigmentStack.OPTIONAL_STREAM_CODEC;
    }

    @Override
    public DataType<PigmentStackData, PigmentStack> getType() {
        return Registration.PIGMENT_DATA.get();
    }
}
