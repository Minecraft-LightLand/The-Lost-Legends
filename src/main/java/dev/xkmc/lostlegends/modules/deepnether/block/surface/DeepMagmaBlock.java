package dev.xkmc.lostlegends.modules.deepnether.block.surface;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.enums.BubbleColumnDirection;

public class DeepMagmaBlock extends Block {

	public DeepMagmaBlock(Properties prop) {
		super(prop);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity e) {
		if (e instanceof LivingEntity) {
			e.hurt(level.damageSources().hotFloor(), 2f);
		}
	}

	@Override
	public BubbleColumnDirection getBubbleColumnDirection(BlockState state) {
		return BubbleColumnDirection.DOWNWARD;
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

}
