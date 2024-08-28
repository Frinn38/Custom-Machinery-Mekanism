package fr.frinn.custommachinerymekanism.client.jei.wrapper;

import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.integration.jei.IRecipeHelper;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.client.jei.CMMJeiPlugin;
import fr.frinn.custommachinerymekanism.client.jei.heat.Heat;
import fr.frinn.custommachinerymekanism.client.jei.heat.HeatJEIIngredientRenderer;
import fr.frinn.custommachinerymekanism.common.guielement.HeatGuiElement;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class HeatIngredientWrapper implements IJEIIngredientWrapper<Heat> {

    private final RequirementIOMode mode;
    private final Heat heat;

    public HeatIngredientWrapper(RequirementIOMode mode, double amount, double chance, boolean isPerTick) {
        this.mode = mode;
        this.heat = new Heat(amount, chance, isPerTick, mode);
    }

    @Override
    public boolean setupRecipe(IRecipeLayoutBuilder builder, int xOffset, int yOffset, IGuiElement element, IRecipeHelper helper) {
        if(!(element instanceof HeatGuiElement heatElement) || element.getType() != Registration.HEAT_GUI_ELEMENT.get())
            return false;

        builder.addSlot(roleFromMode(this.mode), element.getX() - xOffset + 1, element.getY() - yOffset + 1)
                .setCustomRenderer(CMMJeiPlugin.HEAT_INGREDIENT, new HeatJEIIngredientRenderer(heatElement))
                .addIngredient(CMMJeiPlugin.HEAT_INGREDIENT, this.heat)
                .addRichTooltipCallback((slot, tooltips) -> {
                    if(this.heat.isPerTick()) {
                        if(this.heat.mode() == RequirementIOMode.INPUT)
                            tooltips.add(Component.translatable("custommachinerymekanism.jei.ingredient.heat.pertick.input", this.heat.amount()));
                        else
                            tooltips.add(Component.translatable("custommachinerymekanism.jei.ingredient.heat.pertick.output", this.heat.amount()));
                    } else {
                        if(this.heat.mode() == RequirementIOMode.INPUT)
                            tooltips.add(Component.translatable("custommachinerymekanism.jei.ingredient.heat.input", this.heat.amount()));
                        else
                            tooltips.add(Component.translatable("custommachinerymekanism.jei.ingredient.heat.output", this.heat.amount()));
                    }
                    if(this.heat.chance() == 0)
                        tooltips.add(Component.translatable("custommachinery.jei.ingredient.chance.0").withStyle(ChatFormatting.DARK_RED));
                    if(this.heat.chance() < 1.0D && this.heat.chance() > 0)
                        tooltips.add(Component.translatable("custommachinery.jei.ingredient.chance", (int)(this.heat.chance() * 100)));
                });
        return true;
    }
}
