package fr.frinn.custommachinerymekanism.common.mixin;

import fr.frinn.custommachinery.common.init.CustomMachineTile;
import fr.frinn.custommachinery.common.integration.crafttweaker.function.MachineCT;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.component.ChemicalMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.RadiationMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.handler.ChemicalComponentHandler;
import mekanism.api.Action;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.heat.IHeatCapacitor;
import mekanism.common.integration.crafttweaker.chemical.CrTChemicalStack;
import mekanism.common.integration.crafttweaker.chemical.ICrTChemicalStack;
import org.openzen.zencode.java.ZenCodeType.Getter;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Setter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(value = MachineCT.class, remap = false)
public class MachineCTMixin {

    @Final
    @Shadow(remap = false)
    private CustomMachineTile internal;

    /** GAS **/

    @Method
    public ICrTChemicalStack getChemicalStored(String tank) {
        return this.internal.getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get())
                .flatMap(handler -> handler.getComponentForID(tank))
                .map(component -> new CrTChemicalStack(component.getStack().copy()))
                .orElse(new CrTChemicalStack(ChemicalStack.EMPTY));
    }

    @Method
    public void setChemicalStored(String tank, ICrTChemicalStack stack) {
        this.internal.getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get())
                .flatMap(handler -> handler.getComponentForID(tank))
                .ifPresent(component -> component.setStack(stack.getInternal()));
    }

    @Method
    public long getChemicalCapacity(String tank) {
        return this.internal.getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get())
                .flatMap(handler -> handler.getComponentForID(tank))
                .map(ChemicalMachineComponent::getCapacity)
                .orElse(0L);
    }

    @Method
    public ICrTChemicalStack addChemical(ICrTChemicalStack stack, boolean simulate) {
        return this.internal.getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get())
                .map(handler -> (ICrTChemicalStack)new CrTChemicalStack(((ChemicalComponentHandler)handler).getGeneralHandler().insertChemical(stack.getInternal(), simulate ? Action.SIMULATE : Action.EXECUTE)))
                .orElse(stack);
    }

    @Method
    public ICrTChemicalStack addChemicalToTank(String tank, ICrTChemicalStack stack, boolean simulate) {
        return this.internal.getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get())
                .flatMap(handler -> handler.getComponentForID(tank))
                .map(component -> (ICrTChemicalStack)new CrTChemicalStack(component.insert(stack.getInternal(), simulate ? Action.SIMULATE : Action.EXECUTE, true)))
                .orElse(stack);
    }

    @Method
    public ICrTChemicalStack removeChemical(ICrTChemicalStack stack, boolean simulate) {
        return this.internal.getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get())
                .map(handler -> new CrTChemicalStack(((ChemicalComponentHandler)handler).getGeneralHandler().extractChemical(stack.getInternal(), simulate ? Action.SIMULATE : Action.EXECUTE)))
                .orElse(new CrTChemicalStack(ChemicalStack.EMPTY));
    }

    @Method
    public ICrTChemicalStack removeChemicalFromTank(String tank, long amount, boolean simulate) {
        return this.internal.getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get())
                .flatMap(handler -> handler.getComponentForID(tank))
                .map(component -> new CrTChemicalStack(component.extract(amount, simulate ? Action.SIMULATE : Action.EXECUTE, true)))
                .orElse(new CrTChemicalStack(ChemicalStack.EMPTY));
    }

    /** HEAT **/

    @Method
    @Getter("heat")
    public double getHeat() {
        return this.internal.getComponentManager().getComponent(Registration.HEAT_MACHINE_COMPONENT.get())
                .flatMap(component -> Optional.ofNullable(component.getHeatCapacitor(0, null)))
                .map(IHeatCapacitor::getHeat)
                .orElse(0.0D);
    }

    @Method
    @Getter("temperature")
    public double getTemperature() {
        return this.internal.getComponentManager().getComponent(Registration.HEAT_MACHINE_COMPONENT.get())
                .flatMap(component -> Optional.ofNullable(component.getHeatCapacitor(0, null)))
                .map(IHeatCapacitor::getTemperature)
                .orElse(0.0D);
    }

    @Method
    public void addHeat(double heat) {
        this.internal.getComponentManager().getComponent(Registration.HEAT_MACHINE_COMPONENT.get())
                .flatMap(component -> Optional.ofNullable(component.getHeatCapacitor(0, null)))
                .ifPresent(capacitor -> capacitor.handleHeat(heat));
    }

    @Method
    @Setter("heat")
    public void setHeat(double heat) {
        this.internal.getComponentManager().getComponent(Registration.HEAT_MACHINE_COMPONENT.get())
                .flatMap(component -> Optional.ofNullable(component.getHeatCapacitor(0, null)))
                .ifPresent(capacitor -> capacitor.setHeat(heat));
    }

    /** RADIATION **/

    @Method
    @Getter("radiations")
    public double getRadiations() {
        return this.internal.getComponentManager().getComponent(Registration.RADIATION_MACHINE_COMPONENT.get())
                .map(RadiationMachineComponent::getRadiations)
                .orElse(0.0D);
    }

    @Method
    public void addRadiations(double amount) {
        this.internal.getComponentManager().getComponent(Registration.RADIATION_MACHINE_COMPONENT.get())
                .ifPresent(component -> component.addRadiations(amount));
    }

    @Method
    public void removeRadiations(double amount, int radius) {
        this.internal.getComponentManager().getComponent(Registration.RADIATION_MACHINE_COMPONENT.get())
                .ifPresent(component -> component.removeRadiations(amount, radius));
    }
}
