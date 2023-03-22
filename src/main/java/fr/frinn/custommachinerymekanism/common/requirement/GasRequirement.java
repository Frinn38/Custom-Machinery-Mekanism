package fr.frinn.custommachinerymekanism.common.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
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

public class GasRequirement extends ChemicalRequirement<Gas, GasStack, GasComponentHandler> {

    public static final NamedCodec<GasRequirement> CODEC = makeCodec(Codecs.GAS, GasRequirement::new, "Gas requirement");

    public GasRequirement(RequirementIOMode mode, Gas chemical, long amount, String tank) {
        super(mode, chemical, amount, tank);
    }

    @Override
    public RequirementType<GasRequirement> getType() {
        return Registration.GAS_REQUIREMENT.get();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public MachineComponentType getComponentType() {
        return Registration.GAS_MACHINE_COMPONENT.get();
    }

    @Override
    public List<IJEIIngredientWrapper<GasStack>> getJEIIngredientWrappers(IMachineRecipe recipe) {
        return Collections.singletonList(new GasIngredientWrapper(this.getMode(), this.chemical, this.amount, this.getChance(), false, this.tank));
    }
}
