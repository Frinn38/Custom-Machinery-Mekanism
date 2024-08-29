package fr.frinn.custommachinerymekanism;

import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.network.DataType;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinerymekanism.common.component.ChemicalMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.HeatMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.RadiationMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.handler.ChemicalComponentHandler;
import fr.frinn.custommachinerymekanism.common.guielement.ChemicalGuiElement;
import fr.frinn.custommachinerymekanism.common.guielement.HeatGuiElement;
import fr.frinn.custommachinerymekanism.common.network.data.ChemicalStackData;
import fr.frinn.custommachinerymekanism.common.network.syncable.ChemicalStackSyncable;
import fr.frinn.custommachinerymekanism.common.requirement.ChemicalPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.ChemicalRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.HeatPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.HeatRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.RadiationPerTickRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.RadiationRequirement;
import fr.frinn.custommachinerymekanism.common.requirement.TemperatureRequirement;
import mekanism.api.chemical.ChemicalStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class Registration {

    public static final DeferredRegister<MachineComponentType<?>> MACHINE_COMPONENTS = DeferredRegister.create(MachineComponentType.REGISTRY_KEY, ICustomMachineryAPI.INSTANCE.modid());
    public static final DeferredRegister<GuiElementType<?>> GUI_ELEMENTS = DeferredRegister.create(GuiElementType.REGISTRY_KEY, ICustomMachineryAPI.INSTANCE.modid());
    public static final DeferredRegister<RequirementType<?>> REQUIREMENTS = DeferredRegister.create(RequirementType.REGISTRY_KEY, ICustomMachineryAPI.INSTANCE.modid());
    public static final DeferredRegister<DataType<?, ?>> DATAS = DeferredRegister.create(DataType.REGISTRY_KEY, ICustomMachineryAPI.INSTANCE.modid());

    public static final Supplier<MachineComponentType<ChemicalMachineComponent>> CHEMICAL_MACHINE_COMPONENT = MACHINE_COMPONENTS.register("chemical", () -> MachineComponentType.create(ChemicalMachineComponent.Template.CODEC).setNotSingle(ChemicalComponentHandler::new));
    public static final Supplier<MachineComponentType<HeatMachineComponent>> HEAT_MACHINE_COMPONENT = MACHINE_COMPONENTS.register("heat", () -> MachineComponentType.create(HeatMachineComponent.Template.CODEC));
    public static final Supplier<MachineComponentType<RadiationMachineComponent>> RADIATION_MACHINE_COMPONENT = MACHINE_COMPONENTS.register("radiation", () -> MachineComponentType.create(RadiationMachineComponent::new));

    public static final Supplier<GuiElementType<ChemicalGuiElement>> CHEMICAL_GUI_ELEMENT = GUI_ELEMENTS.register("chemical", () -> GuiElementType.create(ChemicalGuiElement.CODEC));
    public static final Supplier<GuiElementType<HeatGuiElement>> HEAT_GUI_ELEMENT = GUI_ELEMENTS.register("heat", () -> GuiElementType.create(HeatGuiElement.CODEC));

    public static final Supplier<RequirementType<ChemicalRequirement>> CHEMICAL_REQUIREMENT = REQUIREMENTS.register("chemical", () -> RequirementType.inventory(ChemicalRequirement.CODEC));
    public static final Supplier<RequirementType<ChemicalPerTickRequirement>> CHEMICAL_PER_TICK_REQUIREMENT = REQUIREMENTS.register("chemical_per_tick", () -> RequirementType.inventory(ChemicalPerTickRequirement.CODEC));
    public static final Supplier<RequirementType<HeatRequirement>> HEAT_REQUIREMENT = REQUIREMENTS.register("heat", () -> RequirementType.inventory(HeatRequirement.CODEC));
    public static final Supplier<RequirementType<HeatPerTickRequirement>> HEAT_PER_TICK_REQUIREMENT = REQUIREMENTS.register("heat_per_tick", () -> RequirementType.inventory(HeatPerTickRequirement.CODEC));
    public static final Supplier<RequirementType<TemperatureRequirement>> TEMPERATURE_REQUIREMENT = REQUIREMENTS.register("temperature", () -> RequirementType.inventory(TemperatureRequirement.CODEC));
    public static final Supplier<RequirementType<RadiationRequirement>> RADIATION_REQUIREMENT = REQUIREMENTS.register("radiation", () -> RequirementType.world(RadiationRequirement.CODEC));
    public static final Supplier<RequirementType<RadiationPerTickRequirement>> RADIATION_PER_TICK = REQUIREMENTS.register("radiation_per_tick", () -> RequirementType.world(RadiationPerTickRequirement.CODEC));

    public static final Supplier<DataType<ChemicalStackData, ChemicalStack>> CHEMICAL_DATA = DATAS.register("chemical", () -> DataType.create(ChemicalStack.class, ChemicalStackSyncable::create, ChemicalStackData::new));
}
