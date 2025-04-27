package dev.xkmc.lostlegends.modules.deepnether.init;

import dev.xkmc.l2core.init.reg.simple.*;
import dev.xkmc.lostlegends.modules.deepnether.worldgen.aquifer.DNCarver;
import dev.xkmc.lostlegends.modules.deepnether.worldgen.aquifer.DNChunkGenerator;
import dev.xkmc.lostlegends.modules.deepnether.worldgen.feature.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;

public class DNWorldGenReg {

	public final CdcVal<DNChunkGenerator> DNCG;
	public final Val<DNCarver> DEEP_CARVER;

	public final Val<DeepNetherPortal> DEEP_PORTAL;
	public final Val<NetherVolcanoPortal> NETHER_PORTAL;
	public final Val<InGroundFeature> IN_GROUND;
	public final Val<SimpleOnGroundFeature> ON_GROUND;
	public final Val<LakeFeature> LAKE;
	public final Val<LakeIslandFeature> LAKE_ISLAND;
	public final Val<HugeFungus> TREE;
	public final Val<WeepingVinesFeature> WEEPING_VINE;
	public final Val<FluidLoggedVinesFeature> FLUID_VINE;
	public final Val<StonePile> STONE_PILE;
	public final Val<ColumnClusters> COLUMN_CLUSTERS;

	public DNWorldGenReg(Reg reg) {
		var cgreg = CdcReg.of(reg, BuiltInRegistries.CHUNK_GENERATOR);
		DNCG = cgreg.reg("deep_nether", DNChunkGenerator.CODEC);

		var carverReg = SR.of(reg, BuiltInRegistries.CARVER);
		DEEP_CARVER = carverReg.reg("deep_nether_carver", () -> new DNCarver(CaveCarverConfiguration.CODEC));

		var freg = SR.of(reg, BuiltInRegistries.FEATURE);
		IN_GROUND = freg.reg("in_ground", InGroundFeature::new);
		ON_GROUND = freg.reg("on_ground", SimpleOnGroundFeature::new);
		LAKE = freg.reg("lake", LakeFeature::new);
		LAKE_ISLAND = freg.reg("lake_island", LakeIslandFeature::new);
		TREE = freg.reg("huge_fungus", HugeFungus::new);
		WEEPING_VINE = freg.reg("weeping_vines", WeepingVinesFeature::new);
		FLUID_VINE = freg.reg("fluid_logged_vines", FluidLoggedVinesFeature::new);
		STONE_PILE = freg.reg("stone_pile", StonePile::new);
		COLUMN_CLUSTERS = freg.reg("column", ColumnClusters::new);
		DEEP_PORTAL = freg.reg("deep_nether_portal", DeepNetherPortal::new);
		NETHER_PORTAL = freg.reg("nether_volcano_portal", NetherVolcanoPortal::new);

	}

}
