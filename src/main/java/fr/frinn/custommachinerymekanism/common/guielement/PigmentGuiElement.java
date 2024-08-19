package fr.frinn.custommachinerymekanism.common.guielement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.component.PigmentMachineComponent;

public class PigmentGuiElement extends ChemicalGuiElement<PigmentMachineComponent> {

    public static final NamedCodec<PigmentGuiElement> CODEC = makeCodec(PigmentGuiElement::new, "Pigment gui element");

    public PigmentGuiElement(Properties properties, boolean highlight) {
        super(properties, highlight);
    }

    @Override
    public MachineComponentType<PigmentMachineComponent> getComponentType() {
        return Registration.PIGMENT_MACHINE_COMPONENT.get();
    }

    @Override
    public GuiElementType<PigmentGuiElement> getType() {
        return Registration.PIGMENT_GUI_ELEMENT.get();
    }
}
