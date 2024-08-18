package fr.frinn.custommachinerymekanism.client.jei.element;

import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.integration.jei.IJEIElementRenderer;
import fr.frinn.custommachinerymekanism.common.guielement.ChemicalGuiElement;
import net.minecraft.client.gui.GuiGraphics;

public class ChemicalGuiElementJeiRenderer<E extends ChemicalGuiElement<?>> implements IJEIElementRenderer<E> {

    @Override
    public void renderElementInJEI(GuiGraphics graphics, E element, IMachineRecipe recipe, int mouseX, int mouseY) {
        int posX = element.getX();
        int posY = element.getY();
        int width = element.getWidth();
        int height = element.getHeight();

        graphics.blit(element.getTexture(), posX, posY, 0, 0, width, height, width, height);
    }
}
