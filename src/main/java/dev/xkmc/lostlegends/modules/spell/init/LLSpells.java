package dev.xkmc.lostlegends.modules.spell.init;

import dev.xkmc.lostlegends.foundation.module.LLModuleBase;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class LLSpells extends LLModuleBase {

	public LLSpells() {
		LLSpellRegistry.register();
	}

	@Override
	public void gatherData(GatherDataEvent event) {
		LLSpellGen.gatherData(event);
	}

}
