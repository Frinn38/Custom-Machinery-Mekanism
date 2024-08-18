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
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.client.jei.heat.Heat;
import fr.frinn.custommachinerymekanism.client.jei.wrapper.HeatIngredientWrapper;
import fr.frinn.custommachinerymekanism.common.component.HeatMachineComponent;
import mekanism.api.heat.IHeatCapacitor;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;

public class HeatRequirement implements IRequirement<HeatMachineComponent>, IJEIIngredientRequirement<Heat> {

    public static final NamedCodec<HeatRequirement> CODEC = NamedCodec.record(heatRequirementInstance ->
            heatRequirementInstance.group(
                    RequirementIOMode.CODEC.fieldOf("mode").forGetter(requirement -> requirement.mode),
                    NamedCodec.doubleRange(0.0D, Double.MAX_VALUE).fieldOf("amount").forGetter(requirement -> requirement.amount)
            ).apply(heatRequirementInstance, HeatRequirement::new), "Heat requirement"
    );

    private final RequirementIOMode mode;
    private final double amount;

    public HeatRequirement(RequirementIOMode mode, double amount) {
        this.mode = mode;
        this.amount = amount;
    }

    @Override
    public RequirementType<HeatRequirement> getType() {
        return Registration.HEAT_REQUIREMENT.get();
    }

    @Override
    public MachineComponentType<HeatMachineComponent> getComponentType() {
        return Registration.HEAT_MACHINE_COMPONENT.get();
    }

    @Override
    public RequirementIOMode getMode() {
        return this.mode;
    }

    @Override
    public boolean test(HeatMachineComponent component, ICraftingContext context) {
        double amount = context.getModifiedValue(this.amount, this, null);
        if(getMode() == RequirementIOMode.INPUT)
            return component.getHeatCapacitors(null).getFirst().getHeat() >= amount;
        return true;
    }

    @Override
    public void gatherRequirements(IRequirementList<HeatMachineComponent> list) {
        if(this.mode == RequirementIOMode.INPUT)
            list.processDelayed(0, this::processInput);
        else
            list.processDelayed(1, this::processOutput);
    }

    private CraftingResult processInput(HeatMachineComponent component, ICraftingContext context) {
        double amount = context.getModifiedValue(this.amount, this, null);
        IHeatCapacitor capacitor = component.getHeatCapacitors(null).get(0);
        if(capacitor.getHeat() < amount)
            return CraftingResult.error(Component.translatable("custommachinerymekanism.requirements.heat.error.input", amount, capacitor.getHeat()));
        capacitor.handleHeat(-amount);
        return CraftingResult.success();
    }

    private CraftingResult processOutput(HeatMachineComponent component, ICraftingContext context) {
        double amount = context.getModifiedValue(this.amount, this, null);
        component.getHeatCapacitors(null).getFirst().handleHeat(amount);
        return CraftingResult.success();
    }

    @Override
    public List<IJEIIngredientWrapper<Heat>> getJEIIngredientWrappers(IMachineRecipe recipe, RecipeRequirement<?, ?> requirement) {
        return Collections.singletonList(new HeatIngredientWrapper(this.getMode(), this.amount, requirement.chance(), false));
    }
}
