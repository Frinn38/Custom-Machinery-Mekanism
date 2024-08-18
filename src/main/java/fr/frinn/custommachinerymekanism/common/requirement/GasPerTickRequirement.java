package fr.frinn.custommachinerymekanism.common.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.requirement.RecipeRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.client.jei.wrapper.GasIngredientWrapper;
import fr.frinn.custommachinerymekanism.common.component.handler.GasComponentHandler;
import fr.frinn.custommachinerymekanism.common.utils.Codecs;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;

import java.util.Collections;
import java.util.List;

public class GasPerTickRequirement extends ChemicalPerTickRequirement<Gas, GasStack, GasComponentHandler> {

    public static final NamedCodec<GasPerTickRequirement> CODEC = makeCodec(Codecs.GAS, GasPerTickRequirement::new, "Gas-per-tick requirement");

    public GasPerTickRequirement(RequirementIOMode mode, Gas chemical, long amount, String tank) {
        super(mode, chemical, amount, tank);
    }

    @Override
    public RequirementType<GasPerTickRequirement> getType() {
        return Registration.GAS_PER_TICK_REQUIREMENT.get();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public MachineComponentType getComponentType() {
        return Registration.GAS_MACHINE_COMPONENT.get();
    }

    @Override
    public List<IJEIIngredientWrapper<GasStack>> getJEIIngredientWrappers(IMachineRecipe recipe, RecipeRequirement<?, ?> requirement) {
        return Collections.singletonList(new GasIngredientWrapper(this.getMode(), this.chemical, this.amount, requirement.chance(), true, this.tank));
    }
}
