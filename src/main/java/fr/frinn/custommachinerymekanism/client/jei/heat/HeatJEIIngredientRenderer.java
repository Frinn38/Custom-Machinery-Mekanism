package fr.frinn.custommachinerymekanism.client.jei.heat;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.frinn.custommachinery.api.integration.jei.JEIIngredientRenderer;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinerymekanism.client.jei.CMMJeiPlugin;
import fr.frinn.custommachinerymekanism.common.guielement.HeatGuiElement;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

public class HeatJEIIngredientRenderer extends JEIIngredientRenderer<Heat, HeatGuiElement> {

    public HeatJEIIngredientRenderer(HeatGuiElement element) {
        super(element);
    }

    @Override
    public IIngredientType<Heat> getType() {
        return CMMJeiPlugin.HEAT_INGREDIENT;
    }

    @Override
    public int getWidth() {
        return this.element.getWidth() - 2;
    }

    @Override
    public int getHeight() {
        return this.element.getHeight() - 2;
    }

    @Override
    public void render(PoseStack pose, Heat ingredient) {
        int width = this.element.getWidth();
        int height = this.element.getHeight();

        GuiComponent.fill(pose, 0, 0, width - 2, height - 2, FastColor.ARGB32.color(200, 255, 128, 0));
    }

    @Override
    public List<Component> getTooltip(Heat ingredient, TooltipFlag tooltipFlag) {
        List<Component> tooltips = new ArrayList<>();
        if(ingredient.isPerTick()) {
            if(ingredient.mode() == RequirementIOMode.INPUT)
                tooltips.add(Component.translatable("custommachinerymekanism.jei.ingredient.heat.pertick.input", ingredient.amount()));
            else
                tooltips.add(Component.translatable("custommachinerymekanism.jei.ingredient.heat.pertick.output", ingredient.amount()));
        } else {
            if(ingredient.mode() == RequirementIOMode.INPUT)
                tooltips.add(Component.translatable("custommachinerymekanism.jei.ingredient.heat.input", ingredient.amount()));
            else
                tooltips.add(Component.translatable("custommachinerymekanism.jei.ingredient.heat.output", ingredient.amount()));
        }
        if(ingredient.chance() == 0)
            tooltips.add(Component.translatable("custommachinery.jei.ingredient.chance.0").withStyle(ChatFormatting.DARK_RED));
        if(ingredient.chance() < 1.0D && ingredient.chance() > 0)
            tooltips.add(Component.translatable("custommachinery.jei.ingredient.chance", (int)(ingredient.chance() * 100)));
        return tooltips;
    }
}
