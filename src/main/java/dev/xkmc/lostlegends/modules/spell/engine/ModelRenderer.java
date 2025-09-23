package dev.xkmc.lostlegends.modules.spell.engine;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.l2magic.content.entity.core.LMProjectile;
import dev.xkmc.l2magic.content.entity.renderer.LMProjectileRenderer;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;

public record ModelRenderer(ResourceLocation id, float scale) implements ProjectileRenderer {

	@Override
	public ResourceLocation getTexture() {
		return TextureAtlas.LOCATION_BLOCKS;
	}

	@Override
	public void render(LMProjectile e, LMProjectileRenderer<?> r, float pTick, PoseStack pose, MultiBufferSource buffer, int packedLight) {
		var manager = Minecraft.getInstance().getModelManager();
		Level level = e.level();
		pose.pushPose();
		BlockPos pos = BlockPos.containing(e.getX(), e.getBoundingBox().maxY, e.getZ());
		pose.scale(scale, scale, scale);
		pose.mulPose(Axis.YP.rotationDegrees(180 - Mth.lerp(pTick, e.yRotO, e.getYRot())));
		pose.mulPose(Axis.XP.rotationDegrees( - Mth.lerp(pTick, e.xRotO, e.getXRot())));
		pose.translate(-0.5, 0, -0.5);
		var model = manager.getModel(ModelResourceLocation.standalone(id));
		int seed = e.getId();
		var state = Blocks.AIR.defaultBlockState();
		for (var rt : model.getRenderTypes(state, RandomSource.create(seed), ModelData.EMPTY))
			Minecraft.getInstance().getBlockRenderer().getModelRenderer().tesselateBlock(
					level, model, state, pos, pose,
					buffer.getBuffer(RenderTypeHelper.getMovingBlockRenderType(rt)), false,
					RandomSource.create(), seed, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, rt
			);
		pose.popPose();

	}

}
