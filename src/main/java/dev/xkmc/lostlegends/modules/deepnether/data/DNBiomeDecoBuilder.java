package dev.xkmc.lostlegends.modules.deepnether.data;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.placement.NetherPlacements;
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
		ins.blob.rackSmall.addTo(builder, UNDERGROUND_STRUCTURES);
		ins.blob.rackLarge.addTo(builder, UNDERGROUND_STRUCTURES);
		ins.struct.deepPortal.addTo(builder, FLUID_SPRINGS);
	}

	public DNBiomeDecoBuilder lavaSprings() {
		ins.simple.springOpen.addTo(builder, FLUID_SPRINGS);
		ins.simple.springClose.addTo(builder, FLUID_SPRINGS);
		return this;
	}

	public DNBiomeDecoBuilder darkstonePile() {
		ins.struct.darkPile.addTo(builder, UNDERGROUND_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder delta() {
		builder.addFeature(LOCAL_MODIFICATIONS, NetherPlacements.DELTA);
		ins.delta.columnSmall.addTo(builder, LOCAL_MODIFICATIONS);
		ins.delta.columnLarge.addTo(builder, LOCAL_MODIFICATIONS);
		ins.simple.springClose2.addTo(builder, FLUID_SPRINGS);
		ins.vege.boneVine.addTo(builder, VEGETAL_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder firePatch() {
		ins.simple.firePatch.addTo(builder, UNDERGROUND_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder soulland() {
		ins.simple.soulfirePatch.addTo(builder, UNDERGROUND_DECORATION);
		ins.vege.soulVine.addTo(builder, VEGETAL_DECORATION);
		ins.struct.soulLake.addTo(builder, LAKES);
		ins.struct.soulIsland.addTo(builder, LAKES);
		ins.vege.scarletRoot.addTo(builder, VEGETAL_DECORATION);
		ins.vege.soulBlossom.addTo(builder, VEGETAL_DECORATION);
		ins.vege.ghoshroom.addTo(builder, VEGETAL_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder pillar() {
		builder.addFeature(LOCAL_MODIFICATIONS, NetherPlacements.BASALT_PILLAR);
		return this;
	}

	public DNBiomeDecoBuilder blackstoneBolb() {
		ins.blob.blackstone.addTo(builder, UNDERGROUND_STRUCTURES);
		ins.ore.amarast.addTo(builder, UNDERGROUND_ORES);
		return this;
	}

	public DNBiomeDecoBuilder warpedBlob() {
		ins.blob.twist.addTo(builder, UNDERGROUND_STRUCTURES);
		ins.ore.resonant.addTo(builder, UNDERGROUND_ORES);
		return this;
	}

	public DNBiomeDecoBuilder magmaBolb() {
		ins.blob.magma.addTo(builder, UNDERGROUND_STRUCTURES);
		return this;
	}

	public DNBiomeDecoBuilder lavaLake() {
		ins.struct.lavaLake.addTo(builder, LAKES);
		return this;
	}

	public DNBiomeDecoBuilder goldLake() {
		ins.struct.goldLake.addTo(builder, LAKES);
		return this;
	}

	public DNBiomeDecoBuilder lavaIsland() {
		ins.struct.lavaIsland.addTo(builder, LAKES);
		return this;
	}

	public DNBiomeDecoBuilder amber() {
		ins.simple.amber.addTo(builder, UNDERGROUND_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder ecto() {
		ins.simple.ecto.addTo(builder, UNDERGROUND_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder ores() {
		ins.ore.debrisLarge.addTo(builder, UNDERGROUND_ORES);
		ins.ore.debrisSmall.addTo(builder, UNDERGROUND_ORES);
		ins.ore.hearth.addTo(builder, UNDERGROUND_ORES);
		ins.ore.gold.addTo(builder, UNDERGROUND_ORES);
		ins.ore.goldClose.addTo(builder, UNDERGROUND_ORES);
		return this;
	}

	public DNBiomeDecoBuilder ashBlossom() {
		ins.vege.ashBlossom.addTo(builder, VEGETAL_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder hearthroom() {
		ins.vege.hearthroom.addTo(builder, VEGETAL_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder crimsonPlains() {
		ins.vege.weepingVineSparse.addTo(builder, VEGETAL_DECORATION);
		ins.tree.crimsonShort.addTo(builder, VEGETAL_DECORATION);
		ins.vege.scarletBlossom.addTo(builder, VEGETAL_DECORATION);
		ins.vege.crimsonVegetation.addTo(builder, VEGETAL_DECORATION);
		return this;
	}

	public DNBiomeDecoBuilder crimsonForest() {
		ins.vege.weepingVine.addTo(builder, VEGETAL_DECORATION);
		ins.tree.crimson.addTo(builder, VEGETAL_DECORATION);
		ins.vege.scarletBlossom.addTo(builder, VEGETAL_DECORATION);
		ins.vege.crimsonVegetation.addTo(builder, VEGETAL_DECORATION);
		return this;
	}

	public BiomeGenerationSettings.Builder build() {
		return builder;
	}

}
