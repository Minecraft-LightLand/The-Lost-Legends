package dev.xkmc.lostlegends.modules.deepnether.block.fluid;

import dev.xkmc.lostlegends.foundation.block.LLFlowingFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import java.util.Optional;

public abstract class MoltenGoldFluid extends BaseFlowingFluid implements LLFlowingFluid {

	protected MoltenGoldFluid(Properties properties) {
		super(properties);
	}

	@Override
	public void entityInside(Entity e) {
		e.lavaHurt();
	}

	@Override
	public boolean canStandOn(LivingEntity le) {
		return false;
	}

	@Override
	public void animateTick(Level level, BlockPos pos, FluidState state, RandomSource rand) {
		BlockPos up = pos.above();
		if (level.getBlockState(up).isAir() && !level.getBlockState(up).isSolidRender(level, up)) {
			if (rand.nextInt(100) == 0) {
				double x = (double) pos.getX() + rand.nextDouble();
				double y = (double) pos.getY() + 1.0;
				double z = (double) pos.getZ() + rand.nextDouble();
				level.playLocalSound(x, y, z, SoundEvents.LAVA_POP, SoundSource.BLOCKS,
						0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
			}
			if (rand.nextInt(200) == 0) {
				level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(),
						SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS,
						0.2F + rand.nextFloat() * 0.2F,
						0.9F + rand.nextFloat() * 0.15F,
						false
				);
			}
		}
	}

	@Override
	protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
		this.fizz(level, pos);
	}

	private void fizz(LevelAccessor level, BlockPos pos) {
		level.levelEvent(LevelEvent.LAVA_FIZZ, pos, 0);
	}

	@Override
	public Optional<SoundEvent> getPickupSound() {
		return Optional.of(SoundEvents.BUCKET_FILL_LAVA);
	}

	public static class Flowing extends MoltenGoldFluid {
		public Flowing(Properties properties) {
			super(properties);
		}

		@Override
		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

		@Override
		public int getAmount(FluidState state) {
			return state.getValue(LEVEL);
		}

		@Override
		public boolean isSource(FluidState state) {
			return false;
		}

	}

	public static class Source extends MoltenGoldFluid {

		public Source(Properties properties) {
			super(properties);
		}

		@Override
		public int getAmount(FluidState state) {
			return 8;
		}

		@Override
		public boolean isSource(FluidState state) {
			return true;
		}

	}

}
