package dev.xkmc.lostlegends.modules.deepnether.entity.flying.reaper;

import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ReaperModelData {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(LostLegends.loc("reaper"), "main");

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, 19.0F, 0.0F));

		PartDefinition Eye_Dominant = Head.addOrReplaceChild("Eye_Dominant", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -12.0F, -7.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Upper_Eyelid = Eye_Dominant.addOrReplaceChild("Upper_Eyelid", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 4.0F));

		PartDefinition upper_eyelid_r1 = Upper_Eyelid.addOrReplaceChild("upper_eyelid_r1", CubeListBuilder.create().texOffs(0, 50).addBox(-10.0F, -13.0F, -14.0F, 16.0F, 14.0F, 20.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(2.0F, -4.5F, -3.0F, -1.0908F, 0.0F, 0.0F));

		PartDefinition Lower_Eyelid = Eye_Dominant.addOrReplaceChild("Lower_Eyelid", CubeListBuilder.create().texOffs(0, 29).addBox(-8.0F, -1.0F, -15.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, 2.0F, 7.0F));

		PartDefinition Right_Eye_Secondary = Head.addOrReplaceChild("Right_Eye_Secondary", CubeListBuilder.create(), PartPose.offset(-17.0F, 0.0F, 2.0F));

		PartDefinition Right_Eyelid = Right_Eye_Secondary.addOrReplaceChild("Right_Eyelid", CubeListBuilder.create().texOffs(0, 98).addBox(-3.5F, -1.0F, -3.5F, 7.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 85).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Right_Eye_Infarction = Right_Eye_Secondary.addOrReplaceChild("Right_Eye_Infarction", CubeListBuilder.create().texOffs(57, 0).addBox(-2.0F, -6.0F, 0.0F, 16.0F, 17.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Left_Eye_Secondary = Head.addOrReplaceChild("Left_Eye_Secondary", CubeListBuilder.create(), PartPose.offset(17.0F, 0.0F, 2.0F));

		PartDefinition Left_Eyelid = Left_Eye_Secondary.addOrReplaceChild("Left_Eyelid", CubeListBuilder.create().texOffs(0, 98).mirror().addBox(-3.5F, -1.0F, -3.5F, 7.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 85).mirror().addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Left_Eye_infarction = Left_Eye_Secondary.addOrReplaceChild("Left_Eye_infarction", CubeListBuilder.create().texOffs(57, 0).mirror().addBox(-14.0F, -6.0F, 0.0F, 16.0F, 17.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
