package fr.frinn.custommachinerymekanism.common.guielement;

import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.guielement.IComponentGuiElement;
import fr.frinn.custommachinery.impl.guielement.AbstractTexturedGuiElement;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.component.ChemicalMachineComponent;
import net.minecraft.resources.ResourceLocation;

public class ChemicalGuiElement extends AbstractTexturedGuiElement implements IComponentGuiElement<ChemicalMachineComponent> {

    public static final ResourceLocation BASE_TEXTURE = ICustomMachineryAPI.INSTANCE.rl("textures/gui/base_fluid_storage.png");

    public static NamedCodec<ChemicalGuiElement> CODEC = NamedCodec.record(chemicalGuiElementInstance ->
            chemicalGuiElementInstance.group(
                    makePropertiesCodec(BASE_TEXTURE).forGetter(ChemicalGuiElement::getProperties),
                    NamedCodec.BOOL.optionalFieldOf("highlight", true).forGetter(ChemicalGuiElement::highlight)
            ).apply(chemicalGuiElementInstance, ChemicalGuiElement::new), "Chemical gui element"
    );

    private final boolean highlight;

    public ChemicalGuiElement(Properties properties, boolean highlight) {
        super(properties);
        this.highlight = highlight;
    }

    @Override
    public GuiElementType<ChemicalGuiElement> getType() {
        return Registration.CHEMICAL_GUI_ELEMENT.get();
    }

    @Override
    public MachineComponentType<ChemicalMachineComponent> getComponentType() {
        return Registration.CHEMICAL_MACHINE_COMPONENT.get();
    }

    @Override
    public String getComponentId() {
        return this.getId();
    }

    public boolean highlight() {
        return this.highlight;
    }
}
