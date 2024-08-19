package fr.frinn.custommachinerymekanism.client.screen.creation.gui;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.gui.IGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.MutableProperties;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement.Properties;
import fr.frinn.custommachinerymekanism.common.guielement.ChemicalGuiElement;
import fr.frinn.custommachinerymekanism.common.guielement.ChemicalGuiElement.Builder;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public record ChemicalGuiElementBuilder<E extends ChemicalGuiElement<?>>(
        GuiElementType<E> type, Builder<E> builder) implements IGuiElementBuilder<E> {

    @Override
    public E make(Properties properties, @Nullable E from) {
        if(from != null)
            return this.builder.build(properties, from.highlight());
        else
            return this.builder.build(properties, true);
    }

    @Override
    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, @Nullable E from, Consumer<E> onFinish) {
        return new ChemicalGuiElementBuilderPopup<>(parent, properties, from, onFinish, this.builder);
    }

    public static class ChemicalGuiElementBuilderPopup<E extends ChemicalGuiElement<?>> extends GuiElementBuilderPopup<E> {

        private final Builder<E> builder;

        private Checkbox highlight;

        public ChemicalGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable E from, Consumer<E> onFinish, Builder<E> builder) {
            super(parent, properties, from, onFinish);
            this.builder = builder;
        }

        @Override
        public E makeElement() {
            return this.builder.build(this.properties.build(), this.highlight.selected());
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
