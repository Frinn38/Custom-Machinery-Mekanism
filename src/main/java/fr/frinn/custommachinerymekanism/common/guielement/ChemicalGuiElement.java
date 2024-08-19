package fr.frinn.custommachinerymekanism.common.guielement;

import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.guielement.IComponentGuiElement;
import fr.frinn.custommachinery.impl.guielement.AbstractTexturedGuiElement;
import fr.frinn.custommachinerymekanism.common.component.ChemicalMachineComponent;
import net.minecraft.resources.ResourceLocation;

public abstract class ChemicalGuiElement<C extends ChemicalMachineComponent<?, ?>> extends AbstractTexturedGuiElement implements IComponentGuiElement<C> {

    public static final ResourceLocation BASE_TEXTURE = ICustomMachineryAPI.INSTANCE.rl("textures/gui/base_fluid_storage.png");

    public static <E extends ChemicalGuiElement<?>> NamedCodec<E> makeCodec(Builder<E> builder, String name) {
        return NamedCodec.record(elementInstance ->
                elementInstance.group(
                        makePropertiesCodec(BASE_TEXTURE).forGetter(ChemicalGuiElement::getProperties),
                        NamedCodec.BOOL.optionalFieldOf("highlight", true).forGetter(ChemicalGuiElement::highlight)
                ).apply(elementInstance, builder::build), name
        );
    }

    private final boolean highlight;

    public ChemicalGuiElement(Properties properties, boolean highlight) {
        super(properties);
        this.highlight = highlight;
    }

    @Override
    public String getComponentId() {
        return this.getId();
    }

    public boolean highlight() {
        return this.highlight;
    }

    public interface Builder<E extends ChemicalGuiElement<?>> {
        E build(Properties properties, boolean highlight);
    }
}
