package dev.xkmc.lostlegends.modules.deepnether.block;

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

	protected static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 15, 15);

	public BonePileBlock(Properties prop) {
		super(prop);
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
	protected VoxelShape getCollisionShape(BlockState p_56702_, BlockGetter p_56703_, BlockPos p_56704_, CollisionContext p_56705_) {
		return SHAPE;
	}

	@Override
	protected VoxelShape getBlockSupportShape(BlockState p_56707_, BlockGetter p_56708_, BlockPos p_56709_) {
		return Shapes.block();
	}

	@Override
	protected VoxelShape getVisualShape(BlockState p_56684_, BlockGetter p_56685_, BlockPos p_56686_, CollisionContext p_56687_) {
		return Shapes.block();
	}

}
