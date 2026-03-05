package dev.xkmc.lostlegends.foundation.entity.slime;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BaseSlimeRenderer<T extends BaseSlime> extends MobRenderer<T, BaseSlimeModel<T>> {

	public BaseSlimeRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new BaseSlimeModel<>(ctx.bakeLayer(BaseSlimeModel.INNER)), 0.25F);
		this.addLayer(new BaseSlimeOuterLayer<>(this, ctx.getModelSet()));
	}

	public BaseSlimeRenderer(EntityRendererProvider.Context ctx, ModelLayerLocation base, ModelLayerLocation outer) {
		super(ctx, new BaseSlimeModel<>(ctx.bakeLayer(base)), 0.25F);
		this.addLayer(new BaseSlimeOuterLayer<>(this, ctx.getModelSet(), outer));
	}

	public void render(T e, float yRot, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		this.shadowRadius = 0.25F * (float) e.getSize();
		super.render(e, yRot, pTick, pose, buffer, light);
	}

	protected void scale(T e, PoseStack pose, float pTick) {
		float f = 0.999F;
		pose.scale(f, f, f);
		pose.translate(0, 1 - f, 0);
		float s = (float) e.getSize() / 2;
		float squish = Mth.lerp(pTick, e.oSquish, e.squish) / (s * 0.5F + 1);
		float r = 1 / (squish + 1);
		pose.scale(r * s, 1 / r * s, r * s);
	}

	public ResourceLocation getTextureLocation(T e) {
		return e.getTexture();
	}

}
