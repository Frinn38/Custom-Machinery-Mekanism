package fr.frinn.custommachinerymekanism.client.screen.creation.component;

import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.utils.Filter;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.component.ComponentBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.component.ComponentConfigBuilderWidget;
import fr.frinn.custommachinery.client.screen.creation.component.IMachineComponentBuilder;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.impl.component.config.IOSideConfig;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.component.ChemicalMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.ChemicalMachineComponent.Template;
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

public class ChemicalComponentBuilder implements IMachineComponentBuilder<ChemicalMachineComponent, Template> {

    @Override
    public MachineComponentType<ChemicalMachineComponent> type() {
        return Registration.CHEMICAL_MACHINE_COMPONENT.get();
    }

    @Override
    public PopupScreen makePopup(MachineEditScreen parent, @Nullable Template template, Consumer<Template> onFinish) {
        return new ChemicalComponentBuilderPopup(parent, template, onFinish, Component.translatable("custommachinerymekanism.gui.creation.components.chemical.title"));
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height, Template template) {
        graphics.renderFakeItem(MekanismBlocks.ULTIMATE_CHEMICAL_TANK.getItemStack(), x, y + height / 2 - 8);
        graphics.drawString(Minecraft.getInstance().font, "type: " + template.getType().getId().getPath(), x + 25, y + 5, 0, false);
        graphics.drawString(Minecraft.getInstance().font, "id: \"" + template.getId() + "\"", x + 25, y + 15, FastColor.ARGB32.color(255, 128, 0, 0), false);
        graphics.drawString(Minecraft.getInstance().font, "mode: " + template.mode(), x + 25, y + 25, FastColor.ARGB32.color(255, 0, 0, 128), false);
    }

    public static class ChemicalComponentBuilderPopup extends ComponentBuilderPopup<Template> {

        private EditBox id;
        private CycleButton<ComponentIOMode> mode;
        private EditBox capacity;
        private EditBox maxInput;
        private EditBox maxOutput;
        private Checkbox unique;
        private IOSideConfig.Template config;

        public ChemicalComponentBuilderPopup(BaseScreen parent, @Nullable Template template, Consumer<Template> onFinish, Component title) {
            super(parent, template, onFinish, title);
        }

        @Override
        public Template makeTemplate() {
            return new Template(this.id.getValue(), this.parseLong(this.capacity.getValue()), this.mode.getValue(), this.baseTemplate().map(Template::filter).orElse(Filter.empty()), this.parseLong(this.maxInput.getValue()), this.parseLong(this.maxOutput.getValue()), this.config, this.unique.selected());
        }

        @Override
        public Component canCreate() {
            if(this.id.getValue().isEmpty())
                return Component.translatable("custommachinery.gui.creation.gui.id.missing");
            else if(this.parent instanceof MachineEditScreen screen && screen.getBuilder().getComponents().stream().anyMatch(template -> template.getType() == Registration.CHEMICAL_MACHINE_COMPONENT.get() && this.baseTemplate().map(base -> base != template).orElse(true) && template.getId().equals(this.id.getValue())))
                return Component.translatable("custommachinery.gui.creation.gui.id.duplicate", this.id.getValue());
            else
                return Component.empty();
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
            this.baseTemplate().ifPresent(template -> this.mode.setValue(template.mode()));

            //Capacity
            this.capacity = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.capacity"), new EditBox(this.font, 0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.capacity")));
            this.capacity.setFilter(this::checkLong);
            this.baseTemplate().ifPresentOrElse(template -> this.capacity.setValue("" + template.capacity()), () -> this.capacity.setValue("10000"));

            //Max input
            this.maxInput = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.maxInput"), new EditBox(this.font, 0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.maxInput")));
            this.maxInput.setFilter(this::checkLong);
            this.baseTemplate().ifPresentOrElse(template -> this.maxInput.setValue("" + template.maxInput()), () -> this.maxInput.setValue("10000"));

            //Max output
            this.maxOutput = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.maxOutput"), new EditBox(this.font, 0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.maxOutput")));
            this.maxOutput.setFilter(this::checkLong);
            this.baseTemplate().ifPresentOrElse(template -> this.maxOutput.setValue("" + template.maxOutput()), () -> this.maxOutput.setValue("10000"));

            //Unique
            this.unique = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.fluid.unique"), Checkbox.builder(Component.translatable("custommachinery.gui.creation.components.fluid.unique"), this.font).selected(false).build());
            if(this.baseTemplate().map(Template::unique).orElse(false) != this.unique.selected())
                this.unique.onPress();

            //Config
            this.baseTemplate().ifPresentOrElse(template -> this.config = template.config(), () -> this.config = IOSideConfig.Template.DEFAULT_ALL_INPUT);
            this.propertyList.add(Component.translatable("custommachinery.gui.config.component"), ComponentConfigBuilderWidget.make(0, 0, 180, 20, Component.translatable("custommachinery.gui.config.component"), this.parent, () -> this.config, template -> this.config = template));
        }
    }
}
