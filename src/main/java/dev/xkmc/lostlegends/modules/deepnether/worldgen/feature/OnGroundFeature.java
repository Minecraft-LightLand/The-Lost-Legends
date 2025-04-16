package dev.xkmc.lostlegends.modules.deepnether.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.jetbrains.annotations.Nullable;

public abstract class OnGroundFeature<T extends FeatureConfiguration> extends Feature<T> {

	public OnGroundFeature(Codec<T> codec) {
		super(codec);
	}

	protected @Nullable BlockPos findValid(FeaturePlaceContext<StonePile.Data> ctx, int maxStep) {
		var pos = new BlockPos.MutableBlockPos();
		pos.set(ctx.origin());
		while (ctx.level().isEmptyBlock(pos)) {
			pos.move(0, -1, 0);
			maxStep--;
			if (maxStep < 0) return null;
			if (ctx.level().isOutsideBuildHeight(pos)) return null;
		}
		maxStep++;
		while (!ctx.level().isEmptyBlock(pos)) {
			pos.move(0, 1, 0);
			maxStep--;
			if (maxStep < 0) return null;
			if (ctx.level().isOutsideBuildHeight(pos)) return null;
		}
		return pos;
	}

}
