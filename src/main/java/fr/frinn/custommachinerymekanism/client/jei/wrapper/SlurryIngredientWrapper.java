package fr.frinn.custommachinerymekanism.client.jei.wrapper;

import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.client.recipe_viewer.jei.MekanismJEI;

public class SlurryIngredientWrapper extends ChemicalIngredientWrapper<Slurry, SlurryStack> {

    public SlurryIngredientWrapper(RequirementIOMode mode, Slurry chemical, long amount, double chance, boolean isPerTick, String tank) {
        super(mode, chemical, amount, chance, isPerTick, tank, MekanismJEI.TYPE_SLURRY, SlurryStack::new);
    }
}
