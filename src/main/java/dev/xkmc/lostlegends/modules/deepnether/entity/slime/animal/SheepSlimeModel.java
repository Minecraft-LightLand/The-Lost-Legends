package dev.xkmc.lostlegends.modules.deepnether.entity.slime.animal;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import dev.xkmc.lostlegends.foundation.entity.slime.BaseSlimeRenderer;
import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SheepSlimeModel {

	public static final ModelLayerLocation INNER = new ModelLayerLocation(LostLegends.loc("sheep_slime"), "main");
	public static final ModelLayerLocation OUTER = new ModelLayerLocation(LostLegends.loc("sheep_slime"), "outer");

	public static LayerDefinition createInner() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, 26.0F, 0.0F));

		PartDefinition Core = Head.addOrReplaceChild("Core", CubeListBuilder.create().texOffs(0, 33)
				.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -8.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public static LayerDefinition createOuter() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, 26.0F, 0.0F));

		PartDefinition Mucous_Lining = Head.addOrReplaceChild("Mucous_Lining", CubeListBuilder.create().texOffs(8, 4).addBox(-6.0F, -14.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public static EntityRenderer<? super SheepSlime> createRenderer(EntityRendererProvider.Context ctx) {
		return new BaseSlimeRenderer<>(ctx, new AnimalSlimeModel<>(ctx.bakeLayer(INNER)), OUTER);
	}

}