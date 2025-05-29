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

public record DNAquifer(RandomState rand, int y, BlockState fluid, List<BlockEntry> blocks,
						List<FluidEntry> fluids) implements Aquifer {

	@Override
	public @Nullable BlockState computeSubstance(DensityFunction.FunctionContext ctx, double val) {
		var temp = rand.sampler().temperature().compute(ctx);
		var hum = rand.sampler().humidity().compute(ctx);
		if (val > 0) {
			for (var e : blocks) {
				var state = e.test(temp, hum, ctx.blockY());
				if (state != null)
					return state;
			}
			return null;
		}
		if (ctx.blockY() >= y)
			return Blocks.AIR.defaultBlockState();
		for (var e : fluids) {
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

	public record FluidEntry(double t0, double t1, double h0, double h1, double offset, double thickness,
							 BlockState fluid, BlockState innerBarrier, BlockState outerBarrier) {

		public static final Codec<FluidEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.DOUBLE.fieldOf("temperature_min").forGetter(FluidEntry::t0),
				Codec.DOUBLE.fieldOf("temperature_max").forGetter(FluidEntry::t1),
				Codec.DOUBLE.fieldOf("humidity_min").forGetter(FluidEntry::h0),
				Codec.DOUBLE.fieldOf("humidity_max").forGetter(FluidEntry::h1),
				Codec.DOUBLE.fieldOf("offset").forGetter(FluidEntry::offset),
				Codec.DOUBLE.fieldOf("barrier_thickness").forGetter(FluidEntry::thickness),
				BlockState.CODEC.fieldOf("fluid").forGetter(FluidEntry::fluid),
				BlockState.CODEC.fieldOf("inner_barrier").forGetter(FluidEntry::innerBarrier),
				BlockState.CODEC.fieldOf("outer_barrier").forGetter(FluidEntry::outerBarrier)
		).apply(i, FluidEntry::new));

		public @Nullable BlockState test(double t, double h) {
			double dt = t < t0 ? t0 - t : t > t1 ? t - t1 : 0;
			double dv = h < h0 ? h0 - h : h > h1 ? h - h1 : 0;
			double val = dt * dt + dv * dv;
			double val1 = offset * offset;
			double val2 = (offset + thickness / 2) * (offset + thickness / 2);
			double val3 = (offset + thickness) * (offset + thickness);
			return val < val1 ? fluid : val < val2 ? innerBarrier : val < val3 ? outerBarrier : null;
		}

	}

	public record BlockEntry(double t0, double t1, double h0, double h1, double offset, double thickness,
							 BlockState block) {

		public static final Codec<BlockEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
				Codec.DOUBLE.fieldOf("temperature_min").forGetter(BlockEntry::t0),
				Codec.DOUBLE.fieldOf("temperature_max").forGetter(BlockEntry::t1),
				Codec.DOUBLE.fieldOf("humidity_min").forGetter(BlockEntry::h0),
				Codec.DOUBLE.fieldOf("humidity_max").forGetter(BlockEntry::h1),
				Codec.DOUBLE.fieldOf("offset").forGetter(BlockEntry::offset),
				Codec.DOUBLE.fieldOf("transition_thickness").forGetter(BlockEntry::thickness),
				BlockState.CODEC.fieldOf("block").forGetter(BlockEntry::block)
		).apply(i, BlockEntry::new));

		public @Nullable BlockState test(double t, double h, int y) {
			double dt = t < t0 ? t0 - t : t > t1 ? t - t1 : 0;
			double dv = h < h0 ? h0 - h : h > h1 ? h - h1 : 0;
			double val = dt * dt + dv * dv;
			double val1 = offset * offset;
			double val3 = (offset + thickness) * (offset + thickness);
			if (val < val1) return block;
			if (val > val3) return null;
			double r = mix(t, h, y);
			return r < 0.5 ? block : null;
		}

		private static double mix(double a, double b, int y) {
			long al = Double.doubleToRawLongBits(a);
			long bl = Double.doubleToRawLongBits(b);
			long x = y ^ al ^ (bl * 0x9e3779b97f4a7c15L);
			x ^= x >> 33;
			x *= 0xff51afd7ed558ccdL;
			x ^= x >> 33;
			x *= 0xc4ceb9fe1a85ec53L;
			x ^= x >> 33;
			return (x >> 11) * 0x1p-52;
		}

	}

}
