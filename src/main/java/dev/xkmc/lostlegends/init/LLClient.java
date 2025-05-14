package dev.xkmc.lostlegends.init;

import dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether.NetherSlimeModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;

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
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(NetherSlimeModel.SLIME, NetherSlimeModel::createInnerBodyLayer);
		event.registerLayerDefinition(NetherSlimeModel.SLIME_OUTER, NetherSlimeModel::createOuterBodyLayer);
	}

	@SubscribeEvent
	public static void onModelLoad(ModelEvent.RegisterAdditional event) {
	}

}
