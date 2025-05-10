package dev.xkmc.lostlegends.modules.deepnether.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import javax.annotation.Nullable;

public class ColumnClusters extends Feature<ColumnClusters.Data> {

	private static final int CLUSTERED_REACH = 5;
	private static final int CLUSTERED_SIZE = 50;
	private static final int UNCLUSTERED_REACH = 8;
	private static final int UNCLUSTERED_SIZE = 15;

	public ColumnClusters() {
		super(Data.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<Data> ctx) {
		int sea = ctx.chunkGenerator().getSeaLevel();
		BlockPos pos = ctx.origin();
		WorldGenLevel level = ctx.level();
		RandomSource random = ctx.random();
		Data data = ctx.config();
		if (!canPlaceAt(data, level, sea, pos.mutable()))
			return false;
		int height = data.height().sample(random);
		boolean cluster = random.nextFloat() < 0.9F;
		int r = Math.min(height, cluster ? CLUSTERED_REACH : UNCLUSTERED_REACH);
		int l = cluster ? CLUSTERED_SIZE : UNCLUSTERED_SIZE;
		boolean success = false;
		for (BlockPos ipos : BlockPos.randomBetweenClosed(random, l,
				pos.getX() - r, pos.getY(), pos.getZ() - r,
				pos.getX() + r, pos.getY(), pos.getZ() + r
		)) {
			int dh = height - ipos.distManhattan(pos);
			if (dh >= 0) {
				success |= this.placeColumn(data, level, sea, ipos, dh, data.reach().sample(random));
			}
		}

		return success;

	}

	private boolean placeColumn(Data data, LevelAccessor level, int sea, BlockPos pos, int h, int r) {
		boolean success = false;

		for (BlockPos ipos : BlockPos.betweenClosed(
				pos.getX() - r, pos.getY(), pos.getZ() - r,
				pos.getX() + r, pos.getY(), pos.getZ() + r
		)) {
			int dist = ipos.distManhattan(pos);
			BlockPos p0 = isAirOrLavaOcean(level, sea, ipos)
					? findSurface(data, level, sea, ipos.mutable(), dist)
					: findAir(data, level, ipos.mutable(), dist);
			if (p0 == null) continue;
			int y = h - dist / 2;
			for (BlockPos.MutableBlockPos p1 = p0.mutable(); y >= 0; y--) {
				BlockState state = level.getBlockState(p1);
				if (state.is(Blocks.LAVA) || state.isAir() && p1.getY() <= sea) {
					this.setBlock(level, p1, data.base);
					success = true;
				} else if (state.isAir()) {
					this.setBlock(level, p1, data.block);
					success = true;
				} else {
					if (!level.getBlockState(p1).is(data.block.getBlock()))
						break;
				}
				p1.move(Direction.UP);
			}
		}
		return success;
	}

	@Nullable
	private static BlockPos findSurface(Data data, LevelAccessor level, int sea, BlockPos.MutableBlockPos pos, int h) {
		while (pos.getY() > level.getMinBuildHeight() + 1 && h > 0) {
			h--;
			if (canPlaceAt(data, level, sea, pos)) {
				return pos;
			}
			pos.move(Direction.DOWN);
		}
		return null;
	}

	private static boolean canPlaceAt(Data data, LevelAccessor level, int sea, BlockPos.MutableBlockPos pos) {
		if (!isAirOrLavaOcean(level, sea, pos))
			return false;
		BlockState state = level.getBlockState(pos.move(Direction.DOWN));
		pos.move(Direction.UP);
		return data.canPlaceOn.contains(state.getBlock().builtInRegistryHolder());
	}

	@Nullable
	private static BlockPos findAir(Data data, LevelAccessor level, BlockPos.MutableBlockPos pos, int y) {
		while (pos.getY() < level.getMaxBuildHeight() && y > 0) {
			y--;
			BlockState state = level.getBlockState(pos);
			if (!data.canPlaceOn.contains(state.getBlock().builtInRegistryHolder())) {
				return null;
			}
			if (state.isAir()) {
				return pos;
			}
			pos.move(Direction.UP);
		}

		return null;
	}

	private static boolean isAirOrLavaOcean(LevelAccessor level, int sea, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		return state.isAir() || state.is(Blocks.LAVA) && pos.getY() <= sea;
	}


	public record Data(
			BlockState base, BlockState block, IntProvider reach, IntProvider height,
			HolderSet<Block> canPlaceOn
	) implements FeatureConfiguration {
		public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
				BlockState.CODEC.fieldOf("base").forGetter(Data::base),
				BlockState.CODEC.fieldOf("block").forGetter(Data::block),
				IntProvider.codec(0, 3).fieldOf("reach").forGetter(Data::reach),
				IntProvider.codec(1, 10).fieldOf("height").forGetter(Data::height),
				RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_place_on").forGetter(Data::canPlaceOn)
		).apply(i, Data::new));

	}


}
