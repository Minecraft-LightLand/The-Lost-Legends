package dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer;

import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class WandererRenderer extends MobRenderer<WandererEntity, WandererModel<WandererEntity>> {

	public static final ResourceLocation TEX = LostLegends.loc("textures/entity/deepnether/wanderer.png");

	public WandererRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new WandererModel<>(ctx.bakeLayer(WandererModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	protected @Nullable RenderType getRenderType(WandererEntity entity, boolean visible, boolean translucent, boolean outline) {
		return super.getRenderType(entity, visible, true, outline);
	}

	@Override
	public ResourceLocation getTextureLocation(WandererEntity e) {
		return TEX;
	}

}
