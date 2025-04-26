package dev.xkmc.lostlegends.modules.deepnether.block.surface;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BonePileBlock extends Block {

	protected static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 16, 15);

	public BonePileBlock(Properties prop) {
		super(prop);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity e) {
		if (e instanceof LivingEntity le) {
			le.igniteForTicks(4);
			le.hurt(le.damageSources().hotFloor(), 1);
		}
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {

		if (e instanceof LivingEntity le) {
			le.inBlockState = state;
			le.igniteForTicks(4);
			le.hurt(le.damageSources().hotFloor(), 1);
		}
		super.entityInside(state, level, pos, e);
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

}
