package dev.xkmc.lostlegends.modules.deepnether.entity.ghost.base;

import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.neoforged.neoforge.common.NeoForgeMod;

public class GhostNodeEvaluator extends WalkNodeEvaluator {

	private boolean isWalkableLiquid(FluidState state) {
		return state.getFluidType() == NeoForgeMod.LAVA_TYPE.value() ||
				state.getFluidType() == DeepNether.BLOCKS.LIQUID_SOUL.getType();
	}

	protected double getFloorLevel(BlockPos pos) {
		BlockGetter level = this.currentContext.level();
		if (isWalkableLiquid(level.getFluidState(pos))) {
			return pos.getY() + 0.5;
		}
		return super.getFloorLevel(pos);
	}

}
