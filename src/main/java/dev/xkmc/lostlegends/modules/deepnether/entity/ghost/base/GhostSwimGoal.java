package dev.xkmc.lostlegends.modules.deepnether.entity.ghost.base;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;

public class GhostSwimGoal extends FloatGoal {

	private final Mob mob;

	public GhostSwimGoal(Mob e) {
		super(e);
		this.mob = e;
	}

	@Override
	public boolean canUse() {
		if (!mob.isInFluidType((fluidType, height) -> mob.canSwimInFluidType(fluidType) && height > mob.getFluidJumpThreshold()))
			return false;
		var target = mob.getTarget();
		return target == null || !mob.hasLineOfSight(target) || !(target.getY() < mob.getY());
	}

	@Override
	public void tick() {
		if (mob.getDeltaMovement().y() > 0.03)
			return;
		mob.getJumpControl().jump();
	}

}
