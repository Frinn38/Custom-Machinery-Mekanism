package fr.frinn.custommachinerymekanism.common.component;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.impl.component.config.SideConfig;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.utils.Codecs;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class PigmentMachineComponent extends ChemicalMachineComponent<Pigment, PigmentStack> {

    public PigmentMachineComponent(IMachineComponentManager manager, String id, long capacity, ComponentIOMode mode, List<Pigment> filter, boolean whitelist, long maxInput, long maxOutput, SideConfig.Template config) {
        super(manager, id, capacity, mode, filter, whitelist, maxInput, maxOutput, config);
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
    public PigmentStack readFromNBT(CompoundTag nbt) {
        return PigmentStack.readFromNBT(nbt);
    }

    public static class Template extends ChemicalMachineComponent.Template<Pigment, PigmentStack, PigmentMachineComponent> {

        public static final NamedCodec<ChemicalMachineComponent.Template<Pigment, PigmentStack, PigmentMachineComponent>> CODEC = makeCodec(Codecs.PIGMENT, Template::new);

        public Template(String id, long capacity, ComponentIOMode mode, List<Pigment> filter, boolean whitelist, long maxInput, long maxOutput, SideConfig.Template config) {
            super(id, capacity, mode, filter, whitelist, maxInput, maxOutput, config);
        }

        @Override
        public boolean isSameType(ChemicalStack<?> stack) {
            return stack instanceof PigmentStack;
        }

        @Override
        public PigmentMachineComponent build(IMachineComponentManager manager, String id, long capacity, ComponentIOMode mode, List<Pigment> filter, boolean whitelist, long maxInput, long maxOutput, SideConfig.Template config) {
            return new PigmentMachineComponent(manager, id, capacity, mode, filter, whitelist, maxInput, maxOutput, config);
        }

        @Override
        public MachineComponentType<PigmentMachineComponent> getType() {
            return Registration.PIGMENT_MACHINE_COMPONENT.get();
        }
    }
}
