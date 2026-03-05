package dev.xkmc.lostlegends.modules.deepnether.entity.flying.reaper;

import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ReaperModelData {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(LostLegends.loc("reaper"), "main");

	public static final AnimationDefinition IDLE, ATTACK;

	static {
		IDLE = AnimationDefinition.Builder.withLength(3.5F).looping()
				.addAnimation("Eye_Dominant", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, 0.55F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.9167F, KeyframeAnimations.posVec(0.0F, 0.05F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(3.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("Right_Eyelid", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 1.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, 1.65F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.9167F, KeyframeAnimations.posVec(0.0F, 0.15F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(3.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("Left_Eyelid", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 1.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, 1.65F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.9167F, KeyframeAnimations.posVec(0.0F, 0.15F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(3.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("Right_Eye_Infarction", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, 0.275F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.9167F, KeyframeAnimations.posVec(0.0F, -0.45F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(3.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("Left_Eye_infarction", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.1667F, KeyframeAnimations.posVec(0.0F, 0.25F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, 0.275F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(2.9167F, KeyframeAnimations.posVec(0.0F, -0.45F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(3.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.build();

		ATTACK = AnimationDefinition.Builder.withLength(2.875F).looping()
				.addAnimation("Eye_Dominant", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("Upper_Eyelid", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.125F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("Upper_Eyelid", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 0.0F, -3.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.addAnimation("Lower_Eyelid", new AnimationChannel(AnimationChannel.Targets.ROTATION,
						new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.125F, KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
						new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
				))
				.addAnimation("Lower_Eyelid", new AnimationChannel(AnimationChannel.Targets.POSITION,
						new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.125F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
						new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
				))
				.build();
	}


	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.0F));

		PartDefinition Eye_Dominant = Head.addOrReplaceChild("Eye_Dominant", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -12.0F, -7.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Upper_Eyelid = Eye_Dominant.addOrReplaceChild("Upper_Eyelid", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 4.0F));

		PartDefinition upper_eyelid_r1 = Upper_Eyelid.addOrReplaceChild("upper_eyelid_r1", CubeListBuilder.create().texOffs(0, 50).addBox(-10.0F, -13.0F, -14.0F, 16.0F, 14.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -4.5F, -3.0F, -1.0908F, 0.0F, 0.0F));

		PartDefinition Lower_Eyelid = Eye_Dominant.addOrReplaceChild("Lower_Eyelid", CubeListBuilder.create().texOffs(0, 29).addBox(-8.0F, -1.0F, -15.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 2.0F, 7.0F));

		PartDefinition Right_Eye_Secondary = Head.addOrReplaceChild("Right_Eye_Secondary", CubeListBuilder.create(), PartPose.offset(-8.0F, 0.0F, 2.0F));

		PartDefinition Right_Eyelid = Right_Eye_Secondary.addOrReplaceChild("Right_Eyelid", CubeListBuilder.create().texOffs(0, 98).addBox(-3.5F, -1.0F, -3.5F, 7.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 85).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 0.0F, 0.0F));

		PartDefinition Right_Eye_Infarction = Right_Eye_Secondary.addOrReplaceChild("Right_Eye_Infarction", CubeListBuilder.create().texOffs(57, 0).addBox(-2.0F, -6.0F, 0.0F, 16.0F, 17.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 0.0F, 0.0F));

		PartDefinition Left_Eye_Secondary = Head.addOrReplaceChild("Left_Eye_Secondary", CubeListBuilder.create(), PartPose.offset(8.0F, 0.0F, 2.0F));

		PartDefinition Left_Eyelid = Left_Eye_Secondary.addOrReplaceChild("Left_Eyelid", CubeListBuilder.create().texOffs(0, 98).mirror().addBox(-3.5F, -1.0F, -3.5F, 7.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 85).mirror().addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(9.0F, 0.0F, 0.0F));

		PartDefinition Left_Eye_infarction = Left_Eye_Secondary.addOrReplaceChild("Left_Eye_infarction", CubeListBuilder.create().texOffs(57, 0).mirror().addBox(-14.0F, -6.0F, 0.0F, 16.0F, 17.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(9.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
