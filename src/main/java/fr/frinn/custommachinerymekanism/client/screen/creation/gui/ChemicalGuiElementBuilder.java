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
import fr.frinn.custommachinerymekanism.common.guielement.ChemicalGuiElement;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ChemicalGuiElementBuilder implements IGuiElementBuilder<ChemicalGuiElement> {

    @Override
    public GuiElementType<ChemicalGuiElement> type() {
        return Registration.CHEMICAL_GUI_ELEMENT.get();
    }

    @Override
    public ChemicalGuiElement make(Properties properties, @Nullable ChemicalGuiElement from) {
        if(from != null)
            return new ChemicalGuiElement(properties, from.highlight());
        else
            return new ChemicalGuiElement(properties, true);
    }

    @Override
    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, @Nullable ChemicalGuiElement from, Consumer<ChemicalGuiElement> onFinish) {
        return new ChemicalGuiElementBuilderPopup(parent, properties, from, onFinish);
    }

    public static class ChemicalGuiElementBuilderPopup extends GuiElementBuilderPopup<ChemicalGuiElement> {

        private Checkbox highlight;

        public ChemicalGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable ChemicalGuiElement from, Consumer<ChemicalGuiElement> onFinish) {
            super(parent, properties, from, onFinish);
        }

        @Override
        public ChemicalGuiElement makeElement() {
            return new ChemicalGuiElement(this.properties.build(), this.highlight.selected());
        }

        @Override
        public Component canCreate() {
            if(this.properties.getId().isEmpty())
                return Component.translatable("custommachinery.gui.creation.gui.id.missing");
            return super.canCreate();
        }

        @Override
        public void addWidgets(RowHelper row) {
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.texture"), this.properties::setTexture, this.baseElement != null ? this.baseElement.getTexture() : ChemicalGuiElement.BASE_TEXTURE);
            this.addId(row);
            this.addPriority(row);
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.highlight"), this.font));
            this.highlight = row.addChild(Checkbox.builder(Component.translatable("custommachinery.gui.creation.gui.highlight"), this.font).selected(this.baseElement == null || this.baseElement.highlight()).build());
        }
    }
}
