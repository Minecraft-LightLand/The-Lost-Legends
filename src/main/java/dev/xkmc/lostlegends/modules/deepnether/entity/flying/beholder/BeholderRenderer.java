package dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder;

import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BeholderRenderer extends MobRenderer<BeholderEntity, BeholderModel<BeholderEntity>> {

	public static final ResourceLocation TEX = LostLegends.loc("textures/entity/deepnether/beholder/beholder.png");
	public static final ResourceLocation POISON = LostLegends.loc("textures/entity/deepnether/beholder/poison_beholder.png");

	public BeholderRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new BeholderModel<>(ctx.bakeLayer(BeholderModelData.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(BeholderEntity e) {
		return switch (e) {
			case PoisonBeholderEntity ignored -> POISON;
			default -> TEX;
		};
	}

}
