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
import fr.frinn.custommachinerymekanism.client.jei.wrapper.ChemicalIngredientWrapper;
import fr.frinn.custommachinerymekanism.common.component.handler.ChemicalComponentHandler;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;

public record ChemicalRequirement(RequirementIOMode mode, Chemical chemical, long amount, String tank) implements IRequirement<ChemicalComponentHandler>, IJEIIngredientRequirement<ChemicalStack> {

    public static NamedCodec<ChemicalRequirement> CODEC = NamedCodec.record(chemicalRequirementInstance ->
            chemicalRequirementInstance.group(
                    RequirementIOMode.CODEC.fieldOf("mode").forGetter(requirement -> requirement.mode),
                    NamedCodec.registrar(MekanismAPI.CHEMICAL_REGISTRY).fieldOf("chemical").forGetter(requirement -> requirement.chemical),
                    NamedCodec.LONG.fieldOf("amount").forGetter(requirement -> requirement.amount),
                    NamedCodec.STRING.optionalFieldOf("tank", "").forGetter(requirement -> requirement.tank)
            ).apply(chemicalRequirementInstance, ChemicalRequirement::new), "Chemical requirement"
    );

    @Override
    public RequirementType<ChemicalRequirement> getType() {
        return Registration.CHEMICAL_REQUIREMENT.get();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public MachineComponentType getComponentType() {
        return Registration.CHEMICAL_MACHINE_COMPONENT.get();
    }

    @Override
    public boolean test(ChemicalComponentHandler handler, ICraftingContext context) {
        long amount = (long)context.getModifiedValue(this.amount, this, null);
        if(this.getMode() == RequirementIOMode.INPUT)
            return handler.getChemicalAmount(this.tank, this.chemical) >= amount;
        else
            return handler.getSpaceForChemical(this.tank, this.chemical) >= amount;
    }

    @Override
    public RequirementIOMode getMode() {
        return this.mode;
    }

    @Override
    public void gatherRequirements(IRequirementList<ChemicalComponentHandler> list) {
        if(this.mode == RequirementIOMode.INPUT)
            list.processOnStart(this::processInput);
        else
            list.processOnEnd(this::processOutput);
    }

    private CraftingResult processInput(ChemicalComponentHandler handler, ICraftingContext context) {
        long amount = (long)context.getModifiedValue(this.amount, this, null);
        if(!test(handler, context))
            return CraftingResult.error(Component.translatable("custommachinerymekanism.requirements.chemical.error.input", Component.translatable(this.chemical.getTranslationKey()), amount));

        handler.removeFromInputs(this.tank, this.chemical, amount);
        return CraftingResult.success();
    }

    private CraftingResult processOutput(ChemicalComponentHandler handler, ICraftingContext context) {
        long amount = (long)context.getModifiedValue(this.amount, this, null);
        if(!test(handler, context))
            return CraftingResult.error(Component.translatable("custommachinerymekanism.requirements.chemical.error.output", amount, Component.translatable(this.chemical.getTranslationKey())));

        handler.addToOutputs(this.tank, this.chemical, amount);
        return CraftingResult.success();
    }

    @Override
    public List<IJEIIngredientWrapper<ChemicalStack>> getJEIIngredientWrappers(IMachineRecipe recipe, RecipeRequirement<?, ?> requirement) {
        return Collections.singletonList(new ChemicalIngredientWrapper(this.mode, this.chemical, this.amount, requirement.chance(), false, this.tank));
    }
}
