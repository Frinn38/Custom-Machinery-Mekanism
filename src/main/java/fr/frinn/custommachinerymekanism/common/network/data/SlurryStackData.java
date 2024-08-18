package fr.frinn.custommachinerymekanism.common.network.data;

import fr.frinn.custommachinery.api.network.DataType;
import fr.frinn.custommachinerymekanism.Registration;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class SlurryStackData extends ChemicalStackData<Slurry, SlurryStack> {

    public SlurryStackData(short id, SlurryStack value) {
        super(id, value);
    }

    public SlurryStackData(short id, RegistryFriendlyByteBuf buf) {
        super(id, SlurryStack.OPTIONAL_STREAM_CODEC.decode(buf));
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, SlurryStack> codec() {
        return SlurryStack.OPTIONAL_STREAM_CODEC;
    }

    @Override
    public DataType<SlurryStackData, SlurryStack> getType() {
        return Registration.SLURRY_DATA.get();
    }
}
