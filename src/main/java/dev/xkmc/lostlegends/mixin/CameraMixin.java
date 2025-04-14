package dev.xkmc.lostlegends.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.xkmc.lostlegends.foundation.fogblock.IFogBlock;
import net.minecraft.client.Camera;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {

	@Inject(method = "getFluidInCamera", cancellable = true, at = @At(value = "INVOKE", target =
			"Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
	public void lostlegends$blockFog(CallbackInfoReturnable<FogType> cir, @Local LocalRef<BlockState> state) {
		if (state.get().getBlock() instanceof IFogBlock fog){
			cir.setReturnValue(fog.getFogType());
		}
	}

}
