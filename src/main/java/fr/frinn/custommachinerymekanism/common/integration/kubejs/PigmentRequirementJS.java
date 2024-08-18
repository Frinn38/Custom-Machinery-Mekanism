package fr.frinn.custommachinerymekanism.common.integration.kubejs;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinerymekanism.common.requirement.PigmentPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.PigmentRequirement;
import mekanism.api.chemical.pigment.PigmentStack;

public interface PigmentRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requirePigment(PigmentStack stack) {
        return requirePigment(stack, "");
    }

    default RecipeJSBuilder requirePigment(PigmentStack stack, String tank) {
        return addRequirement(new PigmentRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    default RecipeJSBuilder requirePigmentPerTick(PigmentStack stack) {
        return requirePigmentPerTick(stack, "");
    }

    default RecipeJSBuilder requirePigmentPerTick(PigmentStack stack, String tank) {
        return addRequirement(new PigmentPerTickRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    default RecipeJSBuilder producePigment(PigmentStack stack) {
        return producePigment(stack, "");
    }

    default RecipeJSBuilder producePigment(PigmentStack stack, String tank) {
        return addRequirement(new PigmentRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    default RecipeJSBuilder producePigmentPerTick(PigmentStack stack) {
        return producePigmentPerTick(stack, "");
    }

    default RecipeJSBuilder producePigmentPerTick(PigmentStack stack, String tank) {
        return addRequirement(new PigmentPerTickRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }
}
