package fr.frinn.custommachinerymekanism.common.requirement;

import com.mojang.datafixers.util.Function4;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.impl.codec.CodecLogger;
import fr.frinn.custommachinery.impl.codec.EnumCodec;
import fr.frinn.custommachinery.impl.requirement.AbstractChanceableRequirement;
import fr.frinn.custommachinery.impl.requirement.AbstractRequirement;
import fr.frinn.custommachinerymekanism.common.component.handler.ChemicalComponentHandler;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.network.chat.TranslatableComponent;

public abstract class ChemicalRequirement<C extends Chemical<C>, S extends ChemicalStack<C>, T extends ChemicalComponentHandler<C, S, ?, ?>> extends AbstractChanceableRequirement<T> implements IJEIIngredientRequirement<S> {

    public static <C extends Chemical<C>, S extends ChemicalStack<C>, T extends ChemicalComponentHandler<C, S, ?, ?>, R extends ChemicalRequirement<C, S, T>> Codec<R> makeCodec(Codec<C> chemicalCodec, Function4<RequirementIOMode, C, Long, String, R> builder) {
        return RecordCodecBuilder.create(fluidRequirementInstance ->
                fluidRequirementInstance.group(
                        EnumCodec.of(RequirementIOMode.class).fieldOf("mode").forGetter(AbstractRequirement::getMode),
                        chemicalCodec.fieldOf("chemical").forGetter(requirement -> requirement.chemical),
                        Codec.LONG.fieldOf("amount").forGetter(requirement -> requirement.amount),
                        CodecLogger.loggedOptional(Codec.doubleRange(0.0, 1.0),"chance", 1.0D).forGetter(AbstractChanceableRequirement::getChance),
                        CodecLogger.loggedOptional(Codec.STRING,"tank", "").forGetter(requirement -> requirement.tank)
                ).apply(fluidRequirementInstance, (mode, gas, amount, chance, tank) -> {
                    R requirement = builder.apply(mode, gas, amount, tank);
                    requirement.setChance(chance);
                    return requirement;
                })
        );
    }

    final C chemical;
    final long amount;
    final String tank;

    public ChemicalRequirement(RequirementIOMode mode, C chemical, long amount, String tank) {
        super(mode);
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
    public CraftingResult processStart(T handler, ICraftingContext context) {
        if(this.getMode() != RequirementIOMode.INPUT)
            return CraftingResult.pass();

        long amount = (long)context.getModifiedValue(this.amount, this, null);
        if(!test(handler, context))
            return CraftingResult.error(new TranslatableComponent("custommachinerymekanism.requirements.chemical.error.input", new TranslatableComponent(this.chemical.getTranslationKey()), amount));

        handler.removeFromInputs(this.tank, this.chemical, amount);
        return CraftingResult.success();
    }

    @Override
    public CraftingResult processEnd(T handler, ICraftingContext context) {
        if(this.getMode() != RequirementIOMode.OUTPUT)
            return CraftingResult.pass();

        long amount = (long)context.getModifiedValue(this.amount, this, null);
        if(!test(handler, context))
            return CraftingResult.error(new TranslatableComponent("custommachinerymekanism.requirements.chemical.error.output", amount, new TranslatableComponent(this.chemical.getTranslationKey())));

        handler.addToOutputs(this.tank, this.chemical, amount);
        return CraftingResult.success();
    }
}
