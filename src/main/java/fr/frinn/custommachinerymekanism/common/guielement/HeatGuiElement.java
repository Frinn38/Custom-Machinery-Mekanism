package fr.frinn.custommachinerymekanism.common.guielement;

import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.guielement.IComponentGuiElement;
import fr.frinn.custommachinery.impl.guielement.AbstractTexturedGuiElement;
import fr.frinn.custommachinery.impl.util.TextureInfo;
import fr.frinn.custommachinerymekanism.CustomMachineryMekanism;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.component.HeatMachineComponent;

public class HeatGuiElement extends AbstractTexturedGuiElement implements IComponentGuiElement<HeatMachineComponent> {

    public static final TextureInfo BASE_TEXTURE = new TextureInfo(ICustomMachineryAPI.INSTANCE.rl("textures/gui/base_fluid_storage.png"));
    public static final TextureInfo BASE_TEXTURE_FILLED = new TextureInfo(CustomMachineryMekanism.rl("textures/gui/base_heat_storage_filled.png"));

    public static final NamedCodec<HeatGuiElement> CODEC = NamedCodec.record(instance ->
            instance.group(
                    makePropertiesCodec().forGetter(HeatGuiElement::getProperties),
                    TextureInfo.CODEC.optionalFieldOf("texture_empty", BASE_TEXTURE).forGetter(HeatGuiElement::getEmptyTexture),
                    TextureInfo.CODEC.optionalFieldOf("texture_filled", BASE_TEXTURE_FILLED).forGetter(HeatGuiElement::getFilledTexture),
                    NamedCodec.BOOL.optionalFieldOf("highlight", true).forGetter(HeatGuiElement::highlight)
            ).apply(instance, HeatGuiElement::new), "Heat gui element"
    );

    private final TextureInfo emptyTexture;
    private final TextureInfo filledTexture;
    private final boolean highlight;

    public HeatGuiElement(Properties properties, TextureInfo emptyTexture, TextureInfo filledTexture, boolean highlight) {
        super(properties, emptyTexture);
        this.emptyTexture = emptyTexture;
        this.filledTexture = filledTexture;
        this.highlight = highlight;
    }

    public TextureInfo getEmptyTexture() {
        return this.emptyTexture;
    }

    public TextureInfo getFilledTexture() {
        return this.filledTexture;
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
