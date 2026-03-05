package dev.xkmc.lostlegends.modules.deepnether.entity.flying.reaper;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ReaperRenderer extends MobRenderer<ReaperEntity, ReaperModel<ReaperEntity>> {

	public ReaperRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new ReaperModel<>(ctx.bakeLayer(ReaperModelData.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(ReaperEntity e) {
		return e.getTexture();
	}

}
