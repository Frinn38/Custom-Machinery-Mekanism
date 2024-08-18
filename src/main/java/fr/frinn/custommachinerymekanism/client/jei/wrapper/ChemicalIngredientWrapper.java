package fr.frinn.custommachinerymekanism.client.jei.wrapper;

import fr.frinn.custommachinery.api.component.IMachineComponentTemplate;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.integration.jei.IRecipeHelper;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinerymekanism.common.guielement.ChemicalGuiElement;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.client.recipe_viewer.jei.ChemicalStackRenderer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.Optional;
import java.util.function.BiFunction;

public class ChemicalIngredientWrapper<C extends Chemical<C>, S extends ChemicalStack<C>> implements IJEIIngredientWrapper<S> {

    private final RequirementIOMode mode;
    private final C chemical;
    private final long amount;
    private final double chance;
    private final boolean isPerTick;
    private final String tank;
    private final IIngredientType<S> ingredientType;
    private final BiFunction<C, Long, S> stackBuilder;

    public ChemicalIngredientWrapper(RequirementIOMode mode, C chemical, long amount, double chance, boolean isPerTick, String tank, IIngredientType<S> ingredientType, BiFunction<C, Long, S> stackBuilder) {
        this.mode = mode;
        this.chemical = chemical;
        this.amount = amount;
        this.chance = chance;
        this.isPerTick = isPerTick;
        this.tank = tank;
        this.ingredientType = ingredientType;
        this.stackBuilder = stackBuilder;
    }

    @Override
    public boolean setupRecipe(IRecipeLayoutBuilder builder, int xOffset, int yOffset, IGuiElement element, IRecipeHelper helper) {
        if(!(element instanceof ChemicalGuiElement<?> chemicalElement))
            return false;

        S ingredient = this.stackBuilder.apply(this.chemical, this.amount);
        Optional<IMachineComponentTemplate<?>> template = helper.getComponentForElement(chemicalElement);
        if(chemicalElement.getComponentId().equals(this.tank) || template.map(t -> t.canAccept(ingredient, this.mode == RequirementIOMode.INPUT, helper.getDummyManager()) && (this.tank.isEmpty() || t.getId().equals(this.tank))).orElse(false)) {
            builder.addSlot(roleFromMode(this.mode), element.getX() - xOffset, element.getY() - yOffset)
                    .setCustomRenderer(this.ingredientType, new ChemicalStackRenderer<>(this.amount, element.getWidth() - 2, element.getHeight() - 2))
                    .addIngredient(this.ingredientType, ingredient)
                    .addRichTooltipCallback((view, tooltips) -> {
                        if(this.isPerTick)
                            tooltips.add(Component.translatable("custommachinery.jei.ingredient.fluid.pertick"));

                        if(this.chance == 0)
                            tooltips.add(Component.translatable("custommachinery.jei.ingredient.chance.0").withStyle(ChatFormatting.DARK_RED));
                        else if(this.chance != 1.0)
                            tooltips.add(Component.translatable("custommachinery.jei.ingredient.chance", (int)(this.chance * 100)));

                        if(!this.tank.isEmpty() && Minecraft.getInstance().options.advancedItemTooltips)
                            tooltips.add(Component.translatable("custommachinery.jei.ingredient.fluid.specificTank").withStyle(ChatFormatting.DARK_RED));
                    });
            return true;
        }
        return false;
    }
}
