package dev.xkmc.lostlegends.modules.deepnether.chunk;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.List;

public class DNChunkGenerator extends NoiseBasedChunkGenerator {

	public static final MapCodec<DNChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BiomeSource.CODEC.fieldOf("biome_source").forGetter(e -> e.biomeSource),
			NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(NoiseBasedChunkGenerator::generatorSettings),
			DNAquifer.Entry.CODEC.listOf().fieldOf("special_fluids").forGetter(e -> e.specialFluids)
	).apply(i, i.stable(DNChunkGenerator::new)));

	private final List<DNAquifer.Entry> specialFluids;

	public DNChunkGenerator(BiomeSource source, Holder<NoiseGeneratorSettings> settings, List<DNAquifer.Entry> specialFluids) {
		super(source, settings);
		this.specialFluids = specialFluids;
	}

	@Override
	protected MapCodec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public NoiseChunk createNoiseChunk(ChunkAccess access, StructureManager manager, Blender blender, RandomState random) {
		var gen = generatorSettings().value();
		var fluid = new Aquifer.FluidStatus(gen.seaLevel(), gen.defaultFluid());
		NoiseSettings noisesettings = gen.noiseSettings().clampToHeightAccessor(access);
		ChunkPos chunkpos = access.getPos();
		int i = 16 / noisesettings.getCellWidth();

		return new DNNoiseChunk(i, random,
				chunkpos.getMinBlockX(), chunkpos.getMinBlockZ(), noisesettings,
				Beardifier.forStructuresInChunk(manager, access.getPos()),
				gen,
				(x, y, z) -> fluid,
				blender, specialFluids
		);
	}

}
