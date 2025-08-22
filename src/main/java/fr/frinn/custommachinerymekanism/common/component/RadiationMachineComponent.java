package fr.frinn.custommachinerymekanism.common.component;

import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import fr.frinn.custommachinerymekanism.Registration;
import mekanism.api.Chunk3D;
import mekanism.api.radiation.IRadiationManager;
import mekanism.api.radiation.IRadiationSource;
import net.minecraft.core.GlobalPos;

import java.util.Set;

public class RadiationMachineComponent extends AbstractMachineComponent {

    public RadiationMachineComponent(IMachineComponentManager manager) {
        super(manager, ComponentIOMode.NONE);
    }

    public double getRadiations() {
        return IRadiationManager.INSTANCE.getRadiationLevel(getManager().getLevel(), getManager().getTile().getBlockPos());
    }

    public void removeRadiations(double amount, int radius) {
        Set<Chunk3D> checkChunks = new Chunk3D(new GlobalPos(getManager().getLevel().dimension(), getManager().getTile().getBlockPos())).expand((int)Math.ceil(radius / 16.0D));

        for(Chunk3D chunk : checkChunks) {
            for(IRadiationSource source : IRadiationManager.INSTANCE.getRadiationSources(getManager().getLevel(), chunk.x, chunk.z)) {
                if(source.getPosition().distSqr(getManager().getTile().getBlockPos()) <= radius * radius) {
                    double toRemove = Math.min(source.getMagnitude(), amount);
                    source.radiate(-toRemove);
                    amount -= toRemove;
                    if(amount <= 0.0D)
                        return;
                }
            }
        }
    }

    public void addRadiations(double amount) {
        IRadiationManager.INSTANCE.radiate(getManager().getLevel(), getManager().getTile().getBlockPos(), amount);
    }

    @Override
    public MachineComponentType<RadiationMachineComponent> getType() {
        return Registration.RADIATION_MACHINE_COMPONENT.get();
    }
}
