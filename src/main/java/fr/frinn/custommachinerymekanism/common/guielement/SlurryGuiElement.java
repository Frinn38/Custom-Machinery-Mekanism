package fr.frinn.custommachinerymekanism.common.guielement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.component.SlurryMachineComponent;

public class SlurryGuiElement extends ChemicalGuiElement<SlurryMachineComponent> {

    public static final NamedCodec<SlurryGuiElement> CODEC = makeCodec(SlurryGuiElement::new, "Slurry gui element");

    public SlurryGuiElement(Properties properties, boolean highlight) {
        super(properties, highlight);
    }

    @Override
    public MachineComponentType<SlurryMachineComponent> getComponentType() {
        return Registration.SLURRY_MACHINE_COMPONENT.get();
    }

    @Override
    public GuiElementType<SlurryGuiElement> getType() {
        return Registration.SLURRY_GUI_ELEMENT.get();
    }
}
