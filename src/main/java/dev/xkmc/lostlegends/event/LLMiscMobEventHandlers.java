package dev.xkmc.lostlegends.event;

import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.foundation.entity.slime.BaseSlime;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobSplitEvent;

@EventBusSubscriber(modid = LostLegends.MODID, bus = EventBusSubscriber.Bus.GAME)
public class LLMiscMobEventHandlers {

	@SubscribeEvent
	public static void slimeSplit(MobSplitEvent event) {
		if (event.getParent().isDeadOrDying()) {
			if (event.getParent() instanceof BaseSlime slime) {
				slime.onDeathSplit(event.getChildren());
			}
		}
	}

}