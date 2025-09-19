package dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;

public class BeholderModel<T extends BeholderEntity> extends HierarchicalModel<T> {

	private final ModelPart root;

	public BeholderModel(ModelPart root) {
		this.root = root;
	}

	@Override
	public ModelPart root() {
		return root;
	}

	public void setupAnim(T e, float limbSw, float limbSA, float tick, float yrot, float xrot) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.root.yRot += yrot * (float) (Math.PI / 180.0);
		this.root.xRot += xrot * (float) (Math.PI / 180.0);
		this.animate(e.states.idle, BeholderModelData.IDLE, tick);
	}

}