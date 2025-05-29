package dev.xkmc.lostlegends.modules.deepnether.worldgen.aquifer;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.lostlegends.foundation.dimension.SurfaceVariantData;
import dev.xkmc.lostlegends.foundation.dimension.VariantSurface;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DNChunkGenerator extends NoiseBasedChunkGenerator {

	public static final MapCodec<DNChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BiomeSource.CODEC.fieldOf("biome_source").forGetter(e -> e.biomeSource),
			NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(NoiseBasedChunkGenerator::generatorSettings),
			DNAquifer.BlockEntry.CODEC.listOf().fieldOf("special_blocks").forGetter(e -> e.specialBlocks),
			DNAquifer.FluidEntry.CODEC.listOf().fieldOf("special_fluids").forGetter(e -> e.specialFluids)
	).apply(i, i.stable(DNChunkGenerator::new)));

	private final List<DNAquifer.BlockEntry> specialBlocks;
	private final List<DNAquifer.FluidEntry> specialFluids;

	public DNChunkGenerator(BiomeSource source, Holder<NoiseGeneratorSettings> settings,
							List<DNAquifer.BlockEntry> specialBlocks,
							List<DNAquifer.FluidEntry> specialFluids) {
		super(source, settings);
		this.specialBlocks = specialBlocks;
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
				blender, specialBlocks, specialFluids
		);
	}

	@Override
	public void buildSurface(
			ChunkAccess chunk, WorldGenerationContext ctx, RandomState state,
			StructureManager structures, BiomeManager biomes, Registry<Biome> biomeReg, Blender blender) {
		Set<Block> set = new LinkedHashSet<>();
		for (var e : specialBlocks) set.add(e.block().getBlock());
		((VariantSurface) state.surfaceSystem()).lostlegends$setVariantData(new SurfaceVariantData(generatorSettings().value().defaultBlock(), set));
		super.buildSurface(chunk, ctx, state, structures, biomes, biomeReg, blender);
	}

}
