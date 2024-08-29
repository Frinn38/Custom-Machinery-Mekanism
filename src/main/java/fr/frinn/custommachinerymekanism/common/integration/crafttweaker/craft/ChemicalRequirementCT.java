package fr.frinn.custommachinerymekanism.common.integration.crafttweaker.craft;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.integration.crafttweaker.CTConstants;
import fr.frinn.custommachinery.common.integration.crafttweaker.CustomCraftRecipeCTBuilder;
import fr.frinn.custommachinerymekanism.common.requirement.ChemicalPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.ChemicalRequirement;
import mekanism.common.integration.crafttweaker.chemical.ICrTChemicalStack;
import org.openzen.zencode.java.ZenCodeType.Expansion;
import org.openzen.zencode.java.ZenCodeType.Method;

@ZenRegister
@Expansion(CTConstants.RECIPE_BUILDER_CRAFT)
public class ChemicalRequirementCT {

    @Method
    public static CustomCraftRecipeCTBuilder requireChemical(CustomCraftRecipeCTBuilder builder, ICrTChemicalStack stack) {
        return requireChemical(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireChemical(CustomCraftRecipeCTBuilder builder, ICrTChemicalStack stack, String tank) {
        return builder.addRequirement(new ChemicalRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireChemicalPerTick(CustomCraftRecipeCTBuilder builder, ICrTChemicalStack stack) {
        return requireChemicalPerTick(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder requireChemicalPerTick(CustomCraftRecipeCTBuilder builder, ICrTChemicalStack stack, String tank) {
        return builder.addRequirement(new ChemicalPerTickRequirement(RequirementIOMode.INPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceChemical(CustomCraftRecipeCTBuilder builder, ICrTChemicalStack stack) {
        return produceChemical(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceChemical(CustomCraftRecipeCTBuilder builder, ICrTChemicalStack stack, String tank) {
        return builder.addRequirement(new ChemicalRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceChemicalPerTick(CustomCraftRecipeCTBuilder builder, ICrTChemicalStack stack) {
        return produceChemicalPerTick(builder, stack, "");
    }

    @Method
    public static CustomCraftRecipeCTBuilder produceChemicalPerTick(CustomCraftRecipeCTBuilder builder, ICrTChemicalStack stack, String tank) {
        return builder.addRequirement(new ChemicalPerTickRequirement(RequirementIOMode.OUTPUT, stack.getChemical(), stack.getAmount(), tank));
    }
}
