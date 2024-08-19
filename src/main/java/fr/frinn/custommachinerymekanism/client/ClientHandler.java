package fr.frinn.custommachinerymekanism.client;

import com.mojang.blaze3d.systems.RenderSystem;
import fr.frinn.custommachinery.api.guielement.RegisterGuiElementWidgetSupplierEvent;
import fr.frinn.custommachinery.api.integration.jei.RegisterGuiElementJEIRendererEvent;
import fr.frinn.custommachinery.client.screen.creation.component.RegisterComponentBuilderEvent;
import fr.frinn.custommachinery.client.screen.creation.gui.RegisterGuiElementBuilderEvent;
import fr.frinn.custommachinerymekanism.CustomMachineryMekanism;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.client.jei.element.ChemicalGuiElementJeiRenderer;
import fr.frinn.custommachinerymekanism.client.jei.element.HeatGuiElementJeiRenderer;
import fr.frinn.custommachinerymekanism.client.render.element.GasGuiElementWidget;
import fr.frinn.custommachinerymekanism.client.render.element.HeatGuiElementWidget;
import fr.frinn.custommachinerymekanism.client.render.element.InfusionGuiElementWidget;
import fr.frinn.custommachinerymekanism.client.render.element.PigmentGuiElementWidget;
import fr.frinn.custommachinerymekanism.client.render.element.SlurryGuiElementWidget;
import fr.frinn.custommachinerymekanism.client.screen.creation.component.ChemicalComponentBuilder;
import fr.frinn.custommachinerymekanism.client.screen.creation.component.HeatComponentBuilder;
import fr.frinn.custommachinerymekanism.client.screen.creation.gui.ChemicalGuiElementBuilder;
import fr.frinn.custommachinerymekanism.client.screen.creation.gui.HeatGuiElementBuilder;
import fr.frinn.custommachinerymekanism.common.component.GasMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.InfusionMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.PigmentMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.SlurryMachineComponent;
import fr.frinn.custommachinerymekanism.common.guielement.GasGuiElement;
import fr.frinn.custommachinerymekanism.common.guielement.InfusionGuiElement;
import fr.frinn.custommachinerymekanism.common.guielement.PigmentGuiElement;
import fr.frinn.custommachinerymekanism.common.guielement.SlurryGuiElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = CustomMachineryMekanism.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientHandler {

    @SubscribeEvent
    public static void registerGuiElementWidgets(final RegisterGuiElementWidgetSupplierEvent event) {
        event.register(Registration.GAS_GUI_ELEMENT.get(), GasGuiElementWidget::new);
        event.register(Registration.INFUSION_GUI_ELEMENT.get(), InfusionGuiElementWidget::new);
        event.register(Registration.PIGMENT_GUI_ELEMENT.get(), PigmentGuiElementWidget::new);
        event.register(Registration.SLURRY_GUI_ELEMENT.get(), SlurryGuiElementWidget::new);
        event.register(Registration.HEAT_GUI_ELEMENT.get(), HeatGuiElementWidget::new);
    }

    @SubscribeEvent
    public static void registerComponentBuilders(final RegisterComponentBuilderEvent event) {
        event.register(Registration.HEAT_MACHINE_COMPONENT.get(), new HeatComponentBuilder());
        event.register(Registration.GAS_MACHINE_COMPONENT.get(), new ChemicalComponentBuilder<>(Registration.GAS_MACHINE_COMPONENT.get(), GasMachineComponent.Template::new, Component.translatable("custommachinerymekanism.gui.creation.components.gas.title")));
        event.register(Registration.INFUSION_MACHINE_COMPONENT.get(), new ChemicalComponentBuilder<>(Registration.INFUSION_MACHINE_COMPONENT.get(), InfusionMachineComponent.Template::new, Component.translatable("custommachinerymekanism.gui.creation.components.infusion.title")));
        event.register(Registration.PIGMENT_MACHINE_COMPONENT.get(), new ChemicalComponentBuilder<>(Registration.PIGMENT_MACHINE_COMPONENT.get(), PigmentMachineComponent.Template::new, Component.translatable("custommachinerymekanism.gui.creation.components.pigment.title")));
        event.register(Registration.SLURRY_MACHINE_COMPONENT.get(), new ChemicalComponentBuilder<>(Registration.SLURRY_MACHINE_COMPONENT.get(), SlurryMachineComponent.Template::new, Component.translatable("custommachinerymekanism.gui.creation.components.slurry.title")));
    }

    @SubscribeEvent
    public static void registerGuiElementBuilders(final RegisterGuiElementBuilderEvent event) {
        event.register(Registration.HEAT_GUI_ELEMENT.get(), new HeatGuiElementBuilder());
        event.register(Registration.GAS_GUI_ELEMENT.get(), new ChemicalGuiElementBuilder<>(Registration.GAS_GUI_ELEMENT.get(), GasGuiElement::new));
        event.register(Registration.INFUSION_GUI_ELEMENT.get(), new ChemicalGuiElementBuilder<>(Registration.INFUSION_GUI_ELEMENT.get(), InfusionGuiElement::new));
        event.register(Registration.PIGMENT_GUI_ELEMENT.get(), new ChemicalGuiElementBuilder<>(Registration.PIGMENT_GUI_ELEMENT.get(), PigmentGuiElement::new));
        event.register(Registration.SLURRY_GUI_ELEMENT.get(), new ChemicalGuiElementBuilder<>(Registration.SLURRY_GUI_ELEMENT.get(), SlurryGuiElement::new));
    }

    @SubscribeEvent
    public static void registerGuiElementJeiRenderers(final RegisterGuiElementJEIRendererEvent event) {
        event.register(Registration.GAS_GUI_ELEMENT.get(), new ChemicalGuiElementJeiRenderer<>());
        event.register(Registration.INFUSION_GUI_ELEMENT.get(), new ChemicalGuiElementJeiRenderer<>());
        event.register(Registration.PIGMENT_GUI_ELEMENT.get(), new ChemicalGuiElementJeiRenderer<>());
        event.register(Registration.SLURRY_GUI_ELEMENT.get(), new ChemicalGuiElementJeiRenderer<>());
        event.register(Registration.HEAT_GUI_ELEMENT.get(), new HeatGuiElementJeiRenderer());
    }

    public static void bindTexture(ResourceLocation texture) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
    }

    public static void renderSlotHighlight(GuiGraphics graphics, int x, int y, int width, int height) {
        RenderSystem.disableDepthTest();
        RenderSystem.colorMask(true, true, true, false);
        graphics.fill(x, y, x + width, y + height, -2130706433);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.enableDepthTest();
    }
}
