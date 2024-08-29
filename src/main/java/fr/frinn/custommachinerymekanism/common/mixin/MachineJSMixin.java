package fr.frinn.custommachinerymekanism.common.mixin;

import fr.frinn.custommachinery.common.init.CustomMachineTile;
import fr.frinn.custommachinery.common.integration.kubejs.function.MachineJS;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.component.ChemicalMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.RadiationMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.handler.ChemicalComponentHandler;
import mekanism.api.Action;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.heat.IHeatCapacitor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(value = MachineJS.class, remap = false)
public abstract class MachineJSMixin {

    @Final
    @Shadow(remap = false)
    private CustomMachineTile internal;

    /** GAS **/

    public ChemicalStack getChemicalStored(String tank) {
        return this.internal.getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get())
                .flatMap(handler -> handler.getComponentForID(tank))
                .map(component -> component.getStack().copy())
                .orElse(ChemicalStack.EMPTY);
    }

    public void setChemicalStored(String tank, ChemicalStack stack) {
        this.internal.getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get())
                .flatMap(handler -> handler.getComponentForID(tank))
                .ifPresent(component -> component.setStack(stack));
    }

    public long getChemicalCapacity(String tank) {
        return this.internal.getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get())
                .flatMap(handler -> handler.getComponentForID(tank))
                .map(ChemicalMachineComponent::getCapacity)
                .orElse(0L);
    }

    public ChemicalStack addChemical(ChemicalStack stack, boolean simulate) {
        return this.internal.getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get())
                .map(handler -> ((ChemicalComponentHandler)handler).getGeneralHandler().insertChemical(stack, simulate ? Action.SIMULATE : Action.EXECUTE))
                .orElse(stack);
    }

    public ChemicalStack addChemicalToTank(String tank, ChemicalStack stack, boolean simulate) {
        return this.internal.getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get())
                .flatMap(handler -> handler.getComponentForID(tank))
                .map(component -> component.insert(stack, simulate ? Action.SIMULATE : Action.EXECUTE, true))
                .orElse(stack);
    }

    public ChemicalStack removeChemical(ChemicalStack stack, boolean simulate) {
        return this.internal.getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get())
                .map(handler -> ((ChemicalComponentHandler)handler).getGeneralHandler().extractChemical(stack, simulate ? Action.SIMULATE : Action.EXECUTE))
                .orElse(ChemicalStack.EMPTY);
    }

    public ChemicalStack removeChemicalFromTank(String tank, long amount, boolean simulate) {
        return this.internal.getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get())
                .flatMap(handler -> handler.getComponentForID(tank))
                .map(component -> component.extract(amount, simulate ? Action.SIMULATE : Action.EXECUTE, true))
                .orElse(ChemicalStack.EMPTY);
    }

    /** HEAT **/

    public double getHeat() {
        return this.internal.getComponentManager().getComponent(Registration.HEAT_MACHINE_COMPONENT.get())
                .flatMap(component -> Optional.ofNullable(component.getHeatCapacitor(0, null)))
                .map(IHeatCapacitor::getHeat)
                .orElse(0.0D);
    }

    public double getTemperature() {
        return this.internal.getComponentManager().getComponent(Registration.HEAT_MACHINE_COMPONENT.get())
                .flatMap(component -> Optional.ofNullable(component.getHeatCapacitor(0, null)))
                .map(IHeatCapacitor::getTemperature)
                .orElse(0.0D);
    }

    public void addHeat(double heat) {
        this.internal.getComponentManager().getComponent(Registration.HEAT_MACHINE_COMPONENT.get())
                .flatMap(component -> Optional.ofNullable(component.getHeatCapacitor(0, null)))
                .ifPresent(capacitor -> capacitor.handleHeat(heat));
    }

    public void setHeat(double heat) {
        this.internal.getComponentManager().getComponent(Registration.HEAT_MACHINE_COMPONENT.get())
                .flatMap(component -> Optional.ofNullable(component.getHeatCapacitor(0, null)))
                .ifPresent(capacitor -> capacitor.setHeat(heat));
    }

    /** RADIATION **/

    public double getRadiations() {
        return this.internal.getComponentManager().getComponent(Registration.RADIATION_MACHINE_COMPONENT.get())
                .map(RadiationMachineComponent::getRadiations)
                .orElse(0.0D);
    }

    public void addRadiations(double amount) {
        this.internal.getComponentManager().getComponent(Registration.RADIATION_MACHINE_COMPONENT.get())
                .ifPresent(component -> component.addRadiations(amount));
    }

    public void removeRadiations(double amount, int radius) {
        this.internal.getComponentManager().getComponent(Registration.RADIATION_MACHINE_COMPONENT.get())
                .ifPresent(component -> component.removeRadiations(amount, radius));
    }
}
