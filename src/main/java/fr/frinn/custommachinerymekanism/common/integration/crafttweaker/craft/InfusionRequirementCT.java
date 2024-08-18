package fr.frinn.custommachinerymekanism.common.integration.crafttweaker.craft;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.integration.crafttweaker.CTConstants;
import fr.frinn.custommachinery.common.integration.crafttweaker.CustomCraftRecipeCTBuilder;
import fr.frinn.custommachinerymekanism.common.requirement.InfusionPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.InfusionRequirement;
import mekanism.common.integration.crafttweaker.chemical.ICrTChemicalStack.ICrTInfusionStack;
import org.openzen.zencode.java.ZenCodeType.Expansion;
import org.openzen.zencode.java.ZenCodeType.Method;

@ZenRegister
@Expansion(CTConstants.RECIPE_BUILDER_CRAFT)
public class InfusionRequirementCT {

    @Method
    public static CustomCraftRecipeCTBuilder requireInfusion(CustomCraftRecipeCTBuilder builder, ICrTInfusionStack stack) {
        return requireInfusion(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireInfusion(CustomCraftRecipeCTBuilder builder, ICrTInfusionStack stack, String tank) {
        return builder.addRequirement(new InfusionRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireInfusionPerTick(CustomCraftRecipeCTBuilder builder, ICrTInfusionStack stack) {
        return requireInfusionPerTick(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireInfusionPerTick(CustomCraftRecipeCTBuilder builder, ICrTInfusionStack stack, String tank) {
        return builder.addRequirement(new InfusionPerTickRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceInfusion(CustomCraftRecipeCTBuilder builder, ICrTInfusionStack stack) {
        return produceInfusion(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceInfusion(CustomCraftRecipeCTBuilder builder, ICrTInfusionStack stack, String tank) {
        return builder.addRequirement(new InfusionRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceInfusionPerTick(CustomCraftRecipeCTBuilder builder, ICrTInfusionStack stack) {
        return produceInfusionPerTick(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceInfusionPerTick(CustomCraftRecipeCTBuilder builder, ICrTInfusionStack stack, String tank) {
        return builder.addRequirement(new InfusionPerTickRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }
}
