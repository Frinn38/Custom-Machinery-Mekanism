package fr.frinn.custommachinerymekanism.common.component.handler;

import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.component.InfusionMachineComponent;
import fr.frinn.custommachinerymekanism.common.transfer.SidedInfusionTank;
import mekanism.api.chemical.infuse.IInfusionHandler;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfusionComponentHandler extends ChemicalComponentHandler<InfuseType, InfusionStack, IInfusionHandler, InfusionMachineComponent> {

    public InfusionComponentHandler(IMachineComponentManager manager, List<InfusionMachineComponent> components) {
        super(manager, components);
    }

    @Override
    public BlockCapability<IInfusionHandler, Direction> targetCap() {
        return Capabilities.INFUSION.block();
    }

    @Override
    public IInfusionHandler createSidedHandler(@Nullable Direction side) {
        return new SidedInfusionTank(this, side);
    }

    @Override
    public MachineComponentType<InfusionMachineComponent> getType() {
        return Registration.INFUSION_MACHINE_COMPONENT.get();
    }
}
