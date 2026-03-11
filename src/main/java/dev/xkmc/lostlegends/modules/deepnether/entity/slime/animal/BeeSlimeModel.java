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

public class BeeSlimeModel {

	public static final ModelLayerLocation INNER = new ModelLayerLocation(LostLegends.loc("bee_slime"), "main");
	public static final ModelLayerLocation OUTER = new ModelLayerLocation(LostLegends.loc("bee_slime"), "outer");

	public static LayerDefinition createInner() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, 26.0F, 0.0F));

		PartDefinition Core = Head.addOrReplaceChild("Core", CubeListBuilder.create().texOffs(0, 33).addBox(-7.0F, -8.0F, -1.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -4.0F, -3.0F));

		PartDefinition Right_Tentacles = Core.addOrReplaceChild("Right_Tentacles", CubeListBuilder.create().texOffs(0, 48).addBox(-5.0F, -8.0F, -3.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Left_Tentacles = Core.addOrReplaceChild("Left_Tentacles", CubeListBuilder.create().texOffs(0, 48).mirror().addBox(5.0F, -8.0F, -3.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, 0.0F, 0.0F));

		PartDefinition Sting = Core.addOrReplaceChild("Sting", CubeListBuilder.create().texOffs(5, 47).addBox(-3.0F, -5.5F, 6.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public static LayerDefinition createOuter() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, 26.0F, 0.0F));

		PartDefinition Mucous_Lining = Head.addOrReplaceChild("Mucous_Lining", CubeListBuilder.create().texOffs(8, 4).addBox(-6.0F, -14.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public static EntityRenderer<? super PigSlime> createRenderer(EntityRendererProvider.Context context) {
		return new BaseSlimeRenderer<>(context, INNER, OUTER);
	}

}