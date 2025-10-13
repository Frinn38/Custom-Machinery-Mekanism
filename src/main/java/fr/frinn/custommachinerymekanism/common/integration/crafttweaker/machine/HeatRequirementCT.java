package fr.frinn.custommachinerymekanism.common.integration.crafttweaker.machine;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.integration.crafttweaker.CTConstants;
import fr.frinn.custommachinery.common.integration.crafttweaker.CustomMachineRecipeCTBuilder;
import fr.frinn.custommachinery.impl.util.DoubleRange;
import fr.frinn.custommachinerymekanism.common.requirement.HeatPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.HeatRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.TemperatureRequirement;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import org.openzen.zencode.java.ZenCodeType.Expansion;
import org.openzen.zencode.java.ZenCodeType.Method;

import java.util.Locale;

@ZenRegister
@Expansion(CTConstants.RECIPE_BUILDER_MACHINE)
public class HeatRequirementCT {

    @Method
    public static CustomMachineRecipeCTBuilder requireHeat(CustomMachineRecipeCTBuilder builder, double amount) {
        return builder.addRequirement(new HeatRequirement(RequirementIOMode.INPUT, amount));
    }

    @Method
    public static CustomMachineRecipeCTBuilder requireHeatPerTick(CustomMachineRecipeCTBuilder builder, double amount) {
        return builder.addRequirement(new HeatPerTickRequirement(RequirementIOMode.INPUT, amount));
    }

    @Method
    public static CustomMachineRecipeCTBuilder produceHeat(CustomMachineRecipeCTBuilder builder, double amount) {
        return builder.addRequirement(new HeatRequirement(RequirementIOMode.OUTPUT, amount));
    }

    @Method
    public static CustomMachineRecipeCTBuilder produceHeatPerTick(CustomMachineRecipeCTBuilder builder, double amount) {
        return builder.addRequirement(new HeatPerTickRequirement(RequirementIOMode.OUTPUT, amount));
    }

    @Method
    public static CustomMachineRecipeCTBuilder requireTemp(CustomMachineRecipeCTBuilder builder, String range, String unit) {
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
    public static CustomMachineRecipeCTBuilder requireTempCelsius(CustomMachineRecipeCTBuilder builder, String range) {
        return requireTemp(builder, range, TemperatureUnit.CELSIUS.name());
    }

    @Method
    public static CustomMachineRecipeCTBuilder requireTempFahrenheit(CustomMachineRecipeCTBuilder builder, String range) {
        return requireTemp(builder, range, TemperatureUnit.FAHRENHEIT.name());
    }

    @Method
    public static CustomMachineRecipeCTBuilder requireTempKelvin(CustomMachineRecipeCTBuilder builder, String range) {
        return requireTemp(builder, range, TemperatureUnit.KELVIN.name());
    }

    @Method
    public static CustomMachineRecipeCTBuilder requireTempRankine(CustomMachineRecipeCTBuilder builder, String range) {
        return requireTemp(builder, range, TemperatureUnit.RANKINE.name());
    }

    @Method
    public static CustomMachineRecipeCTBuilder requireTempAmbient(CustomMachineRecipeCTBuilder builder, String range) {
        return requireTemp(builder, range, TemperatureUnit.AMBIENT.name());
    }
}
