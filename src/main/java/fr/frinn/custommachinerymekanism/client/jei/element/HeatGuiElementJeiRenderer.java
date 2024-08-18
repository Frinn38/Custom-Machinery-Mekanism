package fr.frinn.custommachinerymekanism.client.jei.element;

import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.integration.jei.IJEIElementRenderer;
import fr.frinn.custommachinerymekanism.common.guielement.HeatGuiElement;
import net.minecraft.client.gui.GuiGraphics;

public class HeatGuiElementJeiRenderer implements IJEIElementRenderer<HeatGuiElement> {

    @Override
    public void renderElementInJEI(GuiGraphics graphics, HeatGuiElement element, IMachineRecipe recipe, int mouseX, int mouseY) {
        int posX = element.getX();
        int posY = element.getY();
        int width = element.getWidth();
        int height = element.getHeight();

        graphics.blit(element.getTexture(), posX, posY, 0, 0, width, height, width, height);
    }
}
