package fr.frinn.custommachinerymekanism.common.mixin;

import fr.frinn.custommachinery.common.init.CustomMachineBlock;
import fr.frinn.custommachinery.common.init.CustomMachineTile;
import fr.frinn.custommachinerymekanism.Registration;
import mekanism.common.lib.radiation.RadiationManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CustomMachineBlock.class)
public abstract class CustomMachineBlockMixin {

    @Inject(method = "onRemove", at = @At("HEAD"))
    private void custommachinerymekanism$onMachineRemoved(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston, CallbackInfo ci) {
        if(!state.is(newState.getBlock()) && level instanceof ServerLevel serverLevel && level.getBlockEntity(pos) instanceof CustomMachineTile machine) {
            machine.getComponentManager().getComponentHandler(Registration.CHEMICAL_MACHINE_COMPONENT.get())
                    .ifPresent(handler -> handler.getComponents()
                            .stream()
                            .filter(component -> component.emitRadiationsWhenBroken() && component.getStack().isRadioactive())
                            .forEach(component -> RadiationManager.get().dumpRadiation(GlobalPos.of(serverLevel.dimension(), pos), component.getStack()))
                    );
        }
    }
}
