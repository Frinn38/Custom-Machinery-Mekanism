package fr.frinn.custommachinerymekanism.client.jei.wrapper;

import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.client.recipe_viewer.jei.MekanismJEI;

public class PigmentIngredientWrapper extends ChemicalIngredientWrapper<Pigment, PigmentStack> {

    public PigmentIngredientWrapper(RequirementIOMode mode, Pigment chemical, long amount, double chance, boolean isPerTick, String tank) {
        super(mode, chemical, amount, chance, isPerTick, tank, MekanismJEI.TYPE_PIGMENT, PigmentStack::new);
    }
}
