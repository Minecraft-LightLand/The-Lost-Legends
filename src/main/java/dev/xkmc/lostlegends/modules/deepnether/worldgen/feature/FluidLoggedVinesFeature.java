package dev.xkmc.lostlegends.modules.deepnether.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.lostlegends.foundation.block.FluidVineHead;
import dev.xkmc.lostlegends.foundation.feature.OnGroundFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class FluidLoggedVinesFeature extends OnGroundFeature<FluidLoggedVinesFeature.Data> {

	public FluidLoggedVinesFeature() {
		super(Data.CODEC);
	}

	@Override
	protected boolean isEmpty(LevelAccessor level, BlockPos pos, Data data) {
		var state = level.getBlockState(pos);
		if (state.isAir()) return true;
		if (!(data.vine() instanceof FluidVineHead head)) return false;
		return state.is(head.fluidBlock());
	}

	@Override
	public boolean place(FeaturePlaceContext<Data> ctx) {
		if (!(ctx.config().vine() instanceof FluidVineHead head)) return false;
		WorldGenLevel level = ctx.level();
		RandomSource rand = ctx.random();
		Data data = ctx.config();
		BlockPos origin = findValid(level, data, ctx.origin(), 6);
		if (origin == null) {
			return false;
		}
		int w = data.spread();
		BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
		int n = data.trial();
		if (origin.getY() <= ctx.chunkGenerator().getSeaLevel() - data.min()) {
			n /= 2;
		}
		if (origin.getY() <= ctx.chunkGenerator().getSeaLevel() - data.max()) {
			n /= 2;
		}
		for (int i = 0; i < n; i++) {
			var ipos = findValid(level, data, origin.offset(Mth.nextInt(rand, -w, w), 0, Mth.nextInt(rand, -w, w)), 4);
			if (ipos == null) continue;
			if (!level.getBlockState(ipos).is(head.fluidBlock())) continue;
			if (!level.getBlockState(ipos.below()).isSolid()) continue;
			mpos.set(ipos);
			placeVinesColumn(level, rand, mpos, data, head, Mth.nextInt(rand, data.min, data.max), 17, 25);
		}
		return true;
	}

	public void placeVinesColumn(
			LevelAccessor level, RandomSource rand, BlockPos.MutableBlockPos pos, Data data, FluidVineHead head, int height, int min, int max
	) {
		for (int i = 0; i < height; i++) {
			if (!isEmpty(level, pos, data)) break;
			var logged = level.getBlockState(pos).is(head.fluidBlock());
			if (i == height - 1 || !isEmpty(level, pos.above(), data)) {
				level.setBlock(pos, head.defaultBlockState()
								.setValue(head.property(), logged)
								.setValue(GrowingPlantHeadBlock.AGE, Mth.nextInt(rand, min, max)),
						2);
				break;
			}
			level.setBlock(pos, head.getBodyBlock().defaultBlockState()
					.setValue(head.property(), logged), 2);
			pos.move(Direction.UP);
		}
	}

	public record Data(Block vine, int spread, int trial, int min, int max) implements FeatureConfiguration {
		public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
				BuiltInRegistries.BLOCK.byNameCodec().fieldOf("vine").forGetter(Data::vine),
				ExtraCodecs.POSITIVE_INT.fieldOf("spread").forGetter(Data::spread),
				ExtraCodecs.POSITIVE_INT.fieldOf("trial").forGetter(Data::trial),
				ExtraCodecs.POSITIVE_INT.fieldOf("min_height").forGetter(Data::min),
				ExtraCodecs.POSITIVE_INT.fieldOf("max_height").forGetter(Data::max)
		).apply(i, Data::new));
	}


}
