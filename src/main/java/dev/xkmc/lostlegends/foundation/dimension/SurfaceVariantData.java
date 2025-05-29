package dev.xkmc.lostlegends.foundation.dimension;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public record SurfaceVariantData(
		BlockState target,
		Set<Block> variants
) {
}
