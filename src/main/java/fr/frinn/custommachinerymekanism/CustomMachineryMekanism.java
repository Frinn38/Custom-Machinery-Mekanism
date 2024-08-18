package fr.frinn.custommachinerymekanism;

import fr.frinn.custommachinery.common.init.CustomMachineTile;
import fr.frinn.custommachinerymekanism.common.component.handler.GasComponentHandler;
import fr.frinn.custommachinerymekanism.common.component.handler.InfusionComponentHandler;
import fr.frinn.custommachinerymekanism.common.component.handler.PigmentComponentHandler;
import fr.frinn.custommachinerymekanism.common.component.handler.SlurryComponentHandler;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.api.chemical.infuse.IInfusionHandler;
import mekanism.api.chemical.pigment.IPigmentHandler;
import mekanism.api.chemical.slurry.ISlurryHandler;
import mekanism.api.heat.IHeatHandler;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

@Mod(CustomMachineryMekanism.MODID)
public class CustomMachineryMekanism {

    public static final String MODID = "custommachinerymekanism";

    public CustomMachineryMekanism(final IEventBus MOD_BUS) {
        Registration.MACHINE_COMPONENTS.register(MOD_BUS);
        Registration.GUI_ELEMENTS.register(MOD_BUS);
        Registration.REQUIREMENTS.register(MOD_BUS);
        Registration.DATAS.register(MOD_BUS);
        MOD_BUS.addListener(this::attachCapabilities);
    }

    private void attachCapabilities(final RegisterCapabilitiesEvent event) {
        //GAS
        event.registerBlockEntity(Capabilities.GAS.block(), fr.frinn.custommachinery.common.init.Registration.CUSTOM_MACHINE_TILE.get(), new ICapabilityProvider<>() {
            @Nullable
            @Override
            public IGasHandler getCapability(CustomMachineTile machine, Direction side) {
                return machine.getComponentManager().getComponentHandler(Registration.GAS_MACHINE_COMPONENT.get())
                        .map(handler -> ((GasComponentHandler)handler).getSidedHandler(side))
                        .orElse(null);
            }
        });
        //INFUSION
        event.registerBlockEntity(Capabilities.INFUSION.block(), fr.frinn.custommachinery.common.init.Registration.CUSTOM_MACHINE_TILE.get(), new ICapabilityProvider<>() {
            @Nullable
            @Override
            public IInfusionHandler getCapability(CustomMachineTile machine, Direction side) {
                return machine.getComponentManager().getComponentHandler(Registration.INFUSION_MACHINE_COMPONENT.get())
                        .map(handler -> ((InfusionComponentHandler)handler).getSidedHandler(side))
                        .orElse(null);
            }
        });
        //PIGMENT
        event.registerBlockEntity(Capabilities.PIGMENT.block(), fr.frinn.custommachinery.common.init.Registration.CUSTOM_MACHINE_TILE.get(), new ICapabilityProvider<>() {
            @Nullable
            @Override
            public IPigmentHandler getCapability(CustomMachineTile machine, Direction side) {
                return machine.getComponentManager().getComponentHandler(Registration.PIGMENT_MACHINE_COMPONENT.get())
                        .map(handler -> ((PigmentComponentHandler)handler).getSidedHandler(side))
                        .orElse(null);
            }
        });
        //SLURRY
        event.registerBlockEntity(Capabilities.SLURRY.block(), fr.frinn.custommachinery.common.init.Registration.CUSTOM_MACHINE_TILE.get(), new ICapabilityProvider<>() {
            @Nullable
            @Override
            public ISlurryHandler getCapability(CustomMachineTile machine, Direction side) {
                return machine.getComponentManager().getComponentHandler(Registration.SLURRY_MACHINE_COMPONENT.get())
                        .map(handler -> ((SlurryComponentHandler)handler).getSidedHandler(side))
                        .orElse(null);
            }
        });
        //HEAT
        event.registerBlockEntity(Capabilities.HEAT, fr.frinn.custommachinery.common.init.Registration.CUSTOM_MACHINE_TILE.get(), new ICapabilityProvider<>() {
            @Nullable
            @Override
            public IHeatHandler getCapability(CustomMachineTile machine, Direction side) {
                return machine.getComponentManager().getComponent(Registration.HEAT_MACHINE_COMPONENT.get())
                        .map(component -> component.getHeatHandler(side))
                        .orElse(null);
            }
        });
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
