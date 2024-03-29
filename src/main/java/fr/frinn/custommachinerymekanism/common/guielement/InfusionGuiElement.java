package fr.frinn.custommachinerymekanism.common.guielement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.component.InfusionMachineComponent;
import net.minecraft.resources.ResourceLocation;

public class InfusionGuiElement extends ChemicalGuiElement<InfusionMachineComponent> {

    public static final NamedCodec<InfusionGuiElement> CODEC = makeCodec(InfusionGuiElement::new, "Infusion gui element");

    public InfusionGuiElement(Properties properties, String id, boolean highlight) {
        super(properties, id, highlight);
    }

    @Override
    public MachineComponentType<InfusionMachineComponent> getComponentType() {
        return Registration.INFUSION_MACHINE_COMPONENT.get();
    }

    @Override
    public GuiElementType<InfusionGuiElement> getType() {
        return Registration.INFUSION_GUI_ELEMENT.get();
    }
}
