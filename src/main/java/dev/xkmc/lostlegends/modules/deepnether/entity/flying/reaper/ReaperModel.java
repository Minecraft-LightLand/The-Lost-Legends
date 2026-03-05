package dev.xkmc.lostlegends.modules.deepnether.entity.flying.reaper;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;

public class ReaperModel<T extends ReaperEntity> extends HierarchicalModel<T> {

	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart right;
	private final ModelPart left;

	public ReaperModel(ModelPart root) {
		this.root = root.getChild("Head");
		this.body = this.root.getChild("Eye_Dominant");
		this.right = this.root.getChild("Right_Eye_Secondary");
		this.left = this.root.getChild("Left_Eye_Secondary");
	}

	@Override
	public ModelPart root() {
		return root;
	}

	public void setupAnim(T e, float limbSw, float limbSA, float tick, float yrot, float xrot) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.root.yRot += yrot * (float) (Math.PI / 180.0);
		this.root.xRot += xrot * (float) (Math.PI / 180.0);
		left.xScale = left.yScale = left.zScale = 1 + e.getLeftScale();
		right.xScale = right.yScale = right.zScale = 1 + e.getRightScale();
		this.animate(e.states.idle, ReaperModelData.IDLE, tick);
	}

}