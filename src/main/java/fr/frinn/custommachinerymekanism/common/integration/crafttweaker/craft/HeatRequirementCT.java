package fr.frinn.custommachinerymekanism.common.integration.crafttweaker.craft;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.integration.crafttweaker.CTConstants;
import fr.frinn.custommachinery.common.integration.crafttweaker.CustomCraftRecipeCTBuilder;
import fr.frinn.custommachinery.impl.util.DoubleRange;
import fr.frinn.custommachinerymekanism.common.requirement.HeatPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.HeatRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.TemperatureRequirement;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import org.openzen.zencode.java.ZenCodeType.Expansion;
import org.openzen.zencode.java.ZenCodeType.Method;

import java.util.Locale;

@ZenRegister
@Expansion(CTConstants.RECIPE_BUILDER_CRAFT)
public class HeatRequirementCT {

    @Method
    public static CustomCraftRecipeCTBuilder requireHeat(CustomCraftRecipeCTBuilder builder, double amount) {
        return builder.addRequirement(new HeatRequirement(RequirementIOMode.INPUT, amount));
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireHeatPerTick(CustomCraftRecipeCTBuilder builder, double amount) {
        return builder.addRequirement(new HeatPerTickRequirement(RequirementIOMode.INPUT, amount));
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceHeat(CustomCraftRecipeCTBuilder builder, double amount) {
        return builder.addRequirement(new HeatRequirement(RequirementIOMode.OUTPUT, amount));
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceHeatPerTick(CustomCraftRecipeCTBuilder builder, double amount) {
        return builder.addRequirement(new HeatPerTickRequirement(RequirementIOMode.OUTPUT, amount));
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireTemp(CustomCraftRecipeCTBuilder builder, String range, String unit) {
        try {
            DoubleRange temp = DoubleRange.createFromString(range);
            try {
                TemperatureUnit temperatureUnit = TemperatureUnit.valueOf(unit.toUpperCase(Locale.ROOT));
                return builder.addRequirement(new TemperatureRequirement(temp, temperatureUnit));
            } catch (IllegalArgumentException e) {
                return builder.error("Invalid temperature unit: {}\n{}", unit, e.getMessage());
            }
        } catch (IllegalArgumentException e) {
            return builder.error("Invalid temperature range: {}\n{}", range, e.getMessage());
        }
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireTempCelsius(CustomCraftRecipeCTBuilder builder, String range) {
        return requireTemp(builder, range, TemperatureUnit.CELSIUS.name());
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireTempFahrenheit(CustomCraftRecipeCTBuilder builder, String range) {
        return requireTemp(builder, range, TemperatureUnit.FAHRENHEIT.name());
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireTempKelvin(CustomCraftRecipeCTBuilder builder, String range) {
        return requireTemp(builder, range, TemperatureUnit.KELVIN.name());
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireTempRankine(CustomCraftRecipeCTBuilder builder, String range) {
        return requireTemp(builder, range, TemperatureUnit.RANKINE.name());
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireTempAmbient(CustomCraftRecipeCTBuilder builder, String range) {
        return requireTemp(builder, range, TemperatureUnit.AMBIENT.name());
    }
}
