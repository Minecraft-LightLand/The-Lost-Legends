package dev.xkmc.lostlegends.modules.deepnether.chunk;

import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.List;

public class DNNoiseChunk extends NoiseChunk {

	public DNNoiseChunk(
			int xz, RandomState rand, int x0, int z0, NoiseSettings settings,
			DensityFunctions.BeardifierOrMarker marker, NoiseGeneratorSettings gen,
			Aquifer.FluidPicker aquifier, Blender blender, List<DNAquifer.Entry> specialFluids
	) {
		super(xz, rand, x0, z0, settings, marker, gen, aquifier, blender);
		aquifer = new DNAquifer(rand, gen.seaLevel(), gen.defaultFluid(), specialFluids);
	}

}
