package fr.frinn.custommachinerymekanism.client.jei.wrapper;

import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.client.recipe_viewer.jei.MekanismJEI;

public class GasIngredientWrapper extends ChemicalIngredientWrapper<Gas, GasStack> {


    public GasIngredientWrapper(RequirementIOMode mode, Gas chemical, long amount, double chance, boolean isPerTick, String tank) {
        super(mode, chemical, amount, chance, isPerTick, tank, MekanismJEI.TYPE_GAS, GasStack::new);
    }
}
