package dev.xkmc.lostlegends.modules.deepnether.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.WeepingVinesFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class HugeFungus extends Feature<HugeFungus.Data> {

	public HugeFungus() {
		super(Data.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<Data> ctx) {
		WorldGenLevel level = ctx.level();
		BlockPos pos = ctx.origin();
		RandomSource rand = ctx.random();
		ChunkGenerator cg = ctx.chunkGenerator();
		Data data = ctx.config();
		BlockPos origin = null;
		BlockState blockstate = level.getBlockState(pos.below());
		if (blockstate.is(BlockTags.NYLIUM)) {
			origin = pos;
		}

		if (origin == null) {
			return false;
		}
		boolean thick = !data.planted && rand.nextFloat() < data.thickChance();
		int size = Mth.nextInt(rand, thick ? (data.min + data.max) / 2 : data.min, data.max);
		if (rand.nextFloat() < data.largeChance()) {
			size *= 2;
		}
		if (!data.planted) {
			int j = cg.getGenDepth();
			if (origin.getY() + size + 1 >= j) {
				return false;
			}
		}
		level.setBlock(pos, Blocks.AIR.defaultBlockState(), 4);
		this.placeStem(level, rand, data, origin, size, thick);
		this.placeHat(level, rand, data, origin, size, thick);
		return true;
	}

	private static boolean isReplaceable(WorldGenLevel level, BlockPos pos) {
		return !level.getBlockState(pos).isSolid();
	}

	private void placeStem(
			WorldGenLevel level, RandomSource rand, Data data, BlockPos pos, int height, boolean thick
	) {
		BlockPos.MutableBlockPos ipos = new BlockPos.MutableBlockPos();
		BlockState state = data.stemState;
		int r = thick ? 1 : 0;

		for (int x = -r; x <= r; x++) {
			for (int z = -r; z <= r; z++) {
				boolean inR = thick && Mth.abs(x) == r && Mth.abs(z) == r;
				for (int y = 0; y < height; y++) {
					ipos.setWithOffset(pos, x, y, z);
					if (isReplaceable(level, ipos)) {
						if (data.planted) {
							if (!level.getBlockState(ipos.below()).isAir()) {
								level.destroyBlock(ipos, true);
							}
							level.setBlock(ipos, state, 3);
						} else if (inR) {
							if (rand.nextFloat() < 0.1F) {
								this.setBlock(level, ipos, state);
							}
						} else {
							this.setBlock(level, ipos, state);
						}
					}
				}
			}
		}
	}

	private void placeHat(
			WorldGenLevel level, RandomSource rand, Data data, BlockPos pos, int height, boolean thick
	) {
		BlockPos.MutableBlockPos ipos = new BlockPos.MutableBlockPos();
		boolean weeping = data.weeping();
		int h = Math.min(rand.nextInt(1 + height / 3) + 5, height);
		int y0 = height - h;

		for (int y = y0; y <= height; y++) {
			int r = y < height - rand.nextInt(3) ? 2 : 1;
			if (h > 8 && y < y0 + 4) {
				r = 3;
			}

			if (thick) {
				r++;
			}

			for (int x = -r; x <= r; x++) {
				for (int z = -r; z <= r; z++) {
					boolean xedge = x == -r || x == r;
					boolean zedge = z == -r || z == r;
					boolean mid = !xedge && !zedge && y != height;
					boolean corner = xedge && zedge;
					boolean low = y < y0 + 3;
					ipos.setWithOffset(pos, x, y, z);
					if (isReplaceable(level, ipos)) {
						if (data.planted && !level.getBlockState(ipos.below()).isAir()) {
							level.destroyBlock(ipos, true);
						}
						if (low) {
							if (!mid) {
								this.placeHatDropBlock(level, rand, ipos, data.hatState, weeping);
							}
						} else if (mid) {
							this.placeHatBlock(level, rand, data, ipos, 0.1F, 0.2F, weeping ? 0.1F : 0);
						} else if (corner) {
							this.placeHatBlock(level, rand, data, ipos, 0.01F, 0.7F, weeping ? 0.083F : 0);
						} else {
							this.placeHatBlock(level, rand, data, ipos, 5.0E-4F, 0.98F, weeping ? 0.07F : 0);
						}
					}
				}
			}
		}
	}

	private void placeHatBlock(
			LevelAccessor level, RandomSource rand, Data data, BlockPos.MutableBlockPos pos,
			float decorChance, float hatChance, float weepingChance
	) {
		if (rand.nextFloat() < decorChance) {
			this.setBlock(level, pos, data.decorState);
		} else if (rand.nextFloat() < hatChance) {
			this.setBlock(level, pos, data.hatState);
			if (rand.nextFloat() < weepingChance) {
				tryPlaceWeepingVines(pos, level, rand);
			}
		}
	}

	private void placeHatDropBlock(LevelAccessor level, RandomSource rand, BlockPos pos, BlockState state, boolean weeping) {
		if (level.getBlockState(pos.below()).is(state.getBlock())) {
			this.setBlock(level, pos, state);
		} else if (rand.nextFloat() < 0.15) {
			this.setBlock(level, pos, state);
			if (weeping && rand.nextInt(11) == 0) {
				tryPlaceWeepingVines(pos, level, rand);
			}
		}
	}

	private static void tryPlaceWeepingVines(BlockPos pos, LevelAccessor level, RandomSource source) {
		BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable().move(Direction.DOWN);
		if (level.isEmptyBlock(blockpos$mutableblockpos)) {
			int i = Mth.nextInt(source, 1, 5);
			if (source.nextInt(7) == 0) {
				i *= 2;
			}
			WeepingVinesFeature.placeWeepingVinesColumn(level, source, blockpos$mutableblockpos, i, 23, 25);
		}
	}

	public record Data(
			BlockState stemState, BlockState hatState, BlockState decorState,
			int min, int max, float thickChance, float largeChance, boolean weeping, boolean planted
	) implements FeatureConfiguration {
		public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
				BlockState.CODEC.fieldOf("stem_state").forGetter(Data::stemState),
				BlockState.CODEC.fieldOf("hat_state").forGetter(Data::hatState),
				BlockState.CODEC.fieldOf("decor_state").forGetter(Data::decorState),
				Codec.INT.fieldOf("min_height").forGetter(Data::min),
				Codec.INT.fieldOf("max_height").forGetter(Data::max),
				Codec.FLOAT.fieldOf("thick_chance").forGetter(Data::thickChance),
				Codec.FLOAT.fieldOf("huge_chance").forGetter(Data::largeChance),
				Codec.BOOL.fieldOf("weeping").orElse(false).forGetter(Data::weeping),
				Codec.BOOL.fieldOf("planted").orElse(false).forGetter(Data::planted)
		).apply(i, Data::new));

	}


}
