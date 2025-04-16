package dev.xkmc.lostlegends.modules.deepnether.data;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.placement.NetherPlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import static net.minecraft.world.level.levelgen.GenerationStep.Decoration.UNDERGROUND_DECORATION;

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
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, NetherPlacements.DELTA);//TODO
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, NetherPlacements.SMALL_BASALT_COLUMNS);//TODO
		builder.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, NetherPlacements.LARGE_BASALT_COLUMNS);//TODO
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
		builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, NetherPlacements.BASALT_PILLAR);//TODO
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

	public DNBiomeDecoBuilder soulsandBolb() {
		builder.addFeature(UNDERGROUND_DECORATION, OrePlacements.ORE_SOUL_SAND);//TODO
		return this;
	}

	public DNBiomeDecoBuilder ores() {
		ins.ore.debrisLarge.addTo(builder, UNDERGROUND_DECORATION);
		ins.ore.debrisSmall.addTo(builder, UNDERGROUND_DECORATION);
		ins.ore.hearth.addTo(builder, UNDERGROUND_DECORATION);
		ins.ore.gold.addTo(builder, UNDERGROUND_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder mushrooms() {
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.BROWN_MUSHROOM_NORMAL);//TODO
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.RED_MUSHROOM_NORMAL);//TODO
		return this;
	}

	public DNBiomeDecoBuilder crimson() {
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.WEEPING_VINES);//TODO
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TreePlacements.CRIMSON_FUNGI);//TODO
		builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.CRIMSON_FOREST_VEGETATION);//TODO
		return this;
	}

	public BiomeGenerationSettings.Builder build() {
		return builder;
	}

}
