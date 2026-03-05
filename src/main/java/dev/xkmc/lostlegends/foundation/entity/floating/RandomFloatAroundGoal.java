package dev.xkmc.lostlegends.foundation.entity.floating;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RandomFloatAroundGoal extends Goal {

	private final BaseFloatingEntity mob;

	public RandomFloatAroundGoal(BaseFloatingEntity e) {
		mob = e;
		setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		MoveControl ctrl = mob.getMoveControl();
		if (!ctrl.hasWanted()) {
			return true;
		} else {
			double dx = ctrl.getWantedX() - mob.getX();
			double dy = ctrl.getWantedY() - mob.getY();
			double dz = ctrl.getWantedZ() - mob.getZ();
			double len = dx * dx + dy * dy + dz * dz;
			return len < 1.0 || len > 3600.0;
		}
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	@Override
	public void start() {
		RandomSource r = mob.getRandom();
		double dx = mob.getX() + (r.nextFloat() * 2 - 1) * 16;
		double dy = mob.getY() + (r.nextFloat() * 2 - 1) * 16;
		double dz = mob.getZ() + (r.nextFloat() * 2 - 1) * 16;
		mob.getMoveControl().setWantedPosition(dx, dy, dz, 1);
	}

}