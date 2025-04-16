package dev.xkmc.lostlegends.modules.deepnether.worldgen.aquifer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.RandomState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record DNAquifer(RandomState rand, int y, BlockState fluid, List<Entry> entries) implements Aquifer {

	@Override
	public @Nullable BlockState computeSubstance(DensityFunction.FunctionContext ctx, double val) {
		if (val > 0) return null;
		if (ctx.blockY() >= y)
			return Blocks.AIR.defaultBlockState();

		var temp = rand.sampler().temperature().compute(ctx);
		var hum = rand.sampler().humidity().compute(ctx);
		for (var e : entries) {
			var state = e.test(temp, hum);
			if (state != null)
				return state;
		}
		return fluid;
	}

	@Override
	public boolean shouldScheduleFluidUpdate() {
		return false;
	}

	public record Entry(double t0, double t1, double h0, double h1, double offset, double thickness,
						BlockState fluid, BlockState barrier) {

		public static final Codec<Entry> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.DOUBLE.fieldOf("temperature_min").forGetter(Entry::t0),
				Codec.DOUBLE.fieldOf("temperature_max").forGetter(Entry::t1),
				Codec.DOUBLE.fieldOf("humidity_min").forGetter(Entry::h0),
				Codec.DOUBLE.fieldOf("humidity_max").forGetter(Entry::h1),
				Codec.DOUBLE.fieldOf("offset").forGetter(Entry::offset),
				Codec.DOUBLE.fieldOf("barrier_thickness").forGetter(Entry::thickness),
				BlockState.CODEC.fieldOf("fluid").forGetter(Entry::fluid),
				BlockState.CODEC.fieldOf("barrier").forGetter(Entry::barrier)
		).apply(i, Entry::new));

		public @Nullable BlockState test(double t, double h) {
			double dt = t < t0 ? t0 - t : t > t1 ? t - t1 : 0;
			double dv = h < h0 ? h0 - h : h > h1 ? h - h1 : 0;
			double val = dt * dt + dv * dv;
			double val1 = offset * offset;
			double val2 = (offset + thickness) * (offset + thickness);
			return val < val1 ? fluid : val < val2 ? barrier : null;
		}

	}

}
