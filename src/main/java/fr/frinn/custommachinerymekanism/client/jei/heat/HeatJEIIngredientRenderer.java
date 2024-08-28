package fr.frinn.custommachinerymekanism.client.jei.heat;

import fr.frinn.custommachinery.api.integration.jei.JEIIngredientRenderer;
import fr.frinn.custommachinerymekanism.client.jei.CMMJeiPlugin;
import fr.frinn.custommachinerymekanism.common.guielement.HeatGuiElement;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.Collections;
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
    public void render(GuiGraphics graphics, Heat ingredient) {
        int width = this.element.getWidth();
        int height = this.element.getHeight();

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 10);
        graphics.blit(this.element.getFilledTexture(), -1, -1, 0, 0, width, height, width, height);
        graphics.pose().popPose();
    }

    /** safe to remove **/
    @SuppressWarnings("removal")
    @Override
    public List<Component> getTooltip(Heat ingredient, TooltipFlag tooltipFlag) {
        return Collections.emptyList();
    }
}
