package dev.xkmc.lostlegends.foundation.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.jetbrains.annotations.Nullable;

public abstract class IslandFeature<T extends FeatureConfiguration> extends Feature<T> {

	public IslandFeature(Codec<T> codec) {
		super(codec);
	}

	public @Nullable BlockPos checkHeightAndRadius(
			LevelAccessor level, BlockPos origin, IslandData data,
			int radius, double allowance, int maxStep
	) {
		BlockPos candidate = findValidHeight(level, origin, data.height(), data.clearance(), maxStep);
		if (candidate == null) return null;
		int minY = (int) (data.height() * allowance);
		int maxY = (int) (data.clearance() * allowance);
		var ipos = new BlockPos.MutableBlockPos();
		for (int i = 0; i < radius * 2; i++) {
			float a = i * Mth.PI / radius;
			BlockPos rim = candidate.offset((int) (Mth.sin(a) * radius), 0, (int) (Mth.cos(a) * radius));
			for (int dy = -minY; dy <= maxY; dy++) {
				ipos.setWithOffset(rim, 0, dy, 0);
				if (!level.isEmptyBlock(ipos)) {
					return null;
				}
			}
		}
		return candidate;
	}

	public @Nullable BlockPos findValidHeight(LevelAccessor level, BlockPos origin, int minHeight, int minClear, int maxStep) {
		var pos = new BlockPos.MutableBlockPos();
		pos.set(origin);
		while (!level.isEmptyBlock(pos)) {
			pos.move(0, 1, 0);
			maxStep--;
			if (maxStep < 0) return null;
			if (level.isOutsideBuildHeight(pos)) return null;
		}
		var current = pos.immutable();
		int height = 0;
		for (int i = 0; i < minHeight + minClear + 1; i++) {
			pos.move(0, -1, 0);
			if (!level.isEmptyBlock(pos))
				break;
			height++;
		}
		pos.set(current);
		if (height >= minHeight) {
			int clear = 0;
			for (int i = 0; i < minClear; i++) {
				pos.move(0, 1, 0);
				if (!level.isEmptyBlock(pos)) {
					break;
				}
				clear++;
			}
			if (clear >= minClear)
				return current;
			if (height + clear >= minHeight + minClear) {
				return current.offset(0, clear - minClear, 0);
			}
			return null;
		}
		for (int i = 0; i < minHeight - height + minClear; i++) {
			pos.move(0, 1, 0);
			if (!level.isEmptyBlock(pos)) {
				return null;
			}
		}
		return current.offset(0, minHeight - height, 0);
	}

	public interface IslandData {

		int height();

		int clearance();

	}

}
