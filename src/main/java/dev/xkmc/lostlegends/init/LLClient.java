package dev.xkmc.lostlegends.init;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.event.RegisterNamedRenderTypesEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = LostLegends.MODID, bus = EventBusSubscriber.Bus.MOD)
public class LLClient {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
		});
	}

	@SubscribeEvent
	public static void onOverlayRegister(RegisterGuiLayersEvent event) {
	}

	@SubscribeEvent
	public static void registerItemDecoration(RegisterItemDecorationsEvent event) {
	}

	@SubscribeEvent
	public static void onModelRenderType(RegisterNamedRenderTypesEvent event) {
	}

	@SubscribeEvent
	public static void onModelLoad(ModelEvent.RegisterAdditional event) {
	}

}
