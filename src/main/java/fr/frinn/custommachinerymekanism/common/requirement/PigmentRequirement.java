package fr.frinn.custommachinerymekanism.common.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.requirement.RecipeRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.client.jei.wrapper.PigmentIngredientWrapper;
import fr.frinn.custommachinerymekanism.common.component.handler.PigmentComponentHandler;
import fr.frinn.custommachinerymekanism.common.utils.Codecs;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;

import java.util.Collections;
import java.util.List;

public class PigmentRequirement extends ChemicalRequirement<Pigment, PigmentStack, PigmentComponentHandler> {

    public static final NamedCodec<PigmentRequirement> CODEC = makeCodec(Codecs.PIGMENT, PigmentRequirement::new, "Pigment requirement");

    public PigmentRequirement(RequirementIOMode mode, Pigment chemical, long amount, String tank) {
        super(mode, chemical, amount, tank);
    }

    @Override
    public RequirementType<PigmentRequirement> getType() {
        return Registration.PIGMENT_REQUIREMENT.get();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public MachineComponentType getComponentType() {
        return Registration.PIGMENT_MACHINE_COMPONENT.get();
    }

    @Override
    public List<IJEIIngredientWrapper<PigmentStack>> getJEIIngredientWrappers(IMachineRecipe recipe, RecipeRequirement<?, ?> requirement) {
        return Collections.singletonList(new PigmentIngredientWrapper(this.getMode(), this.chemical, this.amount, requirement.chance(), false, this.tank));
    }
}
