package dev.xkmc.lostlegends.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import javax.annotation.Nullable;
import java.util.Optional;

public interface SimpleFluidloggedBlock extends BucketPickup, LiquidBlockContainer {

	BooleanProperty property();

	FlowingFluid fluid();

	Block fluidBlock();

	@Override
	default boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
		return fluid == fluid();
	}

	@Override
	default boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluid) {
		if (!state.getValue(property()) && fluid.getType() == fluid()) {
			if (!level.isClientSide()) {
				level.setBlock(pos, state.setValue(property(), true), 3);
				level.scheduleTick(pos, fluid.getType(), fluid.getType().getTickDelay(level));
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	default ItemStack pickupBlock(@Nullable Player player, LevelAccessor level, BlockPos pos, BlockState state) {
		if (state.getValue(property())) {
			level.setBlock(pos, state.setValue(property(), false), 3);
			if (!state.canSurvive(level, pos)) {
				level.destroyBlock(pos, true);
			}
			return new ItemStack(fluid().getBucket());
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	default Optional<SoundEvent> getPickupSound() {
		return fluid().getPickupSound();
	}

}
