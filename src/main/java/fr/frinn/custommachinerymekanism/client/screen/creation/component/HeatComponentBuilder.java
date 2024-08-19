package fr.frinn.custommachinerymekanism.client.screen.creation.component;

import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.component.ComponentBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.component.IMachineComponentBuilder;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.DoubleSlider;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.component.HeatMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.HeatMachineComponent.Template;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class HeatComponentBuilder implements IMachineComponentBuilder<HeatMachineComponent, Template> {

    @Override
    public MachineComponentType<HeatMachineComponent> type() {
        return Registration.HEAT_MACHINE_COMPONENT.get();
    }

    @Override
    public PopupScreen makePopup(MachineEditScreen parent, @Nullable Template template, Consumer<Template> onFinish) {
        return new HeatComponentBuilderPopup(parent, template, onFinish, Component.translatable("custommachinerymekanism.gui.creation.components.heat.title"));
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, int width, int height, Template template) {
        graphics.renderFakeItem(Items.FIRE_CHARGE.getDefaultInstance(), x, y + height / 2 - 8);
        graphics.drawString(Minecraft.getInstance().font, "type: " + template.getType().getId().getPath(), x + 25, y + 5, 0, false);
    }

    public static class HeatComponentBuilderPopup extends ComponentBuilderPopup<Template> {

        private DoubleSlider capacity;
        private DoubleSlider baseTemp;
        private DoubleSlider conduction;
        private DoubleSlider insulation;

        public HeatComponentBuilderPopup(BaseScreen parent, @Nullable Template template, Consumer<Template> onFinish, Component title) {
            super(parent, template, onFinish, title);
        }

        @Override
        public Template makeTemplate() {
            return new Template(this.capacity.doubleValue(), this.baseTemp.doubleValue(), this.conduction.doubleValue(), this.insulation.doubleValue(), ComponentIOMode.INPUT.getBaseConfig());
        }

        @Override
        protected void init() {
            super.init();

            //Capacity
            this.capacity = this.propertyList.add(Component.translatable("custommachinerymekanism.gui.creation.components.heat.capacity"), DoubleSlider.builder().bounds(0.0D, 10000.0D).defaultValue(this.baseTemplate().map(Template::capacity).orElse(373.0D)).displayOnlyValue().create(0, 0, 140, 20, Component.translatable("custommachinerymekanism.gui.creation.components.heat.capacity")));
            this.capacity.setTooltip(Tooltip.create(Component.translatable("custommachinerymekanism.gui.creation.components.heat.capacity.tooltip")));

            //Base Heat
            this.baseTemp = this.propertyList.add(Component.translatable("custommachinerymekanism.gui.creation.components.heat.base"), DoubleSlider.builder().bounds(0.0D, 10000.0D).defaultValue(this.baseTemplate().map(Template::baseTemp).orElse(300.0D)).displayOnlyValue().create(0, 0, 140, 20, Component.translatable("custommachinerymekanism.gui.creation.components.heat.base")));
            this.baseTemp.setTooltip(Tooltip.create(Component.translatable("custommachinerymekanism.gui.creation.components.heat.base.tooltip")));

            //Conduction
            this.conduction = this.propertyList.add(Component.translatable("custommachinerymekanism.gui.creation.components.heat.conduction"), DoubleSlider.builder().bounds(1.0D, 10.0D).defaultValue(this.baseTemplate().map(Template::inverseConductionCoefficient).orElse(1.0D)).displayOnlyValue().create(0, 0, 140, 20, Component.translatable("custommachinerymekanism.gui.creation.components.heat.conduction")));
            this.conduction.setTooltip(Tooltip.create(Component.translatable("custommachinerymekanism.gui.creation.components.heat.conduction.tooltip")));

            //Insulation
            this.insulation = this.propertyList.add(Component.translatable("custommachinerymekanism.gui.creation.components.heat.insulation"), DoubleSlider.builder().bounds(0.0D, 10.0D).defaultValue(this.baseTemplate().map(Template::inverseInsulationCoefficient).orElse(0.0D)).displayOnlyValue().create(0, 0, 140, 20, Component.translatable("custommachinerymekanism.gui.creation.components.heat.insulation")));
            this.insulation.setTooltip(Tooltip.create(Component.translatable("custommachinerymekanism.gui.creation.components.heat.insulation.tooltip")));
        }
    }
}
