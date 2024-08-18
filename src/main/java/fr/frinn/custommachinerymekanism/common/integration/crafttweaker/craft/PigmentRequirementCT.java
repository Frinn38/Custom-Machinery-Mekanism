package fr.frinn.custommachinerymekanism.common.integration.crafttweaker.craft;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.integration.crafttweaker.CTConstants;
import fr.frinn.custommachinery.common.integration.crafttweaker.CustomCraftRecipeCTBuilder;
import fr.frinn.custommachinerymekanism.common.requirement.PigmentPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.PigmentRequirement;
import mekanism.common.integration.crafttweaker.chemical.ICrTChemicalStack.ICrTPigmentStack;
import org.openzen.zencode.java.ZenCodeType.Expansion;
import org.openzen.zencode.java.ZenCodeType.Method;

@ZenRegister
@Expansion(CTConstants.RECIPE_BUILDER_CRAFT)
public class PigmentRequirementCT {

    @Method
    public static CustomCraftRecipeCTBuilder requirePigment(CustomCraftRecipeCTBuilder builder, ICrTPigmentStack stack) {
        return requirePigment(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder requirePigment(CustomCraftRecipeCTBuilder builder, ICrTPigmentStack stack, String tank) {
        return builder.addRequirement(new PigmentRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomCraftRecipeCTBuilder requirePigmentPerTick(CustomCraftRecipeCTBuilder builder, ICrTPigmentStack stack) {
        return requirePigmentPerTick(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder requirePigmentPerTick(CustomCraftRecipeCTBuilder builder, ICrTPigmentStack stack, String tank) {
        return builder.addRequirement(new PigmentPerTickRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomCraftRecipeCTBuilder producePigment(CustomCraftRecipeCTBuilder builder, ICrTPigmentStack stack) {
        return producePigment(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder producePigment(CustomCraftRecipeCTBuilder builder, ICrTPigmentStack stack, String tank) {
        return builder.addRequirement(new PigmentRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomCraftRecipeCTBuilder producePigmentPerTick(CustomCraftRecipeCTBuilder builder, ICrTPigmentStack stack) {
        return producePigmentPerTick(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder producePigmentPerTick(CustomCraftRecipeCTBuilder builder, ICrTPigmentStack stack, String tank) {
        return builder.addRequirement(new PigmentPerTickRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }
}
