package dev.xkmc.lostlegends.modules.deepnether.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.phys.Vec3;

public class NetherVolcanoPortal extends OnGroundFeature<NetherVolcanoPortal.Data> {

	public NetherVolcanoPortal(Codec<Data> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<Data> ctx) {
		var level = ctx.level();
		int minHeight = level.getMinBuildHeight();
		var data = ctx.config();
		int min = data.minDepth;
		int max = data.maxDepth;
		int mid = (min + max) / 2;
		var raw = ctx.origin().atY(minHeight + mid);
		var pos = findValid(level, raw, max - mid + 2);
		if (pos == null) return false;
		if (pos.getY() < minHeight + min || pos.getY() > minHeight + max)
			return false;
		var mpos = new BlockPos.MutableBlockPos();
		int rp = (data.r() + 3) / 2;
		int rn = rp - data.r() - 1;
		int bottom = minHeight + data.bottom();
		for (int y = bottom; y <= pos.getY() - 2; y++) {
			for (int x = rn; x <= rp; x++) {
				for (int z = rn; z <= rp; z++) {
					if ((x == rn || x == rp) && (z == rn || z == rp))
						continue;
					mpos.set(pos.getX() + x, y, pos.getZ() + z);
					var state = level.getBlockState(mpos);
					if (!state.isSolid()) return false;
				}
			}
		}
		genWell(level, pos, mpos, data, bottom, rn, rp);
		genCone(level, pos, mpos, data, ctx.random(), rn, rp);
		return true;
	}

	private void genWell(WorldGenLevel level, BlockPos pos, BlockPos.MutableBlockPos mpos, Data data, int bottom, int rn, int rp) {
		for (int y = bottom; y <= pos.getY(); y++) {
			for (int x = rn; x <= rp; x++) {
				for (int z = rn; z <= rp; z++) {
					if ((x == rn || x == rp) && (z == rn || z == rp))
						continue;
					mpos.set(pos.getX() + x, y, pos.getZ() + z);
					var block = level.getBlockState(mpos);
					if (block.is(Blocks.BEDROCK)) continue;
					if (x == rn || x == rp || z == rn || z == rp || y == bottom) {
						if (block.isAir() && y == pos.getY()) continue;
						setBlock(level, mpos, data.base);
					} else if (y == bottom + 1) {
						setBlock(level, mpos, DeepNether.BLOCKS.PORTAL.get().defaultBlockState());
					} else {
						setBlock(level, mpos, Blocks.LAVA.defaultBlockState());
					}
				}
			}
		}
	}

	private void genCone(WorldGenLevel level, BlockPos pos, BlockPos.MutableBlockPos mpos, Data data, RandomSource rand, int rn, int rp) {
		double rad = data.minRadius() + (data.maxRadius() - data.minRadius()) * rand.nextDouble();
		int height = (int) Math.ceil(rad * data.slope());
		for (int dy = 0; dy < height; dy++) {
			double dr = rad - dy / data.slope();
			int ir = (int) Math.ceil(dr);
			Vec3 cen = new Vec3(pos.getX() + (rn + rp + 1) / 2d, pos.getY() + dy + 0.5, pos.getZ() + (rn + rp + 1) / 2d);
			for (int dx = -ir; dx <= ir; dx++) {
				for (int dz = -ir; dz <= ir; dz++) {
					mpos.setWithOffset(pos, dx, dy, dz);
					double dist = mpos.getCenter().distanceTo(cen);
					if (dist > dr) continue;
					if (dx > rn && dx < rp && dz > rn && dz < rp) {
						if (dr > data.r() / 2f + 2f) {
							setBlock(level, mpos, Blocks.LAVA.defaultBlockState());
						}
						continue;
					}
					if (dr - dist < 1 / data.slope()) {
						if (rand.nextFloat() < data.holeChance()) {
							continue;
						}
					}
					if (dr - dist < 1 && rand.nextFloat() < data.decoChance()) {
						BlockState state = level.getBlockState(mpos);
						if (!state.isSolid())
							setBlock(level, mpos, data.deco());
					} else {
						setBlock(level, mpos, data.base());
					}
					if (dy == 0) {
						while (true) {
							mpos.move(Direction.DOWN);
							if (level.isOutsideBuildHeight(mpos)) break;
							BlockState state = level.getBlockState(mpos);
							if (state.isSolid()) break;
							setBlock(level, mpos, data.base());
						}
					}

				}
			}
		}
	}

	public record Data(
			int r, int bottom, int minDepth, int maxDepth,
			int minRadius, int maxRadius, float slope,
			float holeChance, BlockState base,
			float decoChance, BlockState deco
	) implements FeatureConfiguration {

		public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.INT.fieldOf("size").forGetter(Data::r),
				Codec.INT.fieldOf("bottom").forGetter(Data::bottom),
				Codec.INT.fieldOf("min_depth").forGetter(Data::minDepth),
				Codec.INT.fieldOf("max_depth").forGetter(Data::maxDepth),
				Codec.INT.fieldOf("min_radius").forGetter(Data::minRadius),
				Codec.INT.fieldOf("max_radius").forGetter(Data::maxRadius),
				Codec.FLOAT.fieldOf("slope").forGetter(Data::slope),
				Codec.FLOAT.fieldOf("erosion_chance").forGetter(Data::holeChance),
				BlockState.CODEC.fieldOf("base").forGetter(Data::base),
				Codec.FLOAT.fieldOf("decoration_chance").forGetter(Data::decoChance),
				BlockState.CODEC.fieldOf("decoration").forGetter(Data::deco)
		).apply(i, Data::new));

	}

}
