package fr.frinn.custommachinerymekanism;

import fr.frinn.custommachinery.common.init.CustomMachineTile;
import fr.frinn.custommachinerymekanism.common.component.handler.ChemicalComponentHandler;
import mekanism.api.chemical.IChemicalHandler;
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
        event.registerBlockEntity(Capabilities.CHEMICAL.block(), fr.frinn.custommachinery.common.init.Registration.CUSTOM_MACHINE_TILE.get(), new ICapabilityProvider<>() {
            @Nullable
            @Override
            public IChemicalHandler getCapability(CustomMachineTile machine, Direction side) {
                return machine.getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get())
                        .map(handler -> ((ChemicalComponentHandler)handler).getSidedHandler(side))
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
