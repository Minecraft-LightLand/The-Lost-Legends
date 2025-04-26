package dev.xkmc.lostlegends.foundation.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface ILakeFeature {

	void setAir(WorldGenLevel level, BlockPos pos);

	void setFluid(WorldGenLevel level, BlockPos pos, BlockState fluid);

	void setBarrier(WorldGenLevel level, BlockPos pos, BlockState barrier);

	interface Data {

		BlockState fluid();

		BlockState barrier();

		int depth();

		int radius();

	}

}
