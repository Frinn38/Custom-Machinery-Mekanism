package fr.frinn.custommachinerymekanism.common.integration.kubejs;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry.ContextFromFunction;
import dev.latvian.mods.rhino.Wrapper;
import dev.latvian.mods.rhino.type.TypeInfo;
import dev.latvian.mods.rhino.util.wrap.TypeWrapperFactory;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.fluids.FluidType;

public class CustomMachineryMekanismKubeJSPlugin implements KubeJSPlugin {

    private static final Codec<TemperatureUnit> TEMPERATURE_UNIT_CODEC = NamedCodec.enumCodec(TemperatureUnit.class).codec();

    @Override
    public void registerTypeWrappers(final TypeWrapperRegistry registry) {
        registry.register(ChemicalStack.class, (ContextFromFunction<ChemicalStack>) (ctx, o) -> of(o));
        registry.register(TemperatureUnit.class, (TypeWrapperFactory<TemperatureUnit>) TypeInfo.of(TemperatureUnit.class));
    }

    private static ChemicalStack of(Object o) {
        final long BASE_AMOUNT = FluidType.BUCKET_VOLUME;

        if(o instanceof Wrapper w)
            o = w.unwrap();

        if(o == null || o == ChemicalStack.EMPTY)
            return ChemicalStack.EMPTY;
        else if(o instanceof ChemicalStack stack)
            return stack;
        else if (o instanceof Chemical chemical) {
            return new ChemicalStack(chemical, BASE_AMOUNT);
        } else if(o instanceof ResourceLocation loc) {
            Chemical chemical = MekanismAPI.CHEMICAL_REGISTRY.get(loc);
            if(chemical == MekanismAPI.EMPTY_CHEMICAL)
                throw new KubeRuntimeException("Chemical " + loc + " not found!");
            return new ChemicalStack(chemical, BASE_AMOUNT);
        } else {
            try {
                var reader = new StringReader(o.toString());
                reader.skipWhitespace();

                if (!reader.canRead() || reader.peek() == '-')
                    return ChemicalStack.EMPTY;

                long amount = BASE_AMOUNT;
                if(StringReader.isAllowedNumber(reader.peek())) {
                    double amountd = reader.readDouble();
                    reader.skipWhitespace();

                    if (reader.peek() == 'b' || reader.peek() == 'B') {
                        reader.skip();
                        reader.skipWhitespace();
                        amountd *= FluidType.BUCKET_VOLUME;
                    }

                    if (reader.peek() == '/') {
                        reader.skip();
                        reader.skipWhitespace();
                        amountd = amountd / reader.readDouble();
                    }

                    amount = Mth.ceil(amountd);
                    reader.expect('x');
                    reader.skipWhitespace();

                    if (amount < 1)
                        throw new IllegalArgumentException("Fluid amount smaller than 1 is not allowed!");
                }
                ResourceLocation chemicalId = ResourceLocation.read(reader);
                return new ChemicalStack(MekanismAPI.CHEMICAL_REGISTRY.get(chemicalId), amount);
            } catch (CommandSyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
