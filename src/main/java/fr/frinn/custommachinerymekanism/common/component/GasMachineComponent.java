package fr.frinn.custommachinerymekanism.common.component;

import com.mojang.serialization.Codec;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.impl.component.config.SideConfig;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.utils.Codecs;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class GasMachineComponent extends ChemicalMachineComponent<Gas, GasStack> {

    public GasMachineComponent(IMachineComponentManager manager, String id, long capacity, ComponentIOMode mode, List<Gas> filter, boolean whitelist, long maxInput, long maxOutput, SideConfig.Template config) {
        super(manager, id, capacity, mode, filter, whitelist, maxInput, maxOutput, config);
    }

    @Override
    public MachineComponentType<GasMachineComponent> getType() {
        return Registration.GAS_MACHINE_COMPONENT.get();
    }

    @Override
    public GasStack empty() {
        return GasStack.EMPTY;
    }

    @Override
    public GasStack createStack(Gas type, long amount) {
        return new GasStack(type, amount);
    }

    @Override
    public GasStack readFromNBT(CompoundTag nbt) {
        return GasStack.readFromNBT(nbt);
    }

    public static class Template extends ChemicalMachineComponent.Template<Gas, GasStack, GasMachineComponent> {

        public static final Codec<ChemicalMachineComponent.Template<Gas, GasStack, GasMachineComponent>> CODEC = makeCodec(Codecs.GAS, Template::new);

        public Template(String id, long capacity, ComponentIOMode mode, List<Gas> filter, boolean whitelist, long maxInput, long maxOutput, SideConfig.Template config) {
            super(id, capacity, mode, filter, whitelist, maxInput, maxOutput, config);
        }

        @Override
        public MachineComponentType<GasMachineComponent> getType() {
            return Registration.GAS_MACHINE_COMPONENT.get();
        }

        @Override
        public GasMachineComponent build(IMachineComponentManager manager, String id, long capacity, ComponentIOMode mode, List<Gas> filter, boolean whitelist, long maxInput, long maxOutput, SideConfig.Template config) {
            return new GasMachineComponent(manager, id, capacity, mode, filter, whitelist, maxInput, maxOutput, config);
        }
    }
}
