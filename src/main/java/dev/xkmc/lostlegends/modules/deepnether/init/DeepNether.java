package dev.xkmc.lostlegends.modules.deepnether.init;

import dev.xkmc.lostlegends.foundation.module.LLModuleBase;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.data.DNBiomeGen;
import dev.xkmc.lostlegends.modules.deepnether.data.DNDimensionGen;
import dev.xkmc.lostlegends.modules.deepnether.data.DNFeatures;
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
		var init = LostLegends.REGISTRATE.getDataGenInitializer();
		DNFeatures.INS.init(init);
		DNBiomeGen.init(init);
		DNDimensionGen.init(init);
	}
}
