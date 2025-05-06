package dev.xkmc.lostlegends.modules.deepnether.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.lostlegends.foundation.feature.ILakeFeature;
import dev.xkmc.lostlegends.foundation.feature.IslandFeature;
import dev.xkmc.lostlegends.foundation.feature.LakeMaker;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public class LakeIslandFeature extends IslandFeature<LakeIslandFeature.Data> implements ILakeFeature {

	private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

	public LakeIslandFeature() {
		super(Data.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<Data> ctx) {
		WorldGenLevel level = ctx.level();
		Data data = ctx.config();
		RandomSource rand = ctx.random();
		int rad = Math.min(15, data.maxWidth / 2 + 3);
		BlockPos pos = checkHeightAndRadius(level, ctx.origin(), data, rad, 0.8, 8);
		if (pos == null)
			return false;

		pos = pos.below(data.depth);
		int size = rand.nextInt(data.minTrial, data.maxTrial);

		var ins = new LakeMaker(this, true, data.maxWidth, data.maxHeight);
		ins.pre(rand, data, size);
		if (!ins.test(level, pos, data))
			return false;
		ins.gen(level, pos, rand, data);
		for (var e : data.deco) {
			int offset = data.maxWidth / 2 - data.margin - 2;
			for (int i = 0; i < offset; i++) {
				var a = Mth.PI * 2 * i / offset;
				var ipos = pos.offset(Math.round(offset * Mth.sin(a)), data.depth, Math.round(offset * Mth.cos(a)));
				e.place(level, ctx.chunkGenerator(), ctx.random(), ipos);
			}
		}
		return true;
	}


	public void setAir(WorldGenLevel level, BlockPos pos) {
		setBlock(level, pos, AIR);
		level.scheduleTick(pos, AIR.getBlock(), 0);
		markAboveForPostProcessing(level, pos);
	}

	public void setFluid(WorldGenLevel level, BlockPos pos, BlockState fluid) {
		setBlock(level, pos, fluid);
	}

	public void setBarrier(WorldGenLevel level, BlockPos pos, BlockState barrier) {
		setBlock(level, pos, barrier);
		markAboveForPostProcessing(level, pos);
	}

	public record Data(
			BlockState fluid, BlockState barrier, BlockState padding, BlockState surface,
			int depth, int maxWidth, int maxHeight, int minTrial, int maxTrial, int radius,
			int margin, int height, int clearance, List<ConfiguredFeature<?, ?>> deco
	) implements FeatureConfiguration, ILakeFeature.Data, IslandData {
		public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
				BlockState.CODEC.fieldOf("fluid").forGetter(Data::fluid),
				BlockState.CODEC.fieldOf("barrier").forGetter(Data::barrier),
				BlockState.CODEC.fieldOf("padding").forGetter(Data::padding),
				BlockState.CODEC.fieldOf("surface").forGetter(Data::surface),
				Codec.INT.fieldOf("depth").forGetter(Data::depth),
				Codec.INT.fieldOf("max_width").forGetter(Data::maxWidth),
				Codec.INT.fieldOf("max_height").forGetter(Data::maxHeight),
				Codec.INT.fieldOf("min_component").forGetter(Data::minTrial),
				Codec.INT.fieldOf("max_component").forGetter(Data::maxTrial),
				Codec.INT.fieldOf("component_radius").forGetter(Data::radius),
				Codec.INT.fieldOf("margin").forGetter(Data::margin),
				Codec.INT.fieldOf("island_height").forGetter(Data::height),
				Codec.INT.fieldOf("island_clearance").forGetter(Data::clearance),
				ConfiguredFeature.DIRECT_CODEC.listOf().fieldOf("decorations").forGetter(Data::deco)
		).apply(i, Data::new));
	}

}
