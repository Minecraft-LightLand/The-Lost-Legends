package dev.xkmc.lostlegends.foundation.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.jetbrains.annotations.Nullable;

public abstract class OnGroundFeature<T extends FeatureConfiguration> extends Feature<T> {

	public OnGroundFeature(Codec<T> codec) {
		super(codec);
	}

	protected boolean isEmpty(LevelAccessor level, BlockPos pos, T data) {
		return level.isEmptyBlock(pos);
	}

	protected @Nullable BlockPos findValid(LevelAccessor level, T data, BlockPos origin, int maxStep) {
		var pos = new BlockPos.MutableBlockPos();
		pos.set(origin);
		while (isEmpty(level, pos, data)) {
			pos.move(0, -1, 0);
			maxStep--;
			if (maxStep < 0) return null;
			if (level.isOutsideBuildHeight(pos)) return null;
		}
		maxStep++;
		while (!isEmpty(level, pos, data)) {
			pos.move(0, 1, 0);
			maxStep--;
			if (maxStep < 0) return null;
			if (level.isOutsideBuildHeight(pos)) return null;
		}
		return pos;
	}

}
