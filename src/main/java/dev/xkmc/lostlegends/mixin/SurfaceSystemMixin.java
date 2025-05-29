package dev.xkmc.lostlegends.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.lostlegends.foundation.dimension.SurfaceVariantData;
import dev.xkmc.lostlegends.foundation.dimension.VariantSurface;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BlockColumn;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SurfaceSystem.class)
public class SurfaceSystemMixin implements VariantSurface {

	@Unique
	private SurfaceVariantData lostlegends$data;

	@Override
	public void lostlegends$setVariantData(SurfaceVariantData data) {
		this.lostlegends$data = data;
	}

	@WrapOperation(method = "buildSurface", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/BlockColumn;getBlock(I)Lnet/minecraft/world/level/block/state/BlockState;"))
	public BlockState lostlegends$getBlock(BlockColumn ins, int y, Operation<BlockState> original) {
		BlockState ans = original.call(ins, y);
		if (lostlegends$data == null) return ans;
		if (lostlegends$data.variants().contains(ans.getBlock())) {
			return lostlegends$data.target();
		}
		return ans;
	}

}
