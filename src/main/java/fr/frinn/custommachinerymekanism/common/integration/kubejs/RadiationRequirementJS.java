package fr.frinn.custommachinerymekanism.common.integration.kubejs;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinerymekanism.common.requirement.RadiationPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.RadiationRequirement;

public interface RadiationRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requireRadiation(double amount) {
        return requireRadiation(amount, 80);
    }

    default RecipeJSBuilder requireRadiation(double amount, int radius) {
        return addRequirement(new RadiationRequirement(RequirementIOMode.INPUT, amount, radius));
    }

    default RecipeJSBuilder requireRadiationPerTick(double amount) {
        return requireRadiationPerTick(amount, 80);
    }

    default RecipeJSBuilder requireRadiationPerTick(double amount, int radius) {
        return addRequirement(new RadiationPerTickRequirement(RequirementIOMode.INPUT, amount, radius));
    }

    default RecipeJSBuilder emitRadiation(double amount) {
        return addRequirement(new RadiationRequirement(RequirementIOMode.OUTPUT, amount, 1));
    }

    default RecipeJSBuilder emitRadiationPerTick(double amount) {
        return addRequirement(new RadiationPerTickRequirement(RequirementIOMode.OUTPUT, amount, 1));
    }
}
