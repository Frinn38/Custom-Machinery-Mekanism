package fr.frinn.custommachinerymekanism.common.component;

import com.google.common.base.Suppliers;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import fr.frinn.custommachinerymekanism.Registration;
import mekanism.api.Chunk3D;
import mekanism.api.radiation.IRadiationManager;
import mekanism.api.radiation.IRadiationSource;
import net.minecraft.core.GlobalPos;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class RadiationMachineComponent extends AbstractMachineComponent {

    private final Supplier<GlobalPos> coords;

    public RadiationMachineComponent(IMachineComponentManager manager) {
        super(manager, ComponentIOMode.NONE);
        this.coords = Suppliers.memoize(() -> new GlobalPos(manager.getTile().getLevel().dimension(), manager.getTile().getBlockPos()));
    }

    public double getRadiations() {
        return IRadiationManager.INSTANCE.getRadiationLevel(this.coords.get());
    }

    public void removeRadiations(double amount, int radius) {
        Set<Chunk3D> checkChunks = new Chunk3D(this.coords.get()).expand((int)Math.ceil(radius / 16.0D));

        for (Chunk3D chunk : checkChunks) {
            for (Map.Entry<GlobalPos, IRadiationSource> entry : IRadiationManager.INSTANCE.getRadiationSources().row(chunk).entrySet()) {
                if(entry.getKey().pos().distSqr(this.coords.get().pos()) <= radius * radius) {
                    IRadiationSource source = entry.getValue();
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
        IRadiationManager.INSTANCE.radiate(this.coords.get(), amount);
    }

    @Override
    public MachineComponentType<RadiationMachineComponent> getType() {
        return Registration.RADIATION_MACHINE_COMPONENT.get();
    }
}
