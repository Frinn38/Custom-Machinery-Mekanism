package fr.frinn.custommachinerymekanism.client.jei.heat;

import fr.frinn.custommachinerymekanism.CustomMachineryMekanism;
import fr.frinn.custommachinerymekanism.client.jei.CMMJeiPlugin;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class HeatIngredientHelper implements IIngredientHelper<Heat> {

    @Override
    public IIngredientType<Heat> getIngredientType() {
        return CMMJeiPlugin.HEAT_INGREDIENT;
    }

    @Override
    public String getDisplayName(Heat ingredient) {
        return Component.translatable("custommachinerymekanism.jei.ingredient.heat", ingredient.amount()).getString();
    }

    //Safe to remove
    @SuppressWarnings("removal")
    @Override
    public String getUniqueId(Heat ingredient, UidContext context) {
        return "" + ingredient.amount() + ingredient.chance() + ingredient.isPerTick();
    }

    @Override
    public Object getUid(Heat ingredient, UidContext context) {
        return "" + ingredient.amount() + ingredient.chance() + ingredient.isPerTick();
    }

    @Override
    public ResourceLocation getResourceLocation(Heat ingredient) {
        return CustomMachineryMekanism.rl("heat");
    }

    @Override
    public Heat copyIngredient(Heat ingredient) {
        return new Heat(ingredient.amount(), ingredient.chance(), ingredient.isPerTick(), ingredient.mode());
    }

    @Override
    public String getErrorInfo(@Nullable Heat ingredient) {
        return "";
    }
}
