package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import com.mojang.serialization.MapCodec;
import dev.xkmc.lostlegends.foundation.block.SimpleLavaloggedBlock;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BoneVineHead extends GrowingPlantHeadBlock implements SimpleLavaloggedBlock {
	public static final MapCodec<BoneVineHead> CODEC = simpleCodec(BoneVineHead::new);
	protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0);

	@Override
	public MapCodec<BoneVineHead> codec() {
		return CODEC;
	}

	public BoneVineHead(BlockBehaviour.Properties prop) {
		super(prop, Direction.UP, SHAPE, true, 0.14);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LAVALOGGED);
	}

	@Override
	protected boolean canGrowInto(BlockState state) {
		return state.is(Blocks.LAVA) || state.isAir();
	}

	@Override
	protected Block getBodyBlock() {
		return DeepNether.BLOCKS.SCORCHED_BONE_VINE_PLANT.get();
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
		return ans == null ? null : ans.setValue(LAVALOGGED, fluidstate.is(FluidTags.LAVA) && fluidstate.getAmount() == 8);
	}

	protected BlockState updateBodyAfterConvertedFromHead(BlockState head, BlockState body) {
		return body.setValue(LAVALOGGED, head.getValue(LAVALOGGED));
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.getValue(LAVALOGGED) ? Fluids.LAVA.getSource(false) : super.getFluidState(state);
	}

}
