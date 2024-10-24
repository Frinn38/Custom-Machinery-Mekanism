package fr.frinn.custommachinerymekanism.common.mixin;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.common.integration.kubejs.CustomCraftRecipeBuilderJS;
import fr.frinn.custommachinery.common.integration.kubejs.CustomMachineRecipeBuilderJS;
import fr.frinn.custommachinerymekanism.common.integration.kubejs.ChemicalRequirementJS;
import fr.frinn.custommachinerymekanism.common.integration.kubejs.HeatRequirementJS;
import fr.frinn.custommachinerymekanism.common.integration.kubejs.RadiationRequirementJS;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({CustomMachineRecipeBuilderJS.class, CustomCraftRecipeBuilderJS.class})
public abstract class KubeJSIntegrationMixin implements RecipeJSBuilder,
        ChemicalRequirementJS, HeatRequirementJS, RadiationRequirementJS {

}
