package dev.xkmc.lostlegends.modules.deepnether.data;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.placement.NetherPlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import static net.minecraft.world.level.levelgen.GenerationStep.Decoration.*;

public class DNBiomeDecoBuilder {

	private final DNFeatures ins = DNFeatures.INS;
	private final BiomeGenerationSettings.Builder builder;

	public DNBiomeDecoBuilder(HolderGetter<PlacedFeature> pf, HolderGetter<ConfiguredWorldCarver<?>> cw) {
		builder = new BiomeGenerationSettings.Builder(pf, cw);
		builder.addCarver(GenerationStep.Carving.AIR, DNBiomeGen.DEEP_CARVER);
		ins.blob.rackSmall.addTo(builder, UNDERGROUND_DECORATION);
		ins.blob.rackLarge.addTo(builder, UNDERGROUND_DECORATION);
	}

	public DNBiomeDecoBuilder lavaSprings() {
		ins.simple.springOpen.addTo(builder, UNDERGROUND_DECORATION);
		ins.simple.springClose.addTo(builder, UNDERGROUND_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder darkstonePile() {
		ins.struct.darkPile.addTo(builder, UNDERGROUND_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder delta() {
		builder.addFeature(SURFACE_STRUCTURES, NetherPlacements.DELTA);
		builder.addFeature(SURFACE_STRUCTURES, NetherPlacements.SMALL_BASALT_COLUMNS);
		builder.addFeature(SURFACE_STRUCTURES, NetherPlacements.LARGE_BASALT_COLUMNS);
		builder.addFeature(UNDERGROUND_DECORATION, NetherPlacements.SPRING_DELTA);//TODO
		ins.simple.springClose2.addTo(builder, UNDERGROUND_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder firePatch() {
		ins.simple.firePatch.addTo(builder, UNDERGROUND_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder soulfirePatch() {
		ins.simple.soulfirePatch.addTo(builder, UNDERGROUND_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder pillar() {
		builder.addFeature(LOCAL_MODIFICATIONS, NetherPlacements.BASALT_PILLAR);
		return this;
	}

	public DNBiomeDecoBuilder blackstoneBolb() {
		ins.blob.blackstone.addTo(builder, UNDERGROUND_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder magmaBolb() {
		builder.addFeature(UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA);//TODO
		return this;
	}

	public DNBiomeDecoBuilder ores() {
		ins.ore.debrisLarge.addTo(builder, UNDERGROUND_DECORATION);
		ins.ore.debrisSmall.addTo(builder, UNDERGROUND_DECORATION);
		ins.ore.hearth.addTo(builder, UNDERGROUND_DECORATION);
		ins.ore.gold.addTo(builder, UNDERGROUND_DECORATION);
		ins.ore.goldClose.addTo(builder, UNDERGROUND_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder ashBlossom() {
		ins.simple.ashBlossom.addTo(builder, VEGETAL_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder crimsonRoot() {
		ins.simple.crimsonRoot.addTo(builder, VEGETAL_DECORATION);
		return this;
	}


	public DNBiomeDecoBuilder mushrooms() {
		builder.addFeature(VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_NETHER);
		builder.addFeature(VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_NETHER);
		return this;
	}


	public DNBiomeDecoBuilder crimsonPlains() {
		ins.simple.weepingVineSparse.addTo(builder, VEGETAL_DECORATION);
		ins.tree.crimsonShort.addTo(builder, VEGETAL_DECORATION);
		builder.addFeature(VEGETAL_DECORATION, NetherPlacements.CRIMSON_FOREST_VEGETATION);
		return this;
	}

	public DNBiomeDecoBuilder crimsonForest() {
		ins.simple.weepingVine.addTo(builder, VEGETAL_DECORATION);
		ins.tree.crimson.addTo(builder, VEGETAL_DECORATION);
		builder.addFeature(VEGETAL_DECORATION, NetherPlacements.CRIMSON_FOREST_VEGETATION);
		return this;
	}

	public BiomeGenerationSettings.Builder build() {
		return builder;
	}

}
