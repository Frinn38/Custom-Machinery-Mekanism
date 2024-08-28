package fr.frinn.custommachinerymekanism.client.jei.wrapper;

import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.integration.jei.IRecipeHelper;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.impl.util.IntRange;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.client.jei.CMMJeiPlugin;
import fr.frinn.custommachinerymekanism.client.jei.heat.Heat;
import fr.frinn.custommachinerymekanism.client.jei.heat.HeatJEIIngredientRenderer;
import fr.frinn.custommachinerymekanism.common.guielement.HeatGuiElement;
import mekanism.common.MekanismLang;
import mekanism.common.util.UnitDisplayUtils.TemperatureUnit;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;

public class TemperatureIngredientWrapper implements IJEIIngredientWrapper<Heat> {

    private final IntRange temp;
    private final TemperatureUnit unit;
    private final Heat ingredient;

    public TemperatureIngredientWrapper(IntRange temp, TemperatureUnit unit) {
        this.temp = temp;
        this.unit = unit;
        this.ingredient = new Heat(0, 1, false, RequirementIOMode.INPUT);
    }

    @Override
    public boolean setupRecipe(IRecipeLayoutBuilder builder, int xOffset, int yOffset, IGuiElement element, IRecipeHelper helper) {
        if(!(element instanceof HeatGuiElement heatElement) || element.getType() != Registration.HEAT_GUI_ELEMENT.get())
            return false;

        builder.addSlot(RecipeIngredientRole.INPUT, element.getX() - xOffset + 1, element.getY() - yOffset + 1)
                .setCustomRenderer(CMMJeiPlugin.HEAT_INGREDIENT, new HeatJEIIngredientRenderer(heatElement))
                .addIngredient(CMMJeiPlugin.HEAT_INGREDIENT, this.ingredient)
                .addRichTooltipCallback((view, tooltips) -> {
                    tooltips.add(Component.translatable("custommachinerymekanism.requirements.temp.error", this.temp.toFormattedString()));
                    tooltips.add(MekanismLang.UNIT.translate(this.unit.getLabel(true).translate()));
                });
        return true;
    }
}
