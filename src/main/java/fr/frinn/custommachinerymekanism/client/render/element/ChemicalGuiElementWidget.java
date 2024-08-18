package fr.frinn.custommachinerymekanism.client.render.element;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.impl.guielement.TexturedGuiElementWidget;
import fr.frinn.custommachinerymekanism.client.ClientHandler;
import fr.frinn.custommachinerymekanism.common.component.ChemicalMachineComponent;
import fr.frinn.custommachinerymekanism.common.guielement.ChemicalGuiElement;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.math.MathUtils;
import mekanism.api.text.EnumColor;
import mekanism.api.text.TextComponentUtil;
import mekanism.client.gui.GuiUtils;
import mekanism.client.gui.GuiUtils.TilingDirection;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.MekanismLang;
import mekanism.common.util.ChemicalUtil;
import mekanism.common.util.text.TextUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ChemicalGuiElementWidget<C extends ChemicalMachineComponent<?, ?>, E extends ChemicalGuiElement<C>> extends TexturedGuiElementWidget<E> {

    public ChemicalGuiElementWidget(E element, IMachineScreen screen, Component title) {
        super(element, screen, title);
    }

    public abstract MachineComponentType<C> componentType();

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        this.getScreen().getTile().getComponentManager().getComponentHandler(componentType()).flatMap(gasHandler -> gasHandler.getComponentForID(this.getElement().getComponentId())).ifPresent(component -> {
            ChemicalStack<?> stack = component.getStack();
            if(!stack.isEmpty()) {
                RenderSystem.enableBlend();
                int desiredHeight = MathUtils.clampToInt((double)(this.height - 2) * (double)stack.getAmount() / (double)component.getCapacity());
                if (desiredHeight < 1) {
                    desiredHeight = 1;
                }

                if (desiredHeight > this.height) {
                    desiredHeight = this.height;
                }

                Chemical<?> chemical = stack.getChemical();
                MekanismRenderer.color(graphics, chemical);
                GuiUtils.drawTiledSprite(graphics, this.getX() + 1, this.getY() + 1, this.height - 2, this.width - 2, desiredHeight, MekanismRenderer.getSprite(chemical.getIcon()), 16, 16, 100, TilingDirection.UP_RIGHT, false);
                MekanismRenderer.resetColor(graphics);
                RenderSystem.disableBlend();
            }
        });
        if (this.isHoveredOrFocused() && this.getElement().highlight())
            ClientHandler.renderSlotHighlight(graphics, this.getX() + 1, this.getY() + 1, this.width - 2, this.height - 2);
    }

    @Override
    public List<Component> getTooltips() {
        return this.getScreen().getTile().getComponentManager()
                .getComponentHandler(componentType())
                .flatMap(gasHandler -> gasHandler.getComponentForID(this.getElement().getComponentId()))
                .map(component -> {
                    ChemicalStack<?> stack = component.getStack();
                    List<Component> tooltips = new ArrayList<>();
                    tooltips.add(TextComponentUtil.build(stack));
                    tooltips.add(MekanismLang.GENERIC_MB.translateColored(EnumColor.GRAY, TextUtils.format(stack.getAmount())));
                    ChemicalUtil.addChemicalDataToTooltip(tooltips, stack.getChemical(), false);
                    return tooltips;
                })
                .orElse(Collections.emptyList());
    }
}
