package fr.frinn.custommachinerymekanism.common.utils;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.slurry.Slurry;

public class Codecs {

    public static final NamedCodec<Gas> GAS = NamedCodec.lazy(() -> NamedCodec.registrar(MekanismAPI.GAS_REGISTRY), "Gas");
    public static final NamedCodec<InfuseType> INFUSE_TYPE = NamedCodec.lazy(() -> NamedCodec.registrar(MekanismAPI.INFUSE_TYPE_REGISTRY), "Infuse type");
    public static final NamedCodec<Pigment> PIGMENT = NamedCodec.lazy(() -> NamedCodec.registrar(MekanismAPI.PIGMENT_REGISTRY), "Pigment");
    public static final NamedCodec<Slurry> SLURRY = NamedCodec.lazy(() -> NamedCodec.registrar(MekanismAPI.SLURRY_REGISTRY), "Slurry");
}
