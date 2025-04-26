package dev.xkmc.lostlegends.modules.deepnether.block.surface;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.enums.BubbleColumnDirection;

public class WeepingSandBlock extends Block {
	public static final MapCodec<WeepingSandBlock> CODEC = simpleCodec(WeepingSandBlock::new);
	protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);

	@Override
	public MapCodec<WeepingSandBlock> codec() {
		return CODEC;
	}

	public WeepingSandBlock(BlockBehaviour.Properties prop) {
		super(prop);
	}

	@Override
	public BubbleColumnDirection getBubbleColumnDirection(BlockState state) {
		return BubbleColumnDirection.UPWARD;
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		BubbleColumnBlock.updateColumn(level, pos.above(), state);
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction dir, BlockState nState, LevelAccessor level, BlockPos pos, BlockPos nPos) {
		if (dir == Direction.UP && nState.is(Blocks.WATER)) {
			level.scheduleTick(pos, this, 20);
		}

		return super.updateShape(state, dir, nState, level, pos, nPos);
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState old, boolean flag) {
		level.scheduleTick(pos, this, 20);
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
		return Shapes.block();
	}

	@Override
	protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return Shapes.block();
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}

	@Override
	protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
		return 0.2F;
	}

}
