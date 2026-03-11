package dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class BeholderModelData {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(LostLegends.loc("beholder"), "main");

	public static final AnimationDefinition IDLE, FLAY;

	static {
		IDLE = AnimationDefinition.Builder.withLength(3.0F).looping()
				.addAnimation("Right_Wing", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, 0.75F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 1.05F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.5833F, KeyframeAnimations.posVec(0.0F, 0.3F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("Left_Wing", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0833F, KeyframeAnimations.posVec(0.0F, 0.75F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 1.05F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.5833F, KeyframeAnimations.posVec(0.0F, 0.3F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("Eye", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.75F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.5F, KeyframeAnimations.posVec(0.0F, 1.05F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 0.3F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(3.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();

		FLAY = AnimationDefinition.Builder.withLength(1.0F)
				.addAnimation("Right_Wing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.0833F, KeyframeAnimations.degreeVec(100.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5F, KeyframeAnimations.degreeVec(110.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5833F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("Right_Wing", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("Right_Wing", new AnimationChannel(AnimationChannel.Targets.SCALE,
						new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5F, KeyframeAnimations.scaleVec(1.0F, 1.2F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("Left_Wing", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.0833F, KeyframeAnimations.degreeVec(100.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5F, KeyframeAnimations.degreeVec(110.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5833F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("Left_Wing", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -1.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("Left_Wing", new AnimationChannel(AnimationChannel.Targets.SCALE,
						new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5F, KeyframeAnimations.scaleVec(1.0F, 1.2F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("Eye", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 1.25F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.8333F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 0.0F));

		PartDefinition Eye = Head.addOrReplaceChild("Eye", CubeListBuilder.create().texOffs(0, 40).addBox(-5.0F, -33.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Upper_Eyelid = Eye.addOrReplaceChild("Upper_Eyelid", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition upper_eyelid_r1 = Upper_Eyelid.addOrReplaceChild("upper_eyelid_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -9.0F, -13.0F, 12.0F, 10.0F, 13.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 1.25F, 1.0F, -1.0908F, 0.0F, 0.0F));

		PartDefinition Lower_Eyelid = Eye.addOrReplaceChild("Lower_Eyelid", CubeListBuilder.create().texOffs(0, 24).addBox(-6.0F, -1.0F, -6.0F, 12.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition Right_Wing = Head.addOrReplaceChild("Right_Wing", CubeListBuilder.create(), PartPose.offset(-6.0F, 0.0F, 7.0F));

		PartDefinition right_wing_r1 = Right_Wing.addOrReplaceChild("right_wing_r1", CubeListBuilder.create().texOffs(0, 61).mirror().addBox(-15.0F, -16.0F, -1.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3806F, 0.0992F, -0.2427F));

		PartDefinition Left_Wing = Head.addOrReplaceChild("Left_Wing", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, 6.0F));

		PartDefinition left_wing_r1 = Left_Wing.addOrReplaceChild("left_wing_r1", CubeListBuilder.create().texOffs(0, 61).addBox(-1.0F, -16.0F, -1.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.3806F, -0.0992F, 0.2427F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}