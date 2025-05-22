package dev.xkmc.lostlegends.foundation.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;

public abstract class FluidVineBody extends GrowingPlantBodyBlock implements SimpleFluidloggedBlock {

	public FluidVineBody(Properties prop, int w, Direction dir) {
		super(prop, dir, Block.box(w, 0, w, 16 - w, 16, 16 - w), true);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(property());
	}

	protected BlockState updateHeadAfterConvertedFromBody(BlockState body, BlockState head) {
		return head.setValue(property(), body.getValue(property()));
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.getValue(property()) ? fluid().getSource(false) : super.getFluidState(state);
	}

}
