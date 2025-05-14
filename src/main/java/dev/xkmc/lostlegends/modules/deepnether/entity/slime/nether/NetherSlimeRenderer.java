package dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NetherSlimeRenderer extends MobRenderer<NetherSlime, NetherSlimeModel<NetherSlime>> {

	private static final ResourceLocation SLIME_LOCATION = LostLegends.loc("textures/entity/deepnether/nether_slime.png");

	public NetherSlimeRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new NetherSlimeModel<>(ctx.bakeLayer(NetherSlimeModel.SLIME)), 0.25F);
		this.addLayer(new NetherSlimeOuterLayer<>(this, ctx.getModelSet()));
	}

	public void render(NetherSlime e, float yRot, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		this.shadowRadius = 0.25F * (float) e.getSize();
		super.render(e, yRot, pTick, pose, buffer, light);
	}

	protected void scale(NetherSlime e, PoseStack pose, float pTick) {
		float f = 0.999F;
		pose.scale(f, f, f);
		pose.translate(0, 1 - f, 0);
		float s = (float) e.getSize() / 2;
		float squish = Mth.lerp(pTick, e.oSquish, e.squish) / (s * 0.5F + 1);
		float r = 1 / (squish + 1);
		pose.scale(r * s, 1 / r * s, r * s);
	}

	public ResourceLocation getTextureLocation(NetherSlime e) {
		return SLIME_LOCATION;
	}

}
