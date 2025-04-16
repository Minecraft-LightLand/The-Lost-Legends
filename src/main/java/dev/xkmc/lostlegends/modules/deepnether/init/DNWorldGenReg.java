package dev.xkmc.lostlegends.modules.deepnether.init;

import dev.xkmc.l2core.init.reg.simple.*;
import dev.xkmc.lostlegends.modules.deepnether.worldgen.aquifer.DNCarver;
import dev.xkmc.lostlegends.modules.deepnether.worldgen.aquifer.DNChunkGenerator;
import dev.xkmc.lostlegends.modules.deepnether.worldgen.feature.StonePile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;

public class DNWorldGenReg {

	public final CdcVal<DNChunkGenerator> DNCG;
	public final Val<DNCarver> DEEP_CARVER;
	public final Val<StonePile> F_PILE;

	public DNWorldGenReg(Reg reg) {
		var cgreg = CdcReg.of(reg, BuiltInRegistries.CHUNK_GENERATOR);
		DNCG = cgreg.reg("deep_nether", DNChunkGenerator.CODEC);

		var carverReg = SR.of(reg, BuiltInRegistries.CARVER);
		DEEP_CARVER = carverReg.reg("deep_nether_carver", () -> new DNCarver(CaveCarverConfiguration.CODEC));

		var freg = SR.of(reg, BuiltInRegistries.FEATURE);
		F_PILE = freg.reg("stone_pile", () -> new StonePile(StonePile.Data.CODEC));

	}

}
