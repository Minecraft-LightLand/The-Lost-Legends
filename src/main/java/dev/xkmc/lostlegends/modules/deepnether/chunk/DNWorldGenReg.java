package dev.xkmc.lostlegends.modules.deepnether.chunk;

import dev.xkmc.l2core.init.reg.simple.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;

public class DNWorldGenReg {

	public final CdcVal<DNChunkGenerator> DNCG;
	public final Val<DeepCarver> DEEP_CARVER;

	public DNWorldGenReg(Reg reg) {
		var cgreg = CdcReg.of(reg, BuiltInRegistries.CHUNK_GENERATOR);
		DNCG = cgreg.reg("deep_nether", DNChunkGenerator.CODEC);

		var carverReg = SR.of(reg, BuiltInRegistries.CARVER);
		DEEP_CARVER = carverReg.reg("deep_nether_carver", () -> new DeepCarver(CaveCarverConfiguration.CODEC));
	}

}
