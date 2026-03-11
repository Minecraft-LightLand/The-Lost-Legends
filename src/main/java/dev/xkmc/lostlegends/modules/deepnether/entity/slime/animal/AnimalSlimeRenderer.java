package dev.xkmc.lostlegends.modules.deepnether.entity.slime.animal;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.lostlegends.foundation.entity.slime.BaseSlimeModel;
import dev.xkmc.lostlegends.foundation.entity.slime.BaseSlimeRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AnimalSlimeRenderer<T extends AnimalSlime> extends BaseSlimeRenderer<T> {

	public AnimalSlimeRenderer(EntityRendererProvider.Context ctx, BaseSlimeModel<T> base, ModelLayerLocation outer) {
		super(ctx, base, outer);
	}

	@Override
	protected void scale(T e, PoseStack pose, float pTick) {
		super.scale(e, pose, pTick);
		var r = (0.75f + 0.25f * e.getHealth() / e.getMaxHealth());
		pose.scale(r, r, r);
	}

}
