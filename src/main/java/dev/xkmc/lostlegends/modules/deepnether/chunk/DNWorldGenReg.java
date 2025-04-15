package dev.xkmc.lostlegends.modules.deepnether.chunk;

import dev.xkmc.l2core.init.reg.simple.CdcReg;
import dev.xkmc.l2core.init.reg.simple.CdcVal;
import dev.xkmc.l2core.init.reg.simple.Reg;
import net.minecraft.core.registries.BuiltInRegistries;

public class DNWorldGenReg {

	public final CdcVal<DNChunkGenerator> DNCG;

	public DNWorldGenReg(Reg reg) {
		var cgreg = CdcReg.of(reg, BuiltInRegistries.CHUNK_GENERATOR);
		DNCG = cgreg.reg("deep_nether", DNChunkGenerator.CODEC);
	}

}
