package dev.xkmc.lostlegends.modules.deepnether.entity.slime.animal;

import dev.xkmc.lostlegends.foundation.entity.slime.BaseSlime;
import dev.xkmc.lostlegends.foundation.entity.slime.BaseSlimeModel;
import net.minecraft.client.model.geom.ModelPart;

public class AnimalSlimeModel<T extends BaseSlime> extends BaseSlimeModel<T> {

	private final ModelPart core;

	public AnimalSlimeModel(ModelPart root) {
		super(root);
		core = root.getChild("Head").getChild("Core");
	}

	@Override
	public void setupAnim(T e, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		var r = (0.75f + 0.25f * e.getHealth() / e.getMaxHealth());
		core.resetPose();
		core.xScale = core.yScale = core.zScale = 1 / r;
	}

}
