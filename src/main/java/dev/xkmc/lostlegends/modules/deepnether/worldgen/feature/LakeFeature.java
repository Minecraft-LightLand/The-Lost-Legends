package dev.xkmc.lostlegends.modules.deepnether.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.lostlegends.foundation.feature.ILakeFeature;
import dev.xkmc.lostlegends.foundation.feature.LakeMaker;
import dev.xkmc.lostlegends.foundation.feature.OnGroundFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class LakeFeature extends OnGroundFeature<LakeFeature.Data> implements ILakeFeature {

	private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

	public LakeFeature() {
		super(Data.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<Data> ctx) {
		WorldGenLevel level = ctx.level();
		Data data = ctx.config();
		BlockPos pos = findValid(level, data, ctx.origin(), 8);
		if (pos == null)
			return false;
		RandomSource rand = ctx.random();

		pos = pos.below(data.depth);
		int size = rand.nextInt(data.minTrial, data.maxTrial);

		var ins = new LakeMaker(this, false, data.maxWidth, data.maxHeight);
		ins.pre(rand, data, size);
		if (!ins.test(level, pos, data))
			return false;
		ins.gen(level, pos, rand, data);
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
			BlockState fluid, BlockState barrier,
			int depth, int maxWidth, int maxHeight, int minTrial, int maxTrial, int radius
	) implements FeatureConfiguration, ILakeFeature.Data {
		public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
				BlockState.CODEC.fieldOf("fluid").forGetter(Data::fluid),
				BlockState.CODEC.fieldOf("barrier").forGetter(Data::barrier),
				Codec.INT.fieldOf("depth").forGetter(Data::depth),
				Codec.INT.fieldOf("max_width").forGetter(Data::maxWidth),
				Codec.INT.fieldOf("max_height").forGetter(Data::maxHeight),
				Codec.INT.fieldOf("min_component").forGetter(Data::minTrial),
				Codec.INT.fieldOf("max_component").forGetter(Data::maxTrial),
				Codec.INT.fieldOf("component_radius").forGetter(Data::radius)
		).apply(i, Data::new));
	}

}
