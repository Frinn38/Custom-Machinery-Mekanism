package fr.frinn.custommachinerymekanism.common.component;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.impl.component.config.SideConfig;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.utils.Codecs;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class InfusionMachineComponent extends ChemicalMachineComponent<InfuseType, InfusionStack> {

    public InfusionMachineComponent(IMachineComponentManager manager, String id, long capacity, ComponentIOMode mode, List<InfuseType> filter, boolean whitelist, long maxInput, long maxOutput, SideConfig.Template config, boolean unique) {
        super(manager, id, capacity, mode, filter, whitelist, maxInput, maxOutput, config, unique);
    }

    @Override
    public MachineComponentType<InfusionMachineComponent> getType() {
        return Registration.INFUSION_MACHINE_COMPONENT.get();
    }

    @Override
    public InfusionStack empty() {
        return InfusionStack.EMPTY;
    }

    @Override
    public InfusionStack createStack(InfuseType type, long amount) {
        return new InfusionStack(type, amount);
    }

    @Override
    public InfusionStack readFromNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        return InfusionStack.parseOptional(registries, nbt);
    }

    public static class Template extends ChemicalMachineComponent.Template<InfuseType, InfusionStack, InfusionMachineComponent> {

        public static final NamedCodec<ChemicalMachineComponent.Template<InfuseType, InfusionStack, InfusionMachineComponent>> CODEC = makeCodec(Codecs.INFUSE_TYPE, Template::new);

        public Template(String id, long capacity, ComponentIOMode mode, List<InfuseType> filter, boolean whitelist, long maxInput, long maxOutput, SideConfig.Template config, boolean unique) {
            super(id, capacity, mode, filter, whitelist, maxInput, maxOutput, config, unique);
        }

        @Override
        public boolean isSameType(ChemicalStack<?> stack) {
            return stack instanceof InfusionStack;
        }

        @Override
        public InfusionMachineComponent build(IMachineComponentManager manager, String id, long capacity, ComponentIOMode mode, List<InfuseType> filter, boolean whitelist, long maxInput, long maxOutput, SideConfig.Template config, boolean unique) {
            return new InfusionMachineComponent(manager, id, capacity, mode, filter, whitelist, maxInput, maxOutput, config, unique);
        }

        @Override
        public MachineComponentType<InfusionMachineComponent> getType() {
            return Registration.INFUSION_MACHINE_COMPONENT.get();
        }
    }
}
