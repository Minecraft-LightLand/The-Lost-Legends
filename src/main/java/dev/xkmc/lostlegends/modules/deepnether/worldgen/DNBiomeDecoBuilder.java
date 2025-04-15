package dev.xkmc.lostlegends.modules.deepnether.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.placement.NetherPlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class DNBiomeDecoBuilder {

	private final BiomeGenerationSettings.Builder builder;

	public DNBiomeDecoBuilder(HolderGetter<PlacedFeature> pf, HolderGetter<ConfiguredWorldCarver<?>> cw) {
		builder = new BiomeGenerationSettings.Builder(pf, cw);
		builder.addCarver(GenerationStep.Carving.AIR, DNBiomeGen.DEEP_CARVER);
	}

	public DNBiomeDecoBuilder lavaSprings() {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED);
		return this;
	}

	public DNBiomeDecoBuilder delta() {
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, NetherPlacements.DELTA);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, NetherPlacements.SMALL_BASALT_COLUMNS);
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, NetherPlacements.LARGE_BASALT_COLUMNS);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_DELTA);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED_DOUBLE);
		return this;
	}

	public DNBiomeDecoBuilder firePatch() {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE);
		return this;
	}

	public DNBiomeDecoBuilder soulfirePatch() {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_SOUL_FIRE);
		return this;
	}

	public DNBiomeDecoBuilder pillar() {
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, NetherPlacements.BASALT_PILLAR);
		return this;
	}

	public DNBiomeDecoBuilder stoneBolb() {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_BLACKSTONE);
		return this;
	}

	public DNBiomeDecoBuilder magmaBolb() {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA);
		return this;
	}

	public DNBiomeDecoBuilder soulsandBolb() {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_SOUL_SAND);
		return this;
	}

	public DNBiomeDecoBuilder ores() {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_ANCIENT_DEBRIS_LARGE);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_ANCIENT_DEBRIS_SMALL);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, DNFeatures.PF_GOLD);
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, DNFeatures.PF_HEARTH);
		return this;
	}

	public DNBiomeDecoBuilder mushrooms() {
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_NORMAL);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_NORMAL);
		return this;
	}

	public DNBiomeDecoBuilder crimson() {
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.WEEPING_VINES);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TreePlacements.CRIMSON_FUNGI);
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.CRIMSON_FOREST_VEGETATION);
		return this;
	}

	public BiomeGenerationSettings.Builder build() {
		return builder;
	}

}
