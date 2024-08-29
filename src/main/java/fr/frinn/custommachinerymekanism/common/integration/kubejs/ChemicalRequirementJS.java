package fr.frinn.custommachinerymekanism.common.integration.kubejs;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinerymekanism.common.requirement.ChemicalPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.ChemicalRequirement;
import mekanism.api.chemical.ChemicalStack;

public interface ChemicalRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requireChemical(ChemicalStack stack) {
        return requireChemical(stack, "");
    }

    default RecipeJSBuilder requireChemical(ChemicalStack stack, String tank) {
        return addRequirement(new ChemicalRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    default RecipeJSBuilder requireChemicalPerTick(ChemicalStack stack) {
        return requireChemicalPerTick(stack, "");
    }

    default RecipeJSBuilder requireChemicalPerTick(ChemicalStack stack, String tank) {
        return addRequirement(new ChemicalPerTickRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    default RecipeJSBuilder produceChemical(ChemicalStack stack) {
        return produceChemical(stack, "");
    }

    default RecipeJSBuilder produceChemical(ChemicalStack stack, String tank) {
        return addRequirement(new ChemicalRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    default RecipeJSBuilder produceChemicalPerTick(ChemicalStack stack) {
        return produceChemicalPerTick(stack, "");
    }

    default RecipeJSBuilder produceChemicalPerTick(ChemicalStack stack, String tank) {
        return addRequirement(new ChemicalPerTickRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }
}
