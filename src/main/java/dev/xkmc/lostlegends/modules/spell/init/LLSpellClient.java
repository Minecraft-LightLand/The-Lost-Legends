package dev.xkmc.lostlegends.modules.spell.init;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.neoforge.client.event.ModelEvent;

public class LLSpellClient {

	public static void onModelLoad(ModelEvent.RegisterAdditional event) {
		LLSpellGen.LIST.forEach(e -> e.additionalModels().forEach(
				x -> event.register(ModelResourceLocation.standalone(x))));
	}

}
