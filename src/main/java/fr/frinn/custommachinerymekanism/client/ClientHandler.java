package fr.frinn.custommachinerymekanism.client;

import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.RegisterGuiElementWidgetSupplierEvent;
import fr.frinn.custommachinery.api.integration.jei.RegisterGuiElementJEIRendererEvent;
import fr.frinn.custommachinery.api.integration.jei.RegisterWidgetToJeiIngredientGetterEvent;
import fr.frinn.custommachinery.client.screen.creation.component.RegisterComponentBuilderEvent;
import fr.frinn.custommachinery.client.screen.creation.gui.RegisterGuiElementBuilderEvent;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElementWidget;
import fr.frinn.custommachinery.impl.integration.jei.WidgetToJeiIngredientRegistry.IngredientGetter;
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
import fr.frinn.custommachinerymekanism.common.component.ChemicalMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.GasMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.InfusionMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.PigmentMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.SlurryMachineComponent;
import fr.frinn.custommachinerymekanism.common.guielement.ChemicalGuiElement;
import fr.frinn.custommachinerymekanism.common.guielement.GasGuiElement;
import fr.frinn.custommachinerymekanism.common.guielement.InfusionGuiElement;
import fr.frinn.custommachinerymekanism.common.guielement.PigmentGuiElement;
import fr.frinn.custommachinerymekanism.common.guielement.SlurryGuiElement;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import org.jetbrains.annotations.Nullable;

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

    @SubscribeEvent
    public static void registerWidgetToJeiIngredientGetters(final RegisterWidgetToJeiIngredientGetterEvent event) {
        event.register(Registration.GAS_GUI_ELEMENT.get(), new ChemicalIngredientGetter<>(Registration.GAS_MACHINE_COMPONENT.get()));
        event.register(Registration.INFUSION_GUI_ELEMENT.get(), new ChemicalIngredientGetter<>(Registration.INFUSION_MACHINE_COMPONENT.get()));
        event.register(Registration.PIGMENT_GUI_ELEMENT.get(), new ChemicalIngredientGetter<>(Registration.PIGMENT_MACHINE_COMPONENT.get()));
        event.register(Registration.SLURRY_GUI_ELEMENT.get(), new ChemicalIngredientGetter<>(Registration.SLURRY_MACHINE_COMPONENT.get()));
    }

    private record ChemicalIngredientGetter<C extends Chemical<C>, S extends ChemicalStack<C>, CM extends ChemicalMachineComponent<C, S>, E extends ChemicalGuiElement<CM>>(
            MachineComponentType<CM> type) implements IngredientGetter<E> {

        @Nullable
        @Override
        public <T> IClickableIngredient<T> getIngredient(AbstractGuiElementWidget<E> widget, double mouseX, double mouseY, IJeiHelpers helpers) {
            ChemicalMachineComponent<C, S> component = widget.getScreen().getTile().getComponentManager().getComponentHandler(type).flatMap(handler -> handler.getComponentForID(widget.getElement().getComponentId())).orElse(null);
            if(component == null)
                return null;
            return helpers.getIngredientManager().createTypedIngredient(component.getStack()).map(ingredient ->
                new IClickableIngredient<T>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public ITypedIngredient<T> getTypedIngredient() {
                        return (ITypedIngredient<T>) ingredient;
                    }

                    @Override
                    public Rect2i getArea() {
                        return new Rect2i(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
                    }
                }
            ).orElse(null);
        }
    }
}
