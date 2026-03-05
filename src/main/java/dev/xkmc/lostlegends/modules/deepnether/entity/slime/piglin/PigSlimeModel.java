package dev.xkmc.lostlegends.modules.deepnether.entity.slime.piglin;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.foundation.entity.slime.BaseSlimeRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;

public class PigSlimeModel {

	public static final ModelLayerLocation INNER = new ModelLayerLocation(LostLegends.loc("pigslime"), "main");
	public static final ModelLayerLocation OUTER = new ModelLayerLocation(LostLegends.loc("pigslime"), "outer");

	public static LayerDefinition createInner() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 26.0F, 0.0F, 0, Mth.PI, 0));

		PartDefinition Core = Head.addOrReplaceChild("Core", CubeListBuilder.create()
						.texOffs(0, 33).addBox(-7.0F, -8.0F, -1.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
						.texOffs(0, 50).addBox(-5.0F, -4.0F, 6.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(3.0F, -4.0F, -3.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public static LayerDefinition createOuter() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(),
				PartPose.offset(0.0F, 26.0F, 0.0F));

		PartDefinition outer = head.addOrReplaceChild("outer", CubeListBuilder.create()
						.texOffs(8, 4).addBox(-6.0F, -14.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public static EntityRenderer<? super PigSlime> createRenderer(EntityRendererProvider.Context context) {
		return new BaseSlimeRenderer<>(context, INNER, OUTER);
	}

}