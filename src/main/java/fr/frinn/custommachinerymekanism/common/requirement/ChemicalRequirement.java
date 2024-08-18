package fr.frinn.custommachinerymekanism.common.requirement;

import com.mojang.datafixers.util.Function4;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.crafting.IRequirementList;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientRequirement;
import fr.frinn.custommachinery.api.requirement.IRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinerymekanism.common.component.handler.ChemicalComponentHandler;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.network.chat.Component;

public abstract class ChemicalRequirement<C extends Chemical<C>, S extends ChemicalStack<C>, T extends ChemicalComponentHandler<C, S, ?, ?>> implements IRequirement<T>, IJEIIngredientRequirement<S> {

    public static <C extends Chemical<C>, S extends ChemicalStack<C>, T extends ChemicalComponentHandler<C, S, ?, ?>, R extends ChemicalRequirement<C, S, T>> NamedCodec<R> makeCodec(NamedCodec<C> chemicalCodec, Function4<RequirementIOMode, C, Long, String, R> builder, String name) {
        return NamedCodec.record(fluidRequirementInstance ->
                fluidRequirementInstance.group(
                        RequirementIOMode.CODEC.fieldOf("mode").forGetter(requirement -> requirement.mode),
                        chemicalCodec.fieldOf("chemical").forGetter(requirement -> requirement.chemical),
                        NamedCodec.LONG.fieldOf("amount").forGetter(requirement -> requirement.amount),
                        NamedCodec.STRING.optionalFieldOf("tank", "").forGetter(requirement -> requirement.tank)
                ).apply(fluidRequirementInstance, builder), name
        );
    }

    final RequirementIOMode mode;
    final C chemical;
    final long amount;
    final String tank;

    public ChemicalRequirement(RequirementIOMode mode, C chemical, long amount, String tank) {
        this.mode = mode;
        this.chemical = chemical;
        this.amount = amount;
        this.tank = tank;
    }

    @Override
    public boolean test(T handler, ICraftingContext context) {
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
    public void gatherRequirements(IRequirementList<T> list) {
        if(this.mode == RequirementIOMode.INPUT)
            list.processOnStart(this::processInput);
        else
            list.processOnEnd(this::processOutput);
    }

    CraftingResult processInput(T handler, ICraftingContext context) {
        long amount = (long)context.getModifiedValue(this.amount, this, null);
        if(!test(handler, context))
            return CraftingResult.error(Component.translatable("custommachinerymekanism.requirements.chemical.error.input", Component.translatable(this.chemical.getTranslationKey()), amount));

        handler.removeFromInputs(this.tank, this.chemical, amount);
        return CraftingResult.success();
    }

    CraftingResult processOutput(T handler, ICraftingContext context) {
        long amount = (long)context.getModifiedValue(this.amount, this, null);
        if(!test(handler, context))
            return CraftingResult.error(Component.translatable("custommachinerymekanism.requirements.chemical.error.output", amount, Component.translatable(this.chemical.getTranslationKey())));

        handler.addToOutputs(this.tank, this.chemical, amount);
        return CraftingResult.success();
    }
}
