package dev.xkmc.lostlegends.foundation.entity.slime;

import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BaseSlimeModel<T extends Entity> extends HierarchicalModel<T> {

	public static final ModelLayerLocation INNER = new ModelLayerLocation(LostLegends.loc("slime"), "main");
	public static final ModelLayerLocation OUTER = new ModelLayerLocation(LostLegends.loc("slime"), "outer");

	private final ModelPart root;

	public BaseSlimeModel(ModelPart root) {
		this.root = root;
	}

	public static LayerDefinition createOuter() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition part = mesh.getRoot();
		part.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 16.0F),
				PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(mesh, 64, 64);
	}

	public static LayerDefinition createInner() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition part = mesh.getRoot();
		part.addOrReplaceChild("cube",
				CubeListBuilder.create().texOffs(0, 32)
						.addBox(-6.0F, -14.0F, -6.0F, 12.0F, 12.0F, 12.0F),
				PartPose.offset(0.0F, 24.0F, 0.0F));
		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void setupAnim(T e, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

}
