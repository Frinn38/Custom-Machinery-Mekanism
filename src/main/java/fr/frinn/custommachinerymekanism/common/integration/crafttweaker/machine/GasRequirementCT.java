package fr.frinn.custommachinerymekanism.common.integration.crafttweaker.machine;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.integration.crafttweaker.CTConstants;
import fr.frinn.custommachinery.common.integration.crafttweaker.CustomMachineRecipeCTBuilder;
import fr.frinn.custommachinerymekanism.common.requirement.GasPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.GasRequirement;
import mekanism.common.integration.crafttweaker.chemical.ICrTChemicalStack.ICrTGasStack;
import org.openzen.zencode.java.ZenCodeType.Expansion;
import org.openzen.zencode.java.ZenCodeType.Method;

@ZenRegister
@Expansion(CTConstants.RECIPE_BUILDER_MACHINE)
public class GasRequirementCT {

    @Method
    public static CustomMachineRecipeCTBuilder requireGas(CustomMachineRecipeCTBuilder builder, ICrTGasStack stack) {
        return requireGas(builder, stack, "");
    }

    @Method
    public static CustomMachineRecipeCTBuilder requireGas(CustomMachineRecipeCTBuilder builder, ICrTGasStack stack, String tank) {
        return builder.addRequirement(new GasRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomMachineRecipeCTBuilder requireGasPerTick(CustomMachineRecipeCTBuilder builder, ICrTGasStack stack) {
        return requireGasPerTick(builder, stack, "");
    }

    @Method
    public static CustomMachineRecipeCTBuilder requireGasPerTick(CustomMachineRecipeCTBuilder builder, ICrTGasStack stack, String tank) {
        return builder.addRequirement(new GasPerTickRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomMachineRecipeCTBuilder produceGas(CustomMachineRecipeCTBuilder builder, ICrTGasStack stack) {
        return produceGas(builder, stack, "");
    }

    @Method
    public static CustomMachineRecipeCTBuilder produceGas(CustomMachineRecipeCTBuilder builder, ICrTGasStack stack, String tank) {
        return builder.addRequirement(new GasRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomMachineRecipeCTBuilder produceGasPerTick(CustomMachineRecipeCTBuilder builder, ICrTGasStack stack) {
        return produceGasPerTick(builder, stack, "");
    }

    @Method
    public static CustomMachineRecipeCTBuilder produceGasPerTick(CustomMachineRecipeCTBuilder builder, ICrTGasStack stack, String tank) {
        return builder.addRequirement(new GasPerTickRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }
}
