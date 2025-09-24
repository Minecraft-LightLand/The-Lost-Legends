package dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BeholderRenderer extends MobRenderer<BeholderEntity, BeholderModel<BeholderEntity>> {

	public BeholderRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new BeholderModel<>(ctx.bakeLayer(BeholderModelData.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(BeholderEntity e) {
		return e.getTexture();
	}

}
