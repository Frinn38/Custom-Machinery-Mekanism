package fr.frinn.custommachinerymekanism.common.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.crafting.IRequirementList;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientRequirement;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.requirement.IRequirement;
import fr.frinn.custommachinery.api.requirement.RecipeRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinery.impl.util.DoubleRange;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.client.jei.heat.Heat;
import fr.frinn.custommachinerymekanism.client.jei.wrapper.TemperatureIngredientWrapper;
import fr.frinn.custommachinerymekanism.common.component.HeatMachineComponent;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;

public record TemperatureRequirement(DoubleRange temp, TemperatureUnit unit) implements IRequirement<HeatMachineComponent>, IJEIIngredientRequirement<Heat> {

    public static final NamedCodec<TemperatureRequirement> CODEC = NamedCodec.record(temperatureRequirementInstance ->
            temperatureRequirementInstance.group(
                    DoubleRange.CODEC.fieldOf("temperature").forGetter(requirement -> requirement.temp),
                    NamedCodec.enumCodec(TemperatureUnit.class).optionalFieldOf("unit", TemperatureUnit.KELVIN).forGetter(requirement -> requirement.unit)
            ).apply(temperatureRequirementInstance, TemperatureRequirement::new), "Temperature requirement"
    );

    @Override
    public RequirementType<TemperatureRequirement> getType() {
        return Registration.TEMPERATURE_REQUIREMENT.get();
    }

    @Override
    public MachineComponentType<HeatMachineComponent> getComponentType() {
        return Registration.HEAT_MACHINE_COMPONENT.get();
    }

    @Override
    public RequirementIOMode getMode() {
        return RequirementIOMode.INPUT;
    }

    @Override
    public boolean test(HeatMachineComponent component, ICraftingContext context) {
        return this.temp.contains(this.unit.convertFromK(component.getTemperature(0), true));
    }

    @Override
    public void gatherRequirements(IRequirementList<HeatMachineComponent> list) {
        list.inventoryCondition(this::check);
    }

    private CraftingResult check(HeatMachineComponent component, ICraftingContext context) {
        if (test(component, context))
            return CraftingResult.success();
        return CraftingResult.error(Component.translatable("custommachinerymekanism.requirements.temp.error", this.temp.toFormattedString() + this.unit.getSymbol(false)));
    }

    @Override
    public List<IJEIIngredientWrapper<Heat>> getJEIIngredientWrappers(IMachineRecipe recipe, RecipeRequirement<?, ?> requirement) {
        return Collections.singletonList(new TemperatureIngredientWrapper(this.temp, this.unit));
    }
}
