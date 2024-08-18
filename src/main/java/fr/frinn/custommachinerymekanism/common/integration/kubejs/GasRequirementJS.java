package fr.frinn.custommachinerymekanism.common.integration.kubejs;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinerymekanism.common.requirement.GasPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.GasRequirement;
import mekanism.api.chemical.gas.GasStack;

public interface GasRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requireGas(GasStack stack) {
        return requireGas(stack, "");
    }

    default RecipeJSBuilder requireGas(GasStack stack, String tank) {
        return addRequirement(new GasRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    default RecipeJSBuilder requireGasPerTick(GasStack stack) {
        return requireGasPerTick(stack, "");
    }

    default RecipeJSBuilder requireGasPerTick(GasStack stack, String tank) {
        return addRequirement(new GasPerTickRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    default RecipeJSBuilder produceGas(GasStack stack) {
        return produceGas(stack, "");
    }

    default RecipeJSBuilder produceGas(GasStack stack, String tank) {
        return addRequirement(new GasRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    default RecipeJSBuilder produceGasPerTick(GasStack stack) {
        return produceGasPerTick(stack, "");
    }

    default RecipeJSBuilder produceGasPerTick(GasStack stack, String tank) {
        return addRequirement(new GasPerTickRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }
}
