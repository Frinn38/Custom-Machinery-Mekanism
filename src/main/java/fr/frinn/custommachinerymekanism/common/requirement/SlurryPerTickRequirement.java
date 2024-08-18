package fr.frinn.custommachinerymekanism.common.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.requirement.RecipeRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.client.jei.wrapper.SlurryIngredientWrapper;
import fr.frinn.custommachinerymekanism.common.component.handler.SlurryComponentHandler;
import fr.frinn.custommachinerymekanism.common.utils.Codecs;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;

import java.util.Collections;
import java.util.List;

public class SlurryPerTickRequirement extends ChemicalPerTickRequirement<Slurry, SlurryStack, SlurryComponentHandler> {

    public static final NamedCodec<SlurryPerTickRequirement> CODEC = makeCodec(Codecs.SLURRY, SlurryPerTickRequirement::new, "Slurry-per-tick requirement");

    public SlurryPerTickRequirement(RequirementIOMode mode, Slurry chemical, long amount, String tank) {
        super(mode, chemical, amount, tank);
    }

    @Override
    public RequirementType<SlurryPerTickRequirement> getType() {
        return Registration.SLURRY_PER_TICK_REQUIREMENT.get();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public MachineComponentType getComponentType() {
        return Registration.SLURRY_MACHINE_COMPONENT.get();
    }

    @Override
    public List<IJEIIngredientWrapper<SlurryStack>> getJEIIngredientWrappers(IMachineRecipe recipe, RecipeRequirement<?, ?> requirement) {
        return Collections.singletonList(new SlurryIngredientWrapper(this.getMode(), this.chemical, this.amount, requirement.chance(), true, this.tank));
    }
}
