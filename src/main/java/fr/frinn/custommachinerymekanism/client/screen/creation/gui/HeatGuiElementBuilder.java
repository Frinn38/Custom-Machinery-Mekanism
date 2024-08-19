package fr.frinn.custommachinerymekanism.client.screen.creation.gui;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.gui.IGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.MutableProperties;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement.Properties;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.guielement.HeatGuiElement;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class HeatGuiElementBuilder implements IGuiElementBuilder<HeatGuiElement> {

    @Override
    public GuiElementType<HeatGuiElement> type() {
        return Registration.HEAT_GUI_ELEMENT.get();
    }

    @Override
    public HeatGuiElement make(Properties properties, @Nullable HeatGuiElement from) {
        if(from != null)
            return new HeatGuiElement(properties, from.highlight());
        else
            return new HeatGuiElement(properties, true);
    }

    @Override
    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, @Nullable HeatGuiElement from, Consumer<HeatGuiElement> onFinish) {
        return new HeatGuiElementBuilderPopup(parent, properties, from, onFinish);
    }

    public static class HeatGuiElementBuilderPopup extends GuiElementBuilderPopup<HeatGuiElement> {

        private Checkbox highlight;

        public HeatGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable HeatGuiElement from, Consumer<HeatGuiElement> onFinish) {
            super(parent, properties, from, onFinish);
        }

        @Override
        public HeatGuiElement makeElement() {
            return new HeatGuiElement(this.properties.build(), this.highlight.selected());
        }

        @Override
        public void addWidgets(RowHelper row) {
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.texture"), this.properties::setTexture, this.baseElement != null ? this.baseElement.getTexture() : HeatGuiElement.BASE_TEXTURE);
            this.addPriority(row);
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.highlight"), this.font));
            this.highlight = row.addChild(Checkbox.builder(Component.translatable("custommachinery.gui.creation.gui.highlight"), this.font).selected(this.baseElement == null || this.baseElement.highlight()).build());
        }
    }
}