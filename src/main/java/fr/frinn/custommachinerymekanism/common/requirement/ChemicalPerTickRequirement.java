package fr.frinn.custommachinerymekanism.common.requirement;

import fr.frinn.custommachinery.api.crafting.IRequirementList;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinerymekanism.common.component.handler.ChemicalComponentHandler;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;

public abstract class ChemicalPerTickRequirement<C extends Chemical<C>, S extends ChemicalStack<C>, T extends ChemicalComponentHandler<C, S, ?, ?>> extends ChemicalRequirement<C, S, T> {

    public ChemicalPerTickRequirement(RequirementIOMode mode, C chemical, long amount, String tank) {
        super(mode, chemical, amount, tank);
    }

    @Override
    public void gatherRequirements(IRequirementList<T> list) {
        if(this.mode == RequirementIOMode.INPUT)
            list.processEachTick(this::processInput);
        else
            list.processEachTick(this::processOutput);
    }
}
