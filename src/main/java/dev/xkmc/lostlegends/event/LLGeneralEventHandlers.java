package dev.xkmc.lostlegends.event;

import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.util.LavaEffectsHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = LostLegends.MODID, bus = EventBusSubscriber.Bus.GAME)
public class LLGeneralEventHandlers {

	@SubscribeEvent
	public static void immunityCheck(EntityInvulnerabilityCheckEvent event) {
		if (LavaEffectsHelper.fireImmune(event.getEntity(), event.getSource())) {
			event.setInvulnerable(true);
		}
	}

	@SubscribeEvent
	public static void livingTick(EntityTickEvent.Post event) {
	}

}