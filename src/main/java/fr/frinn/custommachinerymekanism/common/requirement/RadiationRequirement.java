package fr.frinn.custommachinerymekanism.common.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.crafting.IRequirementList;
import fr.frinn.custommachinery.api.integration.jei.IDisplayInfo;
import fr.frinn.custommachinery.api.requirement.IRequirement;
import fr.frinn.custommachinery.api.requirement.RecipeRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinerymekanism.Registration;
import fr.frinn.custommachinerymekanism.common.component.RadiationMachineComponent;
import mekanism.common.config.MekanismConfig;
import mekanism.common.lib.radiation.RadiationManager.RadiationScale;
import mekanism.common.registries.MekanismItems;
import mekanism.common.util.UnitDisplayUtils;
import mekanism.common.util.UnitDisplayUtils.RadiationUnit;
import net.minecraft.network.chat.Component;

public class RadiationRequirement implements IRequirement<RadiationMachineComponent> {

    public static final NamedCodec<RadiationRequirement> CODEC = NamedCodec.record(radiationRequirementInstance ->
            radiationRequirementInstance.group(
                    RequirementIOMode.CODEC.fieldOf("mode").forGetter(RadiationRequirement::getMode),
                    NamedCodec.doubleRange(0.0D, Double.MAX_VALUE).fieldOf("amount").forGetter(requirement -> requirement.amount),
                    NamedCodec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("range", () -> MekanismConfig.general.radiationChunkCheckRadius.get() * 16).forGetter(requirement -> requirement.radius)
            ).apply(radiationRequirementInstance, RadiationRequirement::new), "Radiation requirement"
    );

    private final RequirementIOMode mode;
    private final double amount;
    private final int radius;

    public RadiationRequirement(RequirementIOMode mode, double amount, int radius) {
        this.mode = mode;
        this.amount = amount;
        this.radius = radius;
    }

    @Override
    public RequirementType<RadiationRequirement> getType() {
        return Registration.RADIATION_REQUIREMENT.get();
    }

    @Override
    public MachineComponentType<RadiationMachineComponent> getComponentType() {
        return Registration.RADIATION_MACHINE_COMPONENT.get();
    }

    @Override
    public RequirementIOMode getMode() {
        return this.mode;
    }

    @Override
    public boolean test(RadiationMachineComponent component, ICraftingContext context) {
        if(getMode() == RequirementIOMode.INPUT)
            return component.getRadiations() >= this.amount;
        return true;
    }

    @Override
    public void gatherRequirements(IRequirementList<RadiationMachineComponent> list) {
        if(this.mode == RequirementIOMode.INPUT)
            list.processOnStart(this::processInput);
        else
            list.processOnEnd(this::processOutput);
    }

    private CraftingResult processInput(RadiationMachineComponent component, ICraftingContext context) {
        double radiations = component.getRadiations();
        if(radiations < this.amount)
            return CraftingResult.error(Component.translatable("custommachinerymekanism.requirements.radiation.error", sievert(radiations), sievert(this.amount)));
        component.removeRadiations(this.amount, this.radius);
        return CraftingResult.success();
    }

    private CraftingResult processOutput(RadiationMachineComponent component, ICraftingContext context) {
        component.addRadiations(this.amount);
        return CraftingResult.success();
    }

    @Override
    public void getDefaultDisplayInfo(IDisplayInfo info, RecipeRequirement<?, ?> requirement) {
        if(getMode() == RequirementIOMode.INPUT) {
            info.setItemIcon(MekanismItems.GEIGER_COUNTER.asItem());
            info.addTooltip(Component.translatable("custommachinerymekanism.requirements.radiation.info.input", sievert(this.amount), this.radius));
        }
        else {
            info.setItemIcon(MekanismItems.GEIGER_COUNTER.asItem());
            info.addTooltip(Component.translatable("custommachinerymekanism.requirements.radiation.info.output", sievert(this.amount)));
        }
    }

    private static Component sievert(double amount) {
        return Component.literal("")
                .append(UnitDisplayUtils.getDisplayShort(amount, RadiationUnit.SV, 3))
                .withStyle(RadiationScale.getSeverityColor(amount).getColoredName().getStyle());
    }
}
