package dev.xkmc.lostlegends.foundation.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.Shapes;

public abstract class FluidVineBody extends GrowingPlantBodyBlock implements SimpleFluidloggedBlock {

	public FluidVineBody(Properties prop) {
		super(prop, Direction.UP, Shapes.block(), true);
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
