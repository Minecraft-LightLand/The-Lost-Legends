package dev.xkmc.lostlegends.modules.deepnether.init;

import dev.xkmc.lostlegends.foundation.module.LLModuleBase;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.chunk.DNWorldGenReg;
import dev.xkmc.lostlegends.modules.deepnether.worldgen.DNBiomeGen;
import dev.xkmc.lostlegends.modules.deepnether.worldgen.DNDimensionGen;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class DeepNether extends LLModuleBase {

	public static final String ID = "deepnether";
	public static DNBlocks BLOCKS = new DNBlocks(LostLegends.REGISTRATE, ID);
	public static DNItems ITEMS = new DNItems(LostLegends.REGISTRATE, ID);
	public static DNWorldGenReg WG = new DNWorldGenReg(LostLegends.REG);

	@Override
	public void commonInit() {
	}

	@Override
	public void gatherData(GatherDataEvent event) {
		DNBiomeGen.init(LostLegends.REGISTRATE.getDataGenInitializer());
		DNDimensionGen.init(LostLegends.REGISTRATE.getDataGenInitializer());
	}
}
