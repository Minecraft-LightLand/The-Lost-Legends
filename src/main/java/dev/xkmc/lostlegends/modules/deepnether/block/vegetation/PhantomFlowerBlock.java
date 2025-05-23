package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import dev.xkmc.lostlegends.modules.deepnether.util.SoulEffectsHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PhantomFlowerBlock extends BonemealableFlowerBlock {

	protected static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 13, 13);

	public PhantomFlowerBlock(Holder<MobEffect> eff, float time, Properties prop, ResourceKey<ConfiguredFeature<?, ?>> feature) {
		super(eff, time, prop, feature);
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {
		if (level.isClientSide || level.getDifficulty() == Difficulty.PEACEFUL) return;
		SoulEffectsHelper.deal(e);
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(BlockTags.BASE_STONE_NETHER) || super.mayPlaceOn(state, level, pos);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		Vec3 vec3 = state.getOffset(level, pos);
		return SHAPE.move(vec3.x, vec3.y, vec3.z);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		double x = pos.getX() + 0.55 - rand.nextFloat() * 0.1F;
		double y = pos.getY() + 0.9 - (rand.nextFloat() + rand.nextFloat()) * 0.4F;
		double z = pos.getZ() + 0.55 - rand.nextFloat() * 0.1F;
		if (rand.nextInt(5) == 0) {
			level.addParticle(
					ParticleTypes.SOUL_FIRE_FLAME, x, y, z,
					rand.nextGaussian() * 0.005,
					rand.nextGaussian() * 0.005,
					rand.nextGaussian() * 0.005
			);
		}
	}

}
