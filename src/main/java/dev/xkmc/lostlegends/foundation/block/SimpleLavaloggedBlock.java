package dev.xkmc.lostlegends.foundation.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;

public interface SimpleLavaloggedBlock extends SimpleFluidloggedBlock {

	BooleanProperty LAVALOGGED = BooleanProperty.create("lavalogged");

	@Override
	default BooleanProperty property() {
		return LAVALOGGED;
	}

	@Override
	default FlowingFluid fluid() {
		return Fluids.LAVA;
	}

	@Override
	default Block fluidBlock() {
		return Blocks.LAVA;
	}

}
