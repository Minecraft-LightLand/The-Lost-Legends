package dev.xkmc.lostlegends.init;

import dev.xkmc.lostlegends.foundation.entity.slime.BaseSlimeModel;
import dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder.BeholderModelData;
import dev.xkmc.lostlegends.modules.deepnether.entity.flying.reaper.ReaperModelData;
import dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer.WandererModel;
import dev.xkmc.lostlegends.modules.deepnether.entity.slime.animal.BeeSlimeModel;
import dev.xkmc.lostlegends.modules.deepnether.entity.slime.animal.PigSlimeModel;
import dev.xkmc.lostlegends.modules.deepnether.entity.slime.animal.SheepSlimeModel;
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
		event.registerLayerDefinition(BaseSlimeModel.INNER, BaseSlimeModel::createInner);
		event.registerLayerDefinition(BaseSlimeModel.OUTER, BaseSlimeModel::createOuter);
		event.registerLayerDefinition(PigSlimeModel.INNER, PigSlimeModel::createInner);
		event.registerLayerDefinition(PigSlimeModel.OUTER, PigSlimeModel::createOuter);
		event.registerLayerDefinition(SheepSlimeModel.INNER, SheepSlimeModel::createInner);
		event.registerLayerDefinition(SheepSlimeModel.OUTER, SheepSlimeModel::createOuter);
		event.registerLayerDefinition(BeeSlimeModel.INNER, BeeSlimeModel::createInner);
		event.registerLayerDefinition(BeeSlimeModel.OUTER, BeeSlimeModel::createOuter);
		event.registerLayerDefinition(WandererModel.LAYER_LOCATION, WandererModel::createBodyLayer);
		event.registerLayerDefinition(BeholderModelData.LAYER_LOCATION, BeholderModelData::createBodyLayer);
		event.registerLayerDefinition(ReaperModelData.LAYER_LOCATION, ReaperModelData::createBodyLayer);
	}

	@SubscribeEvent
	public static void onModelLoad(ModelEvent.RegisterAdditional event) {
		LLSpellClient.onModelLoad(event);
	}

}
