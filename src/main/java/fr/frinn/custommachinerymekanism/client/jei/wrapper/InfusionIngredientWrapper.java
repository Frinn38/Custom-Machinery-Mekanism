package fr.frinn.custommachinerymekanism.client.jei.wrapper;

import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.client.recipe_viewer.jei.MekanismJEI;

public class InfusionIngredientWrapper extends ChemicalIngredientWrapper<InfuseType, InfusionStack> {

    public InfusionIngredientWrapper(RequirementIOMode mode, InfuseType chemical, long amount, double chance, boolean isPerTick, String tank) {
        super(mode, chemical, amount, chance, isPerTick, tank, MekanismJEI.TYPE_INFUSION, InfusionStack::new);
    }
}
