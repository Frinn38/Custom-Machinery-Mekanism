package fr.frinn.custommachinerymekanism.common.integration.crafttweaker.craft;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.integration.crafttweaker.CTConstants;
import fr.frinn.custommachinery.common.integration.crafttweaker.CustomCraftRecipeCTBuilder;
import fr.frinn.custommachinerymekanism.common.requirement.GasPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.GasRequirement;
import mekanism.common.integration.crafttweaker.chemical.ICrTChemicalStack.ICrTGasStack;
import org.openzen.zencode.java.ZenCodeType.Expansion;
import org.openzen.zencode.java.ZenCodeType.Method;

@ZenRegister
@Expansion(CTConstants.RECIPE_BUILDER_CRAFT)
public class GasRequirementCT {

    @Method
    public static CustomCraftRecipeCTBuilder requireGas(CustomCraftRecipeCTBuilder builder, ICrTGasStack stack) {
        return requireGas(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireGas(CustomCraftRecipeCTBuilder builder, ICrTGasStack stack, String tank) {
        return builder.addRequirement(new GasRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireGasPerTick(CustomCraftRecipeCTBuilder builder, ICrTGasStack stack) {
        return requireGasPerTick(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireGasPerTick(CustomCraftRecipeCTBuilder builder, ICrTGasStack stack, String tank) {
        return builder.addRequirement(new GasPerTickRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceGas(CustomCraftRecipeCTBuilder builder, ICrTGasStack stack) {
        return produceGas(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceGas(CustomCraftRecipeCTBuilder builder, ICrTGasStack stack, String tank) {
        return builder.addRequirement(new GasRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceGasPerTick(CustomCraftRecipeCTBuilder builder, ICrTGasStack stack) {
        return produceGasPerTick(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceGasPerTick(CustomCraftRecipeCTBuilder builder, ICrTGasStack stack, String tank) {
        return builder.addRequirement(new GasPerTickRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }
}
