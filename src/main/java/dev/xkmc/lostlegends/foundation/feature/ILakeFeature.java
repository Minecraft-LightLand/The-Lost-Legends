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

		default BlockState surface() {
			return barrier();
		}

		int depth();

		int radius();

		default int margin() {
			return 1;
		}

	}

}
