package fr.frinn.custommachinerymekanism.client;

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
import fr.frinn.custommachinerymekanism.client.render.element.ChemicalGuiElementWidget;
import fr.frinn.custommachinerymekanism.client.render.element.HeatGuiElementWidget;
import fr.frinn.custommachinerymekanism.client.screen.creation.component.ChemicalComponentBuilder;
import fr.frinn.custommachinerymekanism.client.screen.creation.component.HeatComponentBuilder;
import fr.frinn.custommachinerymekanism.client.screen.creation.gui.ChemicalGuiElementBuilder;
import fr.frinn.custommachinerymekanism.client.screen.creation.gui.HeatGuiElementBuilder;
import fr.frinn.custommachinerymekanism.common.component.ChemicalMachineComponent;
import fr.frinn.custommachinerymekanism.common.guielement.ChemicalGuiElement;
import mekanism.api.chemical.ChemicalStack;
import mekanism.client.recipe_viewer.jei.MekanismJEI;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.client.renderer.Rect2i;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = CustomMachineryMekanism.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientHandler {

    @SubscribeEvent
    public static void registerGuiElementWidgets(final RegisterGuiElementWidgetSupplierEvent event) {
        event.register(Registration.CHEMICAL_GUI_ELEMENT.get(), ChemicalGuiElementWidget::new);
        event.register(Registration.HEAT_GUI_ELEMENT.get(), HeatGuiElementWidget::new);
    }

    @SubscribeEvent
    public static void registerComponentBuilders(final RegisterComponentBuilderEvent event) {
        event.register(Registration.HEAT_MACHINE_COMPONENT.get(), new HeatComponentBuilder());
        event.register(Registration.CHEMICAL_MACHINE_COMPONENT.get(), new ChemicalComponentBuilder());
    }

    @SubscribeEvent
    public static void registerGuiElementBuilders(final RegisterGuiElementBuilderEvent event) {
        event.register(Registration.HEAT_GUI_ELEMENT.get(), new HeatGuiElementBuilder());
        event.register(Registration.CHEMICAL_GUI_ELEMENT.get(), new ChemicalGuiElementBuilder());
    }

    @SubscribeEvent
    public static void registerGuiElementJeiRenderers(final RegisterGuiElementJEIRendererEvent event) {
        event.register(Registration.CHEMICAL_GUI_ELEMENT.get(), new ChemicalGuiElementJeiRenderer());
        event.register(Registration.HEAT_GUI_ELEMENT.get(), new HeatGuiElementJeiRenderer());
    }

    @SubscribeEvent
    public static void registerWidgetToJeiIngredientGetters(final RegisterWidgetToJeiIngredientGetterEvent event) {
        event.register(Registration.CHEMICAL_GUI_ELEMENT.get(), new ChemicalIngredientGetter());
    }

    private static class ChemicalIngredientGetter implements IngredientGetter<ChemicalGuiElement, ChemicalStack> {

        @Nullable
        @Override
        public IClickableIngredient<ChemicalStack> getIngredient(AbstractGuiElementWidget<ChemicalGuiElement> widget, double mouseX, double mouseY, IJeiHelpers helpers) {
            ChemicalMachineComponent component = widget.getScreen().getTile().getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get()).flatMap(handler -> handler.getComponentForID(widget.getElement().getComponentId())).orElse(null);
            if(component == null)
                return null;
            return helpers.getIngredientManager().createTypedIngredient(component.getStack()).map(ingredient ->
                new IClickableIngredient<ChemicalStack>() {
                    //Safe to remove
                    @SuppressWarnings("removal")
                    @Override
                    public ITypedIngredient<ChemicalStack> getTypedIngredient() {
                        return ingredient;
                    }

                    @Override
                    public ChemicalStack getIngredient() {
                        return ingredient.getIngredient();
                    }

                    @Override
                    public IIngredientType<ChemicalStack> getIngredientType() {
                        return MekanismJEI.TYPE_CHEMICAL;
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
