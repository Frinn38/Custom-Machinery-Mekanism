package fr.frinn.custommachinerymekanism.common.component.handler;

import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.component.SlurryMachineComponent;
import fr.frinn.custommachinerymekanism.common.transfer.SidedSlurryTank;
import mekanism.api.chemical.slurry.ISlurryHandler;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SlurryComponentHandler extends ChemicalComponentHandler<Slurry, SlurryStack, ISlurryHandler, SlurryMachineComponent> {

    public SlurryComponentHandler(IMachineComponentManager manager, List<SlurryMachineComponent> components) {
        super(manager, components);
    }

    @Override
    public BlockCapability<ISlurryHandler, Direction> targetCap() {
        return Capabilities.SLURRY.block();
    }

    @Override
    public ISlurryHandler createSidedHandler(@Nullable Direction side) {
        return new SidedSlurryTank(this, side);
    }

    @Override
    public MachineComponentType<SlurryMachineComponent> getType() {
        return Registration.SLURRY_MACHINE_COMPONENT.get();
    }
}
