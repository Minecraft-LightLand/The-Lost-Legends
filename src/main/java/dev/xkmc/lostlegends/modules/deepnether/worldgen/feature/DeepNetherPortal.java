package dev.xkmc.lostlegends.modules.deepnether.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.lostlegends.foundation.feature.OnCeilingFeature;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.material.Fluids;

public class DeepNetherPortal extends OnCeilingFeature<DeepNetherPortal.Data> {

	public DeepNetherPortal() {
		super(Data.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<Data> ctx) {
		var level = ctx.level();
		int maxHeight = level.getMaxBuildHeight();
		var data = ctx.config();
		int min = data.minDepth;
		int max = data.maxDepth;
		int mid = (min + max) / 2;
		var raw = ctx.origin().atY(maxHeight - mid);
		var pos = findValid(level, raw, max - mid + 2);
		if (pos == null) return false;
		if (pos.getY() < maxHeight - max || pos.getY() > maxHeight - min)
			return false;
		var mpos = new BlockPos.MutableBlockPos();
		int r0 = (data.r() + 3) / 2;
		int r1 = r0 - data.r() - 1;
		int top = maxHeight - data.top();
		for (int y = pos.getY() + 2; y <= top; y++) {
			for (int x = r1; x <= r0; x++) {
				for (int z = r1; z <= r0; z++) {
					if ((x == r1 || x == r0) && (z == r1 || z == r0))
						continue;
					mpos.set(pos.getX() + x, y, pos.getZ() + z);
					if (level.isEmptyBlock(mpos)) return false;
				}
			}
		}
		for (int y = pos.getY(); y <= top; y++) {
			for (int x = r1; x <= r0; x++) {
				for (int z = r1; z <= r0; z++) {
					if ((x == r1 || x == r0) && (z == r1 || z == r0))
						continue;
					mpos.set(pos.getX() + x, y, pos.getZ() + z);
					var block = level.getBlockState(mpos);
					if (block.is(Blocks.BEDROCK)) continue;
					if (x == r1 || x == r0 || z == r1 || z == r0 || y == top) {
						if (block.isAir() && y == pos.getY()) continue;
						setBlock(level, mpos, DeepNether.BLOCKS.DEEP_NETHERRACK.get().defaultBlockState());
					} else if (y == top - 1) {
						setBlock(level, mpos, DeepNether.BLOCKS.PORTAL.get().defaultBlockState());
					} else if (y == pos.getY()) {
						setBlock(level, mpos, Blocks.AIR.defaultBlockState());
					} else {
						setBlock(level, mpos, Blocks.LAVA.defaultBlockState());
						if (y == pos.getY() + 1) {
							level.scheduleTick(mpos.immutable(), Fluids.LAVA, 0);
						}
					}
				}
			}
		}
		for (int y = 1; y <= 3; y++) {
			for (int x = r1 + 1; x < r0; x++) {
				for (int z = r1 + 1; z < r0; z++) {
					mpos.set(pos.getX() + x, pos.getY() - y, pos.getZ() + z);
					setBlock(level, mpos, Blocks.AIR.defaultBlockState());
				}
			}
		}
		return true;
	}

	public record Data(
			int r, int top, int minDepth, int maxDepth
	) implements FeatureConfiguration {

		public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.INT.fieldOf("radius").forGetter(Data::r),
				Codec.INT.fieldOf("top").forGetter(Data::top),
				Codec.INT.fieldOf("min_depth").forGetter(Data::minDepth),
				Codec.INT.fieldOf("max_depth").forGetter(Data::maxDepth)
		).apply(i, Data::new));

	}

}
