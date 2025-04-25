package dev.xkmc.lostlegends.modules.deepnether.block.surface;

import com.mojang.serialization.MapCodec;
import dev.xkmc.lostlegends.foundation.fogblock.FogConfig;
import dev.xkmc.lostlegends.foundation.fogblock.IFogBlock;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AshBlock extends Block implements IFogBlock {

	public static final MapCodec<AshBlock> CODEC = simpleCodec(AshBlock::new);
	private static final VoxelShape FALLING_COLLISION_SHAPE =
			Shapes.box(0, 0, 0, 1, 0.9F, 1);
	private static final FogConfig FOG = new FogConfig(FogConfig.Type.VIEWPORT,
			0.1f, 0.1f, 0.1f,
			0f, 2f, 2f, 24f, true);

	public MapCodec<AshBlock> codec() {
		return CODEC;
	}

	public AshBlock(BlockBehaviour.Properties prop) {
		super(prop);
	}

	protected boolean skipRendering(BlockState self, BlockState other, Direction dir) {
		return other.is(this) || super.skipRendering(self, other, dir);
	}

	protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
		return Shapes.empty();
	}

	@Override
	public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
		return isClear(entity);
	}

	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {
		if (e instanceof LivingEntity le && isClear(le)) return;
		if (!(e instanceof LivingEntity) || e.getInBlockState().is(this)) {
			e.makeStuckInBlock(state, new Vec3(0.9, 1.5, 0.9));
			if (e instanceof LivingEntity le && !level.isClientSide() && isInWall(le))
				le.hurt(le.damageSources().inWall(), 1.0F);
			// particle?
		}
	}

	public void fallOn(Level level, BlockState state, BlockPos pos, Entity e, float fallDist) {
		if (!(fallDist < 4) && e instanceof LivingEntity le) {
			LivingEntity.Fallsounds sound = le.getFallSounds();
			SoundEvent se = fallDist < 7 ? sound.small() : sound.big();
			e.playSound(se, 1, 1);
		}
	}

	protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		if (ctx instanceof EntityCollisionContext ecc) {
			Entity entity = ecc.getEntity();
			if (entity != null) {
				if (entity.fallDistance > 2.5F)
					return FALLING_COLLISION_SHAPE;
				if (entity instanceof FallingBlockEntity)
					return Shapes.block();
				if (entity instanceof LivingEntity le && isClear(le) && !ctx.isDescending() &&
						ctx.isAbove(Shapes.block(), pos, true) &&
						!level.getBlockState(pos.above()).is(this))
					return Shapes.block();
			}
		}
		return Shapes.empty();
	}

	protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ct) {
		return Shapes.empty();
	}

	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return true;
	}

	@Override
	public FogConfig getFogConfig() {
		return FOG;
	}

	@Override
	public boolean isClear(LivingEntity le) {
		return le.hasEffect(DeepNether.EFFECTS.ASH_BOUND);
	}

	private boolean isInWall(LivingEntity le) {
		if (le.noPhysics) {
			return false;
		}
		float w = le.getBbWidth() * 0.8F;
		AABB aabb = AABB.ofSize(le.getEyePosition(), w, 1.0E-6, w);
		return BlockPos.betweenClosedStream(aabb)
				.anyMatch(pos -> le.level().getBlockState(pos).is(this));
	}

}
