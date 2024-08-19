package fr.frinn.custommachinerymekanism.common.component;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.utils.Filter;
import fr.frinn.custommachinery.impl.component.config.SideConfig;
import fr.frinn.custommachinerymekanism.Registration;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class PigmentMachineComponent extends ChemicalMachineComponent<Pigment, PigmentStack> {

    public PigmentMachineComponent(IMachineComponentManager manager, String id, long capacity, ComponentIOMode mode, Filter<Pigment> filter, long maxInput, long maxOutput, SideConfig.Template config, boolean unique) {
        super(manager, id, capacity, mode, filter, maxInput, maxOutput, config, unique);
    }

    @Override
    public MachineComponentType<PigmentMachineComponent> getType() {
        return Registration.PIGMENT_MACHINE_COMPONENT.get();
    }

    @Override
    public PigmentStack empty() {
        return PigmentStack.EMPTY;
    }

    @Override
    public PigmentStack createStack(Pigment type, long amount) {
        return new PigmentStack(type, amount);
    }

    @Override
    public PigmentStack readFromNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        return PigmentStack.parseOptional(registries, nbt);
    }

    public static class Template extends ChemicalMachineComponent.Template<Pigment, PigmentStack, PigmentMachineComponent> {

        public static final NamedCodec<ChemicalMachineComponent.Template<Pigment, PigmentStack, PigmentMachineComponent>> CODEC = makeCodec(MekanismAPI.PIGMENT_REGISTRY, Template::new);

        public Template(String id, long capacity, ComponentIOMode mode, Filter<Pigment> filter, long maxInput, long maxOutput, SideConfig.Template config, boolean unique) {
            super(id, capacity, mode, filter, maxInput, maxOutput, config, unique);
        }

        @Override
        public boolean isSameType(ChemicalStack<?> stack) {
            return stack instanceof PigmentStack;
        }

        @Override
        public PigmentMachineComponent build(IMachineComponentManager manager, String id, long capacity, ComponentIOMode mode, Filter<Pigment> filter, long maxInput, long maxOutput, SideConfig.Template config, boolean unique) {
            return new PigmentMachineComponent(manager, id, capacity, mode, filter, maxInput, maxOutput, config, unique);
        }

        @Override
        public MachineComponentType<PigmentMachineComponent> getType() {
            return Registration.PIGMENT_MACHINE_COMPONENT.get();
        }
    }
}
