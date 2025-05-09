package dev.xkmc.lostlegends.event;

import dev.xkmc.lostlegends.foundation.fogblock.ClientFogBlockHandler;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.util.LavaEffectsHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = LostLegends.MODID, bus = EventBusSubscriber.Bus.GAME)
public class LLClientEventHandlers {

	@SubscribeEvent
	public static void onFogColor(ViewportEvent.ComputeFogColor event) {
		ClientFogBlockHandler.onFogColor(event);
	}

	@SubscribeEvent
	public static void renderFireOverlay(RenderBlockScreenEffectEvent event) {
		if (event.getOverlayType() == RenderBlockScreenEffectEvent.OverlayType.FIRE) {
			if (LavaEffectsHelper.noFire(event.getPlayer())) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onFogSetup(ViewportEvent.RenderFog event) {
		if (event.getType() == FogType.LAVA) {
			if (event.getCamera().getEntity() instanceof LivingEntity le && LavaEffectsHelper.lavaVision(le)) {
				event.setFarPlaneDistance(24f);
				event.setCanceled(true);
			}
		}
		ClientFogBlockHandler.onFogSetup(event);
	}

}
