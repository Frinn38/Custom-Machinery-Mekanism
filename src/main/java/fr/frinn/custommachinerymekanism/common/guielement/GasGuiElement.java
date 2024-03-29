package fr.frinn.custommachinerymekanism.common.guielement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.component.GasMachineComponent;
import net.minecraft.resources.ResourceLocation;

public class GasGuiElement extends ChemicalGuiElement<GasMachineComponent> {

    public static final NamedCodec<GasGuiElement> CODEC = makeCodec(GasGuiElement::new, "Gas gui element");

    public GasGuiElement(Properties properties, String id, boolean highlight) {
        super(properties, id, highlight);
    }

    @Override
    public GuiElementType<GasGuiElement> getType() {
        return Registration.GAS_GUI_ELEMENT.get();
    }

    @Override
    public MachineComponentType<GasMachineComponent> getComponentType() {
        return Registration.GAS_MACHINE_COMPONENT.get();
    }
}
