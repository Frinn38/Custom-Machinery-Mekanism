package fr.frinn.custommachinerymekanism.common.component.handler;

import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.component.PigmentMachineComponent;
import fr.frinn.custommachinerymekanism.common.transfer.SidedPigmentTank;
import mekanism.api.chemical.pigment.IPigmentHandler;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PigmentComponentHandler extends ChemicalComponentHandler<Pigment, PigmentStack, IPigmentHandler, PigmentMachineComponent> {

    public PigmentComponentHandler(IMachineComponentManager manager, List<PigmentMachineComponent> components) {
        super(manager, components);
    }

    @Override
    public BlockCapability<IPigmentHandler, Direction> targetCap() {
        return Capabilities.PIGMENT.block();
    }

    @Override
    public IPigmentHandler createSidedHandler(@Nullable Direction side) {
        return new SidedPigmentTank(this, side);
    }

    @Override
    public MachineComponentType<PigmentMachineComponent> getType() {
        return Registration.PIGMENT_MACHINE_COMPONENT.get();
    }
}
