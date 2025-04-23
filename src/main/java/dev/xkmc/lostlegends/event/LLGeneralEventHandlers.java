package dev.xkmc.lostlegends.event;

import dev.xkmc.lostlegends.init.LostLegends;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = LostLegends.MODID, bus = EventBusSubscriber.Bus.GAME)
public class LLGeneralEventHandlers {

	@SubscribeEvent
	public static void livingTick(EntityTickEvent.Post event) {
	}

}