package fr.frinn.custommachinerymekanism.common.integration.kubejs;

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
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CustomMachineryMekanismKubeJSPlugin implements KubeJSPlugin {

    private static final Codec<TemperatureUnit> TEMPERATURE_UNIT_CODEC = NamedCodec.enumCodec(TemperatureUnit.class).codec();

    @Override
    public void registerTypeWrappers(final TypeWrapperRegistry registry) {
        registry.register(GasStack.class, (ContextFromFunction<GasStack>) (ctx, o) -> of(o, MekanismAPI.EMPTY_GAS, GasStack.EMPTY, MekanismAPI.GAS_REGISTRY::get, GasStack::new));
        registry.register(InfusionStack.class, (ContextFromFunction<InfusionStack>) (ctx, o) -> of(o, MekanismAPI.EMPTY_INFUSE_TYPE, InfusionStack.EMPTY, MekanismAPI.INFUSE_TYPE_REGISTRY::get, InfusionStack::new));
        registry.register(PigmentStack.class, (ContextFromFunction<PigmentStack>) (ctx, o) -> of(o, MekanismAPI.EMPTY_PIGMENT, PigmentStack.EMPTY, MekanismAPI.PIGMENT_REGISTRY::get, PigmentStack::new));
        registry.register(SlurryStack.class, (ContextFromFunction<SlurryStack>) (ctx, o) -> of(o, MekanismAPI.EMPTY_SLURRY, SlurryStack.EMPTY, MekanismAPI.SLURRY_REGISTRY::get, SlurryStack::new));
        registry.register(TemperatureUnit.class, (TypeWrapperFactory<TemperatureUnit>) TypeInfo.of(TemperatureUnit.class));
    }

    @SuppressWarnings("unchecked")
    private static <C extends Chemical<C>, S extends ChemicalStack<C>> S of(Object o, C air, S empty, Function<ResourceLocation, C> getter, BiFunction<C, Long, S> maker) {
        final long BASE_AMOUNT = 1000L;

        if(o instanceof Wrapper w)
            o = w.unwrap();

        if(o == null || o == empty)
            return empty;
        else if(o.getClass().isInstance(empty.getClass()))
            return (S)o;
        else if (o.getClass().isInstance(air.getClass()) && o != air) {
            return maker.apply((C)o, BASE_AMOUNT);
        } else if(o instanceof ResourceLocation loc) {
            C chemical = getter.apply(loc);
            if(chemical == air)
                throw new KubeRuntimeException("Chemical " + loc + " not found!");
            return maker.apply(chemical, BASE_AMOUNT);
        } else if (o instanceof CharSequence) {
            String s = o.toString().trim();
            long amount = BASE_AMOUNT;

            if (s.isEmpty() || s.equals("-") || s.equals("empty")) {
                return empty;
            }

            String[] s1 = s.split(" ", 2);
            if(s1.length == 2)
                amount = Long.parseLong(s1[1]);

            C chemical = getter.apply(ResourceLocation.parse(s1[0]));
            if(chemical == air)
                throw new KubeRuntimeException("Chemical " + s1[0] + " not found!");
            return maker.apply(chemical, amount);
        }

        return empty;
    }
}
