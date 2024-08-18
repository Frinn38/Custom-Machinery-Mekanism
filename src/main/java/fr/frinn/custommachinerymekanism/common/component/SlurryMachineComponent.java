package fr.frinn.custommachinerymekanism.common.component;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.impl.component.config.SideConfig;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.utils.Codecs;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class SlurryMachineComponent extends ChemicalMachineComponent<Slurry, SlurryStack> {

    public SlurryMachineComponent(IMachineComponentManager manager, String id, long capacity, ComponentIOMode mode, List<Slurry> filter, boolean whitelist, long maxInput, long maxOutput, SideConfig.Template config, boolean unique) {
        super(manager, id, capacity, mode, filter, whitelist, maxInput, maxOutput, config, unique);
    }

    @Override
    public MachineComponentType<SlurryMachineComponent> getType() {
        return Registration.SLURRY_MACHINE_COMPONENT.get();
    }

    @Override
    public SlurryStack empty() {
        return SlurryStack.EMPTY;
    }

    @Override
    public SlurryStack createStack(Slurry type, long amount) {
        return new SlurryStack(type, amount);
    }

    @Override
    public SlurryStack readFromNBT(CompoundTag nbt, HolderLookup.Provider registries) {
        return SlurryStack.parseOptional(registries, nbt);
    }

    public static class Template extends ChemicalMachineComponent.Template<Slurry, SlurryStack, SlurryMachineComponent> {

        public static final NamedCodec<ChemicalMachineComponent.Template<Slurry, SlurryStack, SlurryMachineComponent>> CODEC = makeCodec(Codecs.SLURRY, Template::new);

        public Template(String id, long capacity, ComponentIOMode mode, List<Slurry> filter, boolean whitelist, long maxInput, long maxOutput, SideConfig.Template config, boolean unique) {
            super(id, capacity, mode, filter, whitelist, maxInput, maxOutput, config, unique);
        }

        @Override
        public boolean isSameType(ChemicalStack<?> stack) {
            return stack instanceof SlurryStack;
        }

        @Override
        public SlurryMachineComponent build(IMachineComponentManager manager, String id, long capacity, ComponentIOMode mode, List<Slurry> filter, boolean whitelist, long maxInput, long maxOutput, SideConfig.Template config, boolean unique) {
            return new SlurryMachineComponent(manager, id, capacity, mode, filter, whitelist, maxInput, maxOutput, config, unique);
        }

        @Override
        public MachineComponentType<SlurryMachineComponent> getType() {
            return Registration.SLURRY_MACHINE_COMPONENT.get();
        }
    }
}
