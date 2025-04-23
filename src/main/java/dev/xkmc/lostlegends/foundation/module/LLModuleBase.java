package dev.xkmc.lostlegends.foundation.module;

import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.FluidType;

public class LLModuleBase {

	public void commonInit() {
	}

	public void gatherData(GatherDataEvent event) {

	}

	public void registerFluidInteraction(
			FluidType a, FluidType b,
			BlockState aa, BlockState ab, BlockState bb
	) {
		FluidInteractionRegistry.addInteraction(a, new FluidInteractionRegistry.InteractionInformation(
				b, state -> state.isSource() ? aa : ab));
		FluidInteractionRegistry.addInteraction(b, new FluidInteractionRegistry.InteractionInformation(
				a, state -> state.isSource() ? bb : ab));
	}

}
