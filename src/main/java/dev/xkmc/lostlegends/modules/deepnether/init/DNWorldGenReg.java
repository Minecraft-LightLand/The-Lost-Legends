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
	public final Val<HugeFungus> F_TREE;
	public final Val<WeepingVines> F_WEEPING_VINE;
	public final Val<StonePile> F_PILE;
	public final Val<ColumnClusters> F_COLUMN;
	public final Val<DeepNetherPortal> F_DEEP_PORTAL;
	public final Val<NetherVolcanoPortal> F_NETHER_PORTAL;

	public DNWorldGenReg(Reg reg) {
		var cgreg = CdcReg.of(reg, BuiltInRegistries.CHUNK_GENERATOR);
		DNCG = cgreg.reg("deep_nether", DNChunkGenerator.CODEC);

		var carverReg = SR.of(reg, BuiltInRegistries.CARVER);
		DEEP_CARVER = carverReg.reg("deep_nether_carver", () -> new DNCarver(CaveCarverConfiguration.CODEC));

		var freg = SR.of(reg, BuiltInRegistries.FEATURE);
		F_TREE = freg.reg("huge_fungus", () -> new HugeFungus(HugeFungus.Data.CODEC));
		F_WEEPING_VINE = freg.reg("weeping_vines", () -> new WeepingVines(WeepingVines.Data.CODEC));
		F_PILE = freg.reg("stone_pile", () -> new StonePile(StonePile.Data.CODEC));
		F_COLUMN = freg.reg("column", () -> new ColumnClusters(ColumnClusters.Data.CODEC));
		F_DEEP_PORTAL = freg.reg("deep_nether_portal", () -> new DeepNetherPortal(DeepNetherPortal.Data.CODEC));
		F_NETHER_PORTAL = freg.reg("nether_volcano_portal", () -> new NetherVolcanoPortal(NetherVolcanoPortal.Data.CODEC));

	}

}
