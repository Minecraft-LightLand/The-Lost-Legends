package dev.xkmc.lostlegends.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class LLFluidBlock extends LiquidBlock {

	public LLFluidBlock(FlowingFluid fluid, Properties prop) {
		super(fluid, prop);
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {
		if (fluid instanceof LLFlowingFluid ins && e.isInFluidType(fluid.getFluidType())) {
			ins.entityInside(e);
		}
	}
}
