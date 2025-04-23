package dev.xkmc.lostlegends.modules.deepnether.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.lostlegends.foundation.block.SimpleLavaloggedBlock;
import dev.xkmc.lostlegends.foundation.feature.OnGroundFeature;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ScorchedBoneVinesFeature extends OnGroundFeature<ScorchedBoneVinesFeature.Data> {

	public ScorchedBoneVinesFeature() {
		super(Data.CODEC);
	}

	@Override
	protected boolean isEmpty(LevelAccessor level, BlockPos pos) {
		return !level.getBlockState(pos).isSolid();
	}

	@Override
	public boolean place(FeaturePlaceContext<Data> ctx) {
		WorldGenLevel level = ctx.level();
		BlockPos origin = findValid(level, ctx.origin(), 6);
		if (origin == null) {
			return false;
		}
		RandomSource rand = ctx.random();
		Data data = ctx.config();
		int w = data.spread();
		BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
		for (int i = 0; i < data.trial(); i++) {
			var ipos = findValid(level, origin.offset(Mth.nextInt(rand, -w, w), 0, Mth.nextInt(rand, -w, w)), 4);
			if (ipos == null) continue;
			if (!level.getBlockState(ipos).is(Blocks.LAVA)) continue;
			mpos.set(ipos);
			placeVinesColumn(level, rand, mpos, Mth.nextInt(rand, data.min, data.max), 17, 25);
		}
		return true;
	}

	public void placeVinesColumn(
			LevelAccessor level, RandomSource rand, BlockPos.MutableBlockPos pos, int height, int min, int max
	) {
		for (int i = 0; i < height; i++) {
			if (!isEmpty(level, pos)) break;
			var lava = level.getBlockState(pos).is(Blocks.LAVA);
			if (i == height - 1 || !isEmpty(level, pos.above())) {
				level.setBlock(pos, DeepNether.BLOCKS.SCORCHED_BONE_VINE.get().defaultBlockState()
								.setValue(SimpleLavaloggedBlock.LAVALOGGED, lava)
								.setValue(GrowingPlantHeadBlock.AGE, Mth.nextInt(rand, min, max)),
						2);
				break;
			}
			level.setBlock(pos, DeepNether.BLOCKS.SCORCHED_BONE_VINE_PLANT.get().defaultBlockState()
					.setValue(SimpleLavaloggedBlock.LAVALOGGED, lava), 2);
			pos.move(Direction.UP);
		}
	}

	public record Data(int spread, int trial, int min, int max) implements FeatureConfiguration {
		public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
				ExtraCodecs.POSITIVE_INT.fieldOf("spread").forGetter(Data::spread),
				ExtraCodecs.POSITIVE_INT.fieldOf("trial").forGetter(Data::trial),
				ExtraCodecs.POSITIVE_INT.fieldOf("min_height").forGetter(Data::min),
				ExtraCodecs.POSITIVE_INT.fieldOf("max_height").forGetter(Data::max)
		).apply(i, Data::new));
	}


}
