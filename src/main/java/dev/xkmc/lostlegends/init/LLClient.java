package dev.xkmc.lostlegends.init;

import dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder.BeholderModelData;
import dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer.WandererModel;
import dev.xkmc.lostlegends.modules.deepnether.entity.slime.base.NetherSlimeModel;
import dev.xkmc.lostlegends.modules.deepnether.entity.slime.piglin.PigSlimeModel;
import dev.xkmc.lostlegends.modules.spell.init.LLSpellClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;

@EventBusSubscriber(value = Dist.CLIENT, modid = LostLegends.MODID)
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
		event.registerLayerDefinition(NetherSlimeModel.INNER, NetherSlimeModel::createInner);
		event.registerLayerDefinition(NetherSlimeModel.OUTER, NetherSlimeModel::createOuter);
		event.registerLayerDefinition(PigSlimeModel.INNER, PigSlimeModel::createInner);
		event.registerLayerDefinition(PigSlimeModel.OUTER, PigSlimeModel::createOuter);
		event.registerLayerDefinition(WandererModel.LAYER_LOCATION, WandererModel::createBodyLayer);
		event.registerLayerDefinition(BeholderModelData.LAYER_LOCATION, BeholderModelData::createBodyLayer);
	}

	@SubscribeEvent
	public static void onModelLoad(ModelEvent.RegisterAdditional event) {
		LLSpellClient.onModelLoad(event);
	}

}
