package dev.xkmc.lostlegends.modules.deepnether.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.lostlegends.foundation.feature.OnGroundFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class StonePile extends OnGroundFeature<StonePile.Data> {

	public StonePile() {
		super(Data.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<Data> ctx) {
		var data = ctx.config();
		var origin = findValid(ctx.level(), ctx.origin(), 8);
		if (origin == null) return false;
		var pos = new BlockPos.MutableBlockPos();
		int nr = (int) Math.floor(data.radius);
		for (int y = 0; y < data.height; y++) {
			pos.set(origin).move(0, y, 0);
			if (!ctx.level().isEmptyBlock(pos)) return false;
		}
		for (int x = -nr; x <= nr; x++) {
			for (int z = -nr; z <= nr; z++) {
				pos.set(origin).move(x, -1, z);
				if (!ctx.level().getBlockState(pos).isCollisionShapeFullBlock(ctx.level(), pos))
					return false;
			}
		}

		boolean success = false;
		for (int y = 0; y < data.height; y++) {
			float r = data.radius - data.slope * y;
			int mr = (int) Math.ceil(r);

			for (int x = -mr; x <= mr; x++) {
				for (int z = -mr; z <= mr; z++) {
					if (x * x + z * z > r * r) continue;
					pos.set(origin).move(x, y, z);
					setBlock(ctx.level(), pos, y == data.height - 1 ? data.top : data.base);
					success = true;
				}
			}
		}
		return success;
	}

	public record Data(
			int height, float radius, float slope,
			BlockState base, BlockState top
	) implements FeatureConfiguration {

		public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.INT.fieldOf("height").forGetter(Data::height),
				Codec.FLOAT.fieldOf("radius").forGetter(Data::radius),
				Codec.FLOAT.fieldOf("slope").forGetter(Data::slope),
				BlockState.CODEC.fieldOf("base").forGetter(Data::base),
				BlockState.CODEC.fieldOf("top").forGetter(Data::top)
		).apply(i, Data::new));

	}

}
