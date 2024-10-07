package fr.frinn.custommachinerymekanism.common.transfer;

import fr.frinn.custommachinery.impl.component.config.IOSideMode;
import fr.frinn.custommachinerymekanism.common.component.ChemicalMachineComponent;
import fr.frinn.custommachinerymekanism.common.component.handler.ChemicalComponentHandler;
import mekanism.api.Action;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public class SidedChemicalTank implements IChemicalHandler {

    private final ChemicalComponentHandler handler;
    @Nullable
    private final Direction side;

    public SidedChemicalTank(ChemicalComponentHandler handler, @Nullable Direction side) {
        this.handler = handler;
        this.side = side;
    }

    @Override
    public int getChemicalTanks() {
        return this.handler.getComponents().size();
    }

    @Override
    public ChemicalStack getChemicalInTank(int i) {
        return this.handler.getComponents().get(i).getStack();
    }

    @Override
    public void setChemicalInTank(int i, ChemicalStack stack) {
        this.handler.getComponents().get(i).setStack(stack);
    }

    @Override
    public long getChemicalTankCapacity(int i) {
        return this.handler.getComponents().get(i).getCapacity();
    }

    @Override
    public boolean isValid(int i, ChemicalStack stack) {
        return this.handler.getComponents().get(i).isValid(stack);
    }

    @Override
    public ChemicalStack insertChemical(int i, ChemicalStack stack, Action action) {
        ChemicalMachineComponent component = this.handler.getComponents().get(i);
        if(!getMode(component).isInput())
            return stack;
        return component.insert(stack, action, false);
    }

    @Override
    public ChemicalStack extractChemical(int i, long amount, Action action) {
        ChemicalMachineComponent component = this.handler.getComponents().get(i);
        if(!getMode(component).isOutput())
            return ChemicalStack.EMPTY;
        return component.extract(amount, action, false);
    }

    private IOSideMode getMode(ChemicalMachineComponent component) {
        if(this.side == null)
            return IOSideMode.BOTH;
        return component.getConfig().getSideMode(this.side);
    }
}
