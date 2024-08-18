package fr.frinn.custommachinerymekanism.common.network.data;

import fr.frinn.custommachinery.api.network.DataType;
import fr.frinn.custommachinerymekanism.Registration;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class InfusionStackData extends ChemicalStackData<InfuseType, InfusionStack> {

    public InfusionStackData(short id, InfusionStack value) {
        super(id, value);
    }

    public InfusionStackData(short id, RegistryFriendlyByteBuf buf) {
        super(id, InfusionStack.OPTIONAL_STREAM_CODEC.decode(buf));
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, InfusionStack> codec() {
        return InfusionStack.OPTIONAL_STREAM_CODEC;
    }

    @Override
    public DataType<InfusionStackData, InfusionStack> getType() {
        return Registration.INFUSION_DATA.get();
    }
}
