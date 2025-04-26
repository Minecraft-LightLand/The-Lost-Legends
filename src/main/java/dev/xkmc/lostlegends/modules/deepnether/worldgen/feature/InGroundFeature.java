package dev.xkmc.lostlegends.modules.deepnether.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.lostlegends.foundation.feature.OnGroundFeature;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class InGroundFeature extends OnGroundFeature<InGroundFeature.Data> {

	public InGroundFeature() {
		super(Data.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<Data> ctx) {
		var pos = findValid(ctx.level(), ctx.config(), ctx.origin(), 4);
		if (pos == null)
			return false;
		pos = pos.below();
		var old = ctx.level().getBlockState(pos);
		if (!ctx.config().test().test(old, ctx.random()))
			return false;
		setBlock(ctx.level(), pos.below(), ctx.config().base());
		return true;
	}

	public record Data(
			BlockState base, RuleTest test
	) implements FeatureConfiguration {

		public static final Codec<Data> CODEC = RecordCodecBuilder.create(i -> i.group(
				BlockState.CODEC.fieldOf("base").forGetter(Data::base),
				RuleTest.CODEC.fieldOf("test").forGetter(Data::test)
		).apply(i, Data::new));

	}


}
