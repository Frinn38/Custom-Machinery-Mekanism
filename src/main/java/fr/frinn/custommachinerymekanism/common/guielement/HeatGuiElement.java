package fr.frinn.custommachinerymekanism.common.guielement;

import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.guielement.IComponentGuiElement;
import fr.frinn.custommachinery.impl.guielement.AbstractTexturedGuiElement;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.component.HeatMachineComponent;
import net.minecraft.resources.ResourceLocation;

public class HeatGuiElement extends AbstractTexturedGuiElement implements IComponentGuiElement<HeatMachineComponent> {

    public static final ResourceLocation BASE_TEXTURE = ICustomMachineryAPI.INSTANCE.rl("textures/gui/base_fluid_storage.png");

    public static final NamedCodec<HeatGuiElement> CODEC = NamedCodec.record(instance ->
            instance.group(
                    makePropertiesCodec(BASE_TEXTURE).forGetter(HeatGuiElement::getProperties),
                    NamedCodec.BOOL.optionalFieldOf("highlight", true).forGetter(HeatGuiElement::highlight)
            ).apply(instance, HeatGuiElement::new), "Heat gui element"
    );

    private final boolean highlight;

    public HeatGuiElement(Properties properties, boolean highlight) {
        super(properties);
        this.highlight = highlight;
    }

    public boolean highlight() {
        return this.highlight;
    }

    @Override
    public GuiElementType<HeatGuiElement> getType() {
        return Registration.HEAT_GUI_ELEMENT.get();
    }

    @Override
    public MachineComponentType<HeatMachineComponent> getComponentType() {
        return Registration.HEAT_MACHINE_COMPONENT.get();
    }

    @Override
    public String getComponentId() {
        return "Heat";
    }
}
