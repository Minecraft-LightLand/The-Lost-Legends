package dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NetherSlimeOuterLayer<T extends LivingEntity> extends RenderLayer<T, NetherSlimeModel<T>> {
	private final EntityModel<T> model;

	public NetherSlimeOuterLayer(RenderLayerParent<T, NetherSlimeModel<T>> parent, EntityModelSet set) {
		super(parent);
		this.model = new NetherSlimeModel<>(set.bakeLayer(NetherSlimeModel.SLIME_OUTER));
	}

	public void render(
			PoseStack pose, MultiBufferSource buffer, int light, T e,
			float limbSwing, float limbSwingAmount,
			float pTick, float ageInTicks, float netHeadYaw, float headPitch
	) {
		Minecraft minecraft = Minecraft.getInstance();
		boolean flag = minecraft.shouldEntityAppearGlowing(e) && e.isInvisible();
		if (!e.isInvisible() || flag) {
			VertexConsumer vc;
			if (flag) {
				vc = buffer.getBuffer(RenderType.outline(this.getTextureLocation(e)));
			} else {
				vc = buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(e)));
			}
			this.getParentModel().copyPropertiesTo(this.model);
			this.model.prepareMobModel(e, limbSwing, limbSwingAmount, pTick);
			this.model.setupAnim(e, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			this.model.renderToBuffer(pose, vc, light, LivingEntityRenderer.getOverlayCoords(e, 0));
		}
	}

}
