package fr.frinn.custommachinerymekanism.common.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.requirement.RecipeRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.client.jei.wrapper.InfusionIngredientWrapper;
import fr.frinn.custommachinerymekanism.common.component.handler.InfusionComponentHandler;
import fr.frinn.custommachinerymekanism.common.utils.Codecs;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;

import java.util.Collections;
import java.util.List;

public class InfusionRequirement extends ChemicalRequirement<InfuseType, InfusionStack, InfusionComponentHandler> {

    public static final NamedCodec<InfusionRequirement> CODEC = makeCodec(Codecs.INFUSE_TYPE, InfusionRequirement::new, "Infusion requirement");

    public InfusionRequirement(RequirementIOMode mode, InfuseType chemical, long amount, String tank) {
        super(mode, chemical, amount, tank);
    }

    @Override
    public RequirementType<InfusionRequirement> getType() {
        return Registration.INFUSION_REQUIREMENT.get();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public MachineComponentType getComponentType() {
        return Registration.INFUSION_MACHINE_COMPONENT.get();
    }

    @Override
    public List<IJEIIngredientWrapper<InfusionStack>> getJEIIngredientWrappers(IMachineRecipe recipe, RecipeRequirement<?, ?> requirement) {
        return Collections.singletonList(new InfusionIngredientWrapper(this.getMode(), this.chemical, this.amount, requirement.chance(), false, this.tank));
    }
}
