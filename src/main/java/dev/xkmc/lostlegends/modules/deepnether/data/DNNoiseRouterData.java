package dev.xkmc.lostlegends.modules.deepnether.data;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class DNNoiseRouterData {
	private static final ResourceKey<DensityFunction> SHIFT_X = createKey("shift_x");
	private static final ResourceKey<DensityFunction> SHIFT_Z = createKey("shift_z");
	private static final ResourceKey<DensityFunction> BASE_3D_NOISE_NETHER = createKey("nether/base_3d_noise");

	private static ResourceKey<DensityFunction> createKey(String p_209537_) {
		return ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation.withDefaultNamespace(p_209537_));
	}

	private static DensityFunction getFunction(HolderGetter<DensityFunction> p_256312_, ResourceKey<DensityFunction> p_256077_) {
		return new DensityFunctions.HolderHolder(p_256312_.getOrThrow(p_256077_));
	}

	private static DensityFunction postProcess(DensityFunction p_224493_) {
		DensityFunction densityfunction = DensityFunctions.blendDensity(p_224493_);
		return DensityFunctions.mul(DensityFunctions.interpolated(densityfunction), DensityFunctions.constant(0.64)).squeeze();
	}

	private static NoiseRouter noNewCaves(
			HolderGetter<DensityFunction> df, HolderGetter<NormalNoise.NoiseParameters> np, DensityFunction base,
			double tempScale, double humiScale
	) {
		DensityFunction sx = getFunction(df, SHIFT_X);
		DensityFunction sz = getFunction(df, SHIFT_Z);
		DensityFunction temp = DensityFunctions.shiftedNoise2d(sx, sz, tempScale, np.getOrThrow(Noises.TEMPERATURE));
		DensityFunction humi = DensityFunctions.shiftedNoise2d(sx, sz, humiScale, np.getOrThrow(Noises.VEGETATION));
		DensityFunction ans = postProcess(base);
		return new NoiseRouter(
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				temp,
				humi,
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				ans,
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero()
		);
	}

	private static DensityFunction slideNetherLike(HolderGetter<DensityFunction> p_256084_, int p_255802_, int p_255834_) {
		return slide(getFunction(p_256084_, BASE_3D_NOISE_NETHER), p_255802_, p_255834_, 24, 0, 0.9375, -8, 24, 2.5);
	}

	public static NoiseRouter nether(HolderGetter<DensityFunction> df, HolderGetter<NormalNoise.NoiseParameters> np,
									 double tempScale, double humiScale, int maxY) {
		return noNewCaves(df, np, slideNetherLike(df, 0, maxY), tempScale, humiScale);
	}

	protected static NoiseRouter none() {
		return new NoiseRouter(
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero(),
				DensityFunctions.zero()
		);
	}

	private static DensityFunction slide(
			DensityFunction p_224444_, int p_224445_, int p_224446_, int p_224447_, int p_224448_, double p_224449_, int p_224450_, int p_224451_, double p_224452_
	) {
		DensityFunction densityfunction1 = DensityFunctions.yClampedGradient(p_224445_ + p_224446_ - p_224447_, p_224445_ + p_224446_ - p_224448_, 1.0, 0.0);
		DensityFunction $$9 = DensityFunctions.lerp(densityfunction1, p_224449_, p_224444_);
		DensityFunction densityfunction2 = DensityFunctions.yClampedGradient(p_224445_ + p_224450_, p_224445_ + p_224451_, 0.0, 1.0);
		return DensityFunctions.lerp(densityfunction2, p_224452_, $$9);
	}

}
