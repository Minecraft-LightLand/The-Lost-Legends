package dev.xkmc.lostlegends.modules.deepnether.block.fluid;

import dev.xkmc.lostlegends.foundation.block.SimpleFluidloggedBlock;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FlowingFluid;

public interface SimpleSoulLoggedBlock extends SimpleFluidloggedBlock {

	BooleanProperty LOGGED = BooleanProperty.create("soullogged");

	@Override
	default BooleanProperty property() {
		return LOGGED;
	}

	@Override
	default FlowingFluid fluid() {
		return DeepNether.BLOCKS.LIQUID_SOUL.getSource();
	}

	@Override
	default Block fluidBlock() {
		return fluid().defaultFluidState().createLegacyBlock().getBlock();
	}

}
