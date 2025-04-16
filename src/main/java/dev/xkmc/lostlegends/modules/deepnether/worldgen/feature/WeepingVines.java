package dev.xkmc.lostlegends.modules.deepnether.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class WeepingVines extends OnCeilingFeature<WeepingVines.Data> {
	private static final Direction[] DIRECTIONS = Direction.values();

	public WeepingVines(Codec<WeepingVines.Data> codec) {
		super(codec);
	}

	private boolean isValid(BlockState state) {
		return state.is(DeepNether.BLOCKS.DEEP_NETHERRACK) || state.is(Blocks.NETHERRACK) || state.is(Blocks.NETHER_WART_BLOCK);
	}

	@Override
	public boolean place(FeaturePlaceContext<WeepingVines.Data> ctx) {
		WorldGenLevel level = ctx.level();
		BlockPos pos = findValid(ctx.level(), ctx.origin(), 8);
		if (pos == null) return false;
		RandomSource rand = ctx.random();
		if (!level.isEmptyBlock(pos)) {
			return false;
		} else {
			BlockState state = level.getBlockState(pos.above());
			if (!isValid(state)) {
				return false;
			} else {
				this.placeRoofNetherWart(level, ctx.config(), rand, pos);
				this.placeRoofWeepingVines(level, ctx.config(), rand, pos);
				return true;
			}
		}
	}

	private void placeRoofNetherWart(LevelAccessor level, Data data, RandomSource rand, BlockPos pos) {
		level.setBlock(pos, Blocks.NETHER_WART_BLOCK.defaultBlockState(), 2);
		BlockPos.MutableBlockPos p0 = new BlockPos.MutableBlockPos();
		BlockPos.MutableBlockPos p1 = new BlockPos.MutableBlockPos();

		int r = data.wartXZ;
		for (int trial = 0; trial < data.wartTrial; trial++) {
			p0.setWithOffset(pos,
					rand.nextInt(r) - rand.nextInt(r),
					0,
					rand.nextInt(r) - rand.nextInt(r));
			var valid = findValid(level, p0, 4);
			if (valid == null) continue;
			p0.set(valid);
			if (level.isEmptyBlock(p0)) {
				int spread = 0;
				for (Direction dir : DIRECTIONS) {
					BlockState state = level.getBlockState(p1.setWithOffset(p0, dir));
					if (isValid(state)) {
						spread++;
					}
					if (spread > 1) {
						break;
					}
				}
				if (spread == 1) {
					level.setBlock(p0, Blocks.NETHER_WART_BLOCK.defaultBlockState(), 2);
				}
			}
		}
	}

	private void placeRoofWeepingVines(LevelAccessor level, Data data, RandomSource rand, BlockPos pos) {
		BlockPos.MutableBlockPos ipos = new BlockPos.MutableBlockPos();

		int r = data.vineXZ;
		for (int trial = 0; trial < data.vineTrial; trial++) {
			ipos.setWithOffset(pos,
					rand.nextInt(r) - rand.nextInt(r), 0,
					rand.nextInt(r) - rand.nextInt(r));
			var valid = findValid(level, ipos, 4);
			if (valid == null) continue;
			ipos.set(valid);
			if (level.isEmptyBlock(ipos)) {
				BlockState state = level.getBlockState(ipos.above());
				if (isValid(state)) {
					placeWeepingVinesColumn(level, rand, ipos, data.stopChance, 17, 25);
				}
			}
		}
	}

	public void placeWeepingVinesColumn(
			LevelAccessor level, RandomSource rand, BlockPos.MutableBlockPos pos, float stopChance, int min, int max
	) {
		while (true) {
			if (!level.isEmptyBlock(pos)) break;
			boolean stop = rand.nextFloat() < stopChance;
			if (stop || !level.isEmptyBlock(pos.below())) {
				level.setBlock(pos, Blocks.WEEPING_VINES.defaultBlockState()
								.setValue(GrowingPlantHeadBlock.AGE, Mth.nextInt(rand, min, max)),
						2);
				break;
			}
			level.setBlock(pos, Blocks.WEEPING_VINES_PLANT.defaultBlockState(), 2);
			pos.move(Direction.DOWN);
		}
	}

	public record Data(
			int wartTrial, int wartXZ,
			int vineTrial, int vineXZ,
			float stopChance
	) implements FeatureConfiguration {

		public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.INT.fieldOf("wart_trial").forGetter(Data::wartTrial),
				Codec.INT.fieldOf("wart_spread").forGetter(Data::wartXZ),
				Codec.INT.fieldOf("vine_trial").forGetter(Data::vineTrial),
				Codec.INT.fieldOf("vine_spread").forGetter(Data::vineXZ),
				Codec.FLOAT.fieldOf("stop_chance").forGetter(Data::stopChance)
		).apply(i, Data::new));


	}

}
