package dev.xkmc.lostlegends.modules.deepnether.init;

import dev.xkmc.lostlegends.foundation.module.LLModuleBase;
import dev.xkmc.lostlegends.init.LostLegends;

public class DeepNether extends LLModuleBase {

	public static final String ID = "deepnether";
	public static DNBlocks BLOCKS = new DNBlocks(LostLegends.REGISTRATE, ID);
	public static DNItems ITEMS = new DNItems(LostLegends.REGISTRATE, ID);

	@Override
	public void commonInit() {
	}
}
