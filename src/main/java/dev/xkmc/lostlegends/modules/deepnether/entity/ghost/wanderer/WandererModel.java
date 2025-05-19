package dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer;// Made with Blockbench 4.12.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class WandererModel<T extends WandererEntity> extends HierarchicalModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(LostLegends.loc("wanderer"), "main");

	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart leftArm;
	private final ModelPart rightArm;

	public WandererModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.leftArm = root.getChild("left_arm");
		this.rightArm = root.getChild("right_arm");
	}

	@Override
	public ModelPart root() {
		return root;
	}

	public void setupAnim(T e, float limbSw, float limbSA, float tick, float yrot, float xrot) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.yRot = yrot * (float) (Math.PI / 180.0);
		this.head.xRot = xrot * (float) (Math.PI / 180.0);
		this.animate(e.breath, WandererAnimation.BREATH, tick);
		this.animate(e.idle, WandererAnimation.IDLE, tick);
		this.animate(e.attack, WandererAnimation.ATTACK, tick);
		this.animate(e.jump, WandererAnimation.JUMP, tick);
		this.animate(e.hug, WandererAnimation.HUG, tick);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition body_1 = body.addOrReplaceChild("body_1", CubeListBuilder.create().texOffs(0, 29).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 14.0F, 5.0F, new CubeDeformation(-0.01F)), PartPose.offset(0.0F, 4.0F, 0.0F));
		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(26, 16).addBox(-1.0F, -5.0F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offset(5.0F, 5.0F, 0.0F));
		PartDefinition left_arm_1 = left_arm.addOrReplaceChild("left_arm_1", CubeListBuilder.create().texOffs(32, 0).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -3.0F, 0.0F));
		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(26, 27).addBox(-5.0F, -5.0F, -2.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offset(-5.0F, 5.0F, 0.0F));
		PartDefinition right_arm_1 = right_arm.addOrReplaceChild("right_arm_1", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -3.0F, 1.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

}