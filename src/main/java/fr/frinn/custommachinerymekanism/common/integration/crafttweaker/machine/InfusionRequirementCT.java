package fr.frinn.custommachinerymekanism.common.integration.crafttweaker.machine;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.integration.crafttweaker.CTConstants;
import fr.frinn.custommachinery.common.integration.crafttweaker.CustomMachineRecipeCTBuilder;
import fr.frinn.custommachinerymekanism.common.requirement.InfusionPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.InfusionRequirement;
import mekanism.common.integration.crafttweaker.chemical.ICrTChemicalStack.ICrTInfusionStack;
import org.openzen.zencode.java.ZenCodeType.Expansion;
import org.openzen.zencode.java.ZenCodeType.Method;

@ZenRegister
@Expansion(CTConstants.RECIPE_BUILDER_MACHINE)
public class InfusionRequirementCT {

    @Method
    public static CustomMachineRecipeCTBuilder requireInfusion(CustomMachineRecipeCTBuilder builder, ICrTInfusionStack stack) {
        return requireInfusion(builder, stack, "");
    }

    @Method
    public static CustomMachineRecipeCTBuilder requireInfusion(CustomMachineRecipeCTBuilder builder, ICrTInfusionStack stack, String tank) {
        return builder.addRequirement(new InfusionRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomMachineRecipeCTBuilder requireInfusionPerTick(CustomMachineRecipeCTBuilder builder, ICrTInfusionStack stack) {
        return requireInfusionPerTick(builder, stack, "");
    }

    @Method
    public static CustomMachineRecipeCTBuilder requireInfusionPerTick(CustomMachineRecipeCTBuilder builder, ICrTInfusionStack stack, String tank) {
        return builder.addRequirement(new InfusionPerTickRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomMachineRecipeCTBuilder produceInfusion(CustomMachineRecipeCTBuilder builder, ICrTInfusionStack stack) {
        return produceInfusion(builder, stack, "");
    }

    @Method
    public static CustomMachineRecipeCTBuilder produceInfusion(CustomMachineRecipeCTBuilder builder, ICrTInfusionStack stack, String tank) {
        return builder.addRequirement(new InfusionRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomMachineRecipeCTBuilder produceInfusionPerTick(CustomMachineRecipeCTBuilder builder, ICrTInfusionStack stack) {
        return produceInfusionPerTick(builder, stack, "");
    }

    @Method
    public static CustomMachineRecipeCTBuilder produceInfusionPerTick(CustomMachineRecipeCTBuilder builder, ICrTInfusionStack stack, String tank) {
        return builder.addRequirement(new InfusionPerTickRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }
}
