package fr.frinn.custommachinerymekanism.common.integration.crafttweaker.craft;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.integration.crafttweaker.CTConstants;
import fr.frinn.custommachinery.common.integration.crafttweaker.CustomCraftRecipeCTBuilder;
import fr.frinn.custommachinerymekanism.common.requirement.SlurryPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.SlurryRequirement;
import mekanism.common.integration.crafttweaker.chemical.ICrTChemicalStack.ICrTSlurryStack;
import org.openzen.zencode.java.ZenCodeType.Expansion;
import org.openzen.zencode.java.ZenCodeType.Method;

@ZenRegister
@Expansion(CTConstants.RECIPE_BUILDER_CRAFT)
public class SlurryRequirementCT {

    @Method
    public static CustomCraftRecipeCTBuilder requireSlurry(CustomCraftRecipeCTBuilder builder, ICrTSlurryStack stack) {
        return requireSlurry(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireSlurry(CustomCraftRecipeCTBuilder builder, ICrTSlurryStack stack, String tank) {
        return builder.addRequirement(new SlurryRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireSlurryPerTick(CustomCraftRecipeCTBuilder builder, ICrTSlurryStack stack) {
        return requireSlurryPerTick(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireSlurryPerTick(CustomCraftRecipeCTBuilder builder, ICrTSlurryStack stack, String tank) {
        return builder.addRequirement(new SlurryPerTickRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceSlurry(CustomCraftRecipeCTBuilder builder, ICrTSlurryStack stack) {
        return produceSlurry(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceSlurry(CustomCraftRecipeCTBuilder builder, ICrTSlurryStack stack, String tank) {
        return builder.addRequirement(new SlurryRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceSlurryPerTick(CustomCraftRecipeCTBuilder builder, ICrTSlurryStack stack) {
        return produceSlurryPerTick(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceSlurryPerTick(CustomCraftRecipeCTBuilder builder, ICrTSlurryStack stack, String tank) {
        return builder.addRequirement(new SlurryPerTickRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }
}
