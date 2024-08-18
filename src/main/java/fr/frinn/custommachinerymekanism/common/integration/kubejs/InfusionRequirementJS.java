package fr.frinn.custommachinerymekanism.common.integration.kubejs;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinerymekanism.common.requirement.InfusionPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.InfusionRequirement;
import mekanism.api.chemical.infuse.InfusionStack;

public interface InfusionRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requireInfusion(InfusionStack stack) {
        return requireInfusion(stack, "");
    }

    default RecipeJSBuilder requireInfusion(InfusionStack stack, String tank) {
        return addRequirement(new InfusionRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    default RecipeJSBuilder requireInfusionPerTick(InfusionStack stack) {
        return requireInfusionPerTick(stack, "");
    }

    default RecipeJSBuilder requireInfusionPerTick(InfusionStack stack, String tank) {
        return addRequirement(new InfusionPerTickRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    default RecipeJSBuilder produceInfusion(InfusionStack stack) {
        return produceInfusion(stack, "");
    }

    default RecipeJSBuilder produceInfusion(InfusionStack stack, String tank) {
        return addRequirement(new InfusionRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    default RecipeJSBuilder produceInfusionPerTick(InfusionStack stack) {
        return produceInfusionPerTick(stack, "");
    }

    default RecipeJSBuilder produceInfusionPerTick(InfusionStack stack, String tank) {
        return addRequirement(new InfusionPerTickRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }
}
