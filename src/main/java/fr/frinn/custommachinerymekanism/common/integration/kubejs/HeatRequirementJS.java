package fr.frinn.custommachinerymekanism.common.integration.kubejs;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.impl.util.DoubleRange;
import fr.frinn.custommachinerymekanism.common.requirement.HeatPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.HeatRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.TemperatureRequirement;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;

public interface HeatRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requireHeat(double amount) {
        return addRequirement(new HeatRequirement(RequirementIOMode.INPUT, amount));
    }

    default RecipeJSBuilder requireHeatPerTick(double amount) {
        return addRequirement(new HeatPerTickRequirement(RequirementIOMode.INPUT, amount));
    }

    default RecipeJSBuilder produceHeat(double amount) {
        return addRequirement(new HeatRequirement(RequirementIOMode.OUTPUT, amount));
    }

    default RecipeJSBuilder produceHeatPerTick(double amount) {
        return addRequirement(new HeatPerTickRequirement(RequirementIOMode.OUTPUT, amount));
    }

    default RecipeJSBuilder requireTemp(DoubleRange range, TemperatureUnit unit) {
        return addRequirement(new TemperatureRequirement(range, unit));
    }

    default RecipeJSBuilder requireTempKelvin(DoubleRange range) {
        return requireTemp(range, TemperatureUnit.KELVIN);
    }

    default RecipeJSBuilder requireTempCelsius(DoubleRange range) {
        return requireTemp(range, TemperatureUnit.CELSIUS);
    }

    default RecipeJSBuilder requireTempFahrenheit(DoubleRange range) {
        return requireTemp(range, TemperatureUnit.FAHRENHEIT);
    }

    default RecipeJSBuilder requireTempRankine(DoubleRange range) {
        return requireTemp(range, TemperatureUnit.RANKINE);
    }

    default RecipeJSBuilder requireTempAmbient(DoubleRange range) {
        return requireTemp(range, TemperatureUnit.AMBIENT);
    }
}
