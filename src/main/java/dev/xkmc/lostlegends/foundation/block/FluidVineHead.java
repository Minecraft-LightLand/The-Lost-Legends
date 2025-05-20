package dev.xkmc.lostlegends.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

import javax.annotation.Nullable;

public abstract class FluidVineHead extends GrowingPlantHeadBlock implements SimpleFluidloggedBlock {

	protected static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 9, 16);
	private static final double growPerTickProbability = 0.14;

	public FluidVineHead(Properties prop, Direction dir) {
		super(prop, dir, SHAPE, true, growPerTickProbability);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(property());
	}

	@Override
	protected boolean canGrowInto(BlockState state) {
		return state.is(fluidBlock()) || state.isAir();
	}

	@Override
	protected int getBlocksToGrowWhenBonemealed(RandomSource rand) {
		return 1;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
		var ans = super.getStateForPlacement(ctx);
		return ans == null ? null : ans.setValue(property(), fluidstate.getType().isSame(fluid()) && fluidstate.getAmount() == 8);
	}

	protected BlockState updateBodyAfterConvertedFromHead(BlockState head, BlockState body) {
		return body.setValue(property(), head.getValue(property()));
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		if (state.getValue(AGE) >= 25) return;
		BlockPos up = pos.relative(this.growthDirection);
		BlockState upState = level.getBlockState(up);
		if (this.canGrowInto(upState)) {
			if (CommonHooks.canCropGrow(level, up, state, rand.nextDouble() < growPerTickProbability)) {
				level.setBlockAndUpdate(up, getGrowIntoState(state, upState, level.random));
				CommonHooks.fireCropGrowPost(level, up, upState);
			}
		}
	}

	protected BlockState getGrowIntoState(BlockState state, BlockState upState, RandomSource rand) {
		boolean lava = upState.is(fluidBlock());
		return state.cycle(AGE).setValue(property(), lava);
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.getValue(property()) ? fluid().getSource(false) : super.getFluidState(state);
	}

	public abstract Block getBodyBlock();

}
