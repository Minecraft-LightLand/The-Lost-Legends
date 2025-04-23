package dev.xkmc.lostlegends.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import java.util.Optional;

public interface SimpleLavaloggedBlock extends BucketPickup, LiquidBlockContainer {

	BooleanProperty LAVALOGGED = BooleanProperty.create("lavalogged");

	@Override
	default boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
		return fluid == Fluids.LAVA;
	}

	@Override
	default boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluid) {
		if (!state.getValue(LAVALOGGED) && fluid.getType() == Fluids.LAVA) {
			if (!level.isClientSide()) {
				level.setBlock(pos, state.setValue(LAVALOGGED, true), 3);
				level.scheduleTick(pos, fluid.getType(), fluid.getType().getTickDelay(level));
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	default ItemStack pickupBlock(@Nullable Player player, LevelAccessor level, BlockPos pos, BlockState state) {
		if (state.getValue(LAVALOGGED)) {
			level.setBlock(pos, state.setValue(LAVALOGGED, false), 3);
			if (!state.canSurvive(level, pos)) {
				level.destroyBlock(pos, true);
			}
			return new ItemStack(Items.LAVA_BUCKET);
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	default Optional<SoundEvent> getPickupSound() {
		return Fluids.LAVA.getPickupSound();
	}
}
