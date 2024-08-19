package fr.frinn.custommachinerymekanism.client.screen.creation.component;

import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.utils.Filter;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.component.ComponentBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.component.IMachineComponentBuilder;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinerymekanism.common.component.ChemicalMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.ChemicalMachineComponent.Template;
import fr.frinn.custommachinerymekanism.common.component.ChemicalMachineComponent.Template.Builder;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.common.registries.MekanismBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public record ChemicalComponentBuilder<C extends Chemical<C>, S extends ChemicalStack<C>, MC extends ChemicalMachineComponent<C, S>, T extends Template<C, S, MC>>(
        MachineComponentType<MC> type, Builder<C, S, MC, T> builder, Component title) implements IMachineComponentBuilder<MC, T> {

    @Override
    public PopupScreen makePopup(MachineEditScreen parent, @Nullable T template, Consumer<T> onFinish) {
        return new ChemicalComponentBuilderPopup<>(parent, template, onFinish, this.title, this.builder);
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height, T template) {
        graphics.renderFakeItem(MekanismBlocks.ULTIMATE_CHEMICAL_TANK.getItemStack(), x, y + height / 2 - 8);
        graphics.drawString(Minecraft.getInstance().font, "type: " + template.getType().getId().getPath(), x + 25, y + 5, 0, false);
        graphics.drawString(Minecraft.getInstance().font, "id: \"" + template.getId() + "\"", x + 25, y + 15, FastColor.ARGB32.color(255, 128, 0, 0), false);
        graphics.drawString(Minecraft.getInstance().font, "mode: " + template.mode, x + 25, y + 25, FastColor.ARGB32.color(255, 0, 0, 128), false);
    }

    public static class ChemicalComponentBuilderPopup<C extends Chemical<C>, S extends ChemicalStack<C>, CM extends ChemicalMachineComponent<C, S>, T extends Template<C, S, CM>> extends ComponentBuilderPopup<T> {

        private final Builder<C, S, CM, T> builder;

        private EditBox id;
        private CycleButton<ComponentIOMode> mode;
        private EditBox capacity;
        private EditBox maxInput;
        private EditBox maxOutput;
        private Checkbox unique;

        public ChemicalComponentBuilderPopup(BaseScreen parent, @Nullable T template, Consumer<T> onFinish, Component title, Builder<C, S, CM, T> builder) {
            super(parent, template, onFinish, title);
            this.builder = builder;
        }

        @Override
        public T makeTemplate() {
            return this.builder.build(this.id.getValue(), this.parseLong(this.capacity.getValue()), this.mode.getValue(), this.baseTemplate().map(template -> template.filter).orElse(Filter.empty()), this.parseLong(this.maxInput.getValue()), this.parseLong(this.maxOutput.getValue()), this.mode.getValue().getBaseConfig(), this.unique.selected());
        }

        @Override
        protected void init() {
            super.init();

            //ID
            this.id = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.id"), new EditBox(Minecraft.getInstance().font, 0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.id")));
            this.baseTemplate().ifPresentOrElse(template -> this.id.setValue(template.getId()), () -> this.id.setValue("input"));
            this.id.setTooltip(Tooltip.create(Component.translatable("custommachinery.gui.creation.components.id.tooltip")));

            //Mode
            this.mode = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.mode"), CycleButton.builder(ComponentIOMode::toComponent).displayOnlyValue().withValues(ComponentIOMode.values()).withInitialValue(ComponentIOMode.BOTH).create(0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.mode")));
            this.baseTemplate().ifPresent(template -> this.mode.setValue(template.mode));

            //Capacity
            this.capacity = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.capacity"), new EditBox(this.font, 0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.capacity")));
            this.capacity.setFilter(this::checkLong);
            this.baseTemplate().ifPresentOrElse(template -> this.capacity.setValue("" + template.capacity), () -> this.capacity.setValue("10000"));

            //Max input
            this.maxInput = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.maxInput"), new EditBox(this.font, 0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.maxInput")));
            this.maxInput.setFilter(this::checkLong);
            this.baseTemplate().ifPresentOrElse(template -> this.maxInput.setValue("" + template.maxInput), () -> this.maxInput.setValue("10000"));

            //Max output
            this.maxOutput = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.maxOutput"), new EditBox(this.font, 0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.maxOutput")));
            this.maxOutput.setFilter(this::checkLong);
            this.baseTemplate().ifPresentOrElse(template -> this.maxOutput.setValue("" + template.maxOutput), () -> this.maxOutput.setValue("10000"));

            //Unique
            this.unique = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.fluid.unique"), Checkbox.builder(Component.translatable("custommachinery.gui.creation.components.fluid.unique"), this.font).selected(false).build());
            if(this.baseTemplate().map(template -> template.unique).orElse(false) != this.unique.selected())
                this.unique.onPress();
        }
    }
}
