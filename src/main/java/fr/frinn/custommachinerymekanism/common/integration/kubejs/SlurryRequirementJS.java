package fr.frinn.custommachinerymekanism.common.integration.kubejs;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinerymekanism.common.requirement.SlurryPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.SlurryRequirement;
import mekanism.api.chemical.slurry.SlurryStack;

public interface SlurryRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requireSlurry(SlurryStack stack) {
        return requireSlurry(stack, "");
    }

    default RecipeJSBuilder requireSlurry(SlurryStack stack, String tank) {
        return addRequirement(new SlurryRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    default RecipeJSBuilder requireSlurryPerTick(SlurryStack stack) {
        return requireSlurryPerTick(stack, "");
    }

    default RecipeJSBuilder requireSlurryPerTick(SlurryStack stack, String tank) {
        return addRequirement(new SlurryPerTickRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    default RecipeJSBuilder produceSlurry(SlurryStack stack) {
        return produceSlurry(stack, "");
    }

    default RecipeJSBuilder produceSlurry(SlurryStack stack, String tank) {
        return addRequirement(new SlurryRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    default RecipeJSBuilder produceSlurryPerTick(SlurryStack stack) {
        return produceSlurryPerTick(stack, "");
    }

    default RecipeJSBuilder produceSlurryPerTick(SlurryStack stack, String tank) {
        return addRequirement(new SlurryPerTickRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }
}
