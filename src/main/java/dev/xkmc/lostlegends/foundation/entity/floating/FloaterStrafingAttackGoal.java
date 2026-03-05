package dev.xkmc.lostlegends.foundation.entity.floating;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public abstract class FloaterStrafingAttackGoal extends Goal {

	protected final BaseFloatingEntity mob;
	private final double speedModifier;
	private final float attackRadius, attackRadiusSqr;

	private int attackIntervalMin;
	private int attackTime = -1;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;
	private float strafeUp = 0;

	public FloaterStrafingAttackGoal(BaseFloatingEntity e, double speed, int interval, float rad) {
		mob = e;
		speedModifier = speed;
		attackIntervalMin = interval;
		attackRadius = rad;
		attackRadiusSqr = rad * rad;
		setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return mob.getTarget() != null;
	}

	@Override
	public void start() {
		super.start();
		mob.setAggressive(true);
		inAttack = false;
	}

	@Override
	public void stop() {
		super.stop();
		mob.setAggressive(false);
		seeTime = 0;
		attackTime = -1;
		inAttack = false;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		LivingEntity target = mob.getTarget();
		if (target == null) return;
		var diff = target.position().subtract(mob.position());
		double dist = diff.length();
		boolean see = mob.getSensing().hasLineOfSight(target);
		boolean seen = seeTime > 0;
		if (see != seen) seeTime = 0;
		if (see) seeTime++;
		else seeTime--;
		if (dist < attackRadius && seeTime >= 20)
			strafingTime++;
		else strafingTime = -1;
		if (!inAttack) doStrafe(target, diff, dist);
		tickAttack(target, see, dist < attackRadius);
	}

	private void doStrafe(LivingEntity target, Vec3 diff, double dist) {
		double horDistSqr = diff.horizontalDistanceSqr();
		if (dist > attackRadius || seeTime < 20) {
			double move = Math.min(0.5, dist - attackRadius);
			if (move > 0.1) {
				var pos = target.position().subtract(mob.position()).normalize().scale(move).add(mob.position());
				mob.getMoveControl().setWantedPosition(pos.x, pos.y, pos.z, speedModifier);
				strafingTime = -1;
			}
		}
		double dy = diff.y;
		if (dy < 3) {
			strafeUp = 0.5f;
		} else if (dy > 7) {
			strafeUp = -0.5f;
		} else if (strafeUp > 0 && dy > 5 || strafeUp < 0 && dy < 5) {
			strafeUp = 0;
		}
		if (strafingTime >= 20) {
			if (mob.getRandom().nextFloat() < 0.3) {
				strafingClockwise = !strafingClockwise;
			}
			if ((double) mob.getRandom().nextFloat() < 0.3) {
				strafingBackwards = !strafingBackwards;
			}
			strafingTime = 0;
		}
		if (strafingTime > -1) {
			if (horDistSqr > attackRadiusSqr * 0.75F) {
				strafingBackwards = false;
			} else if (horDistSqr < attackRadiusSqr * 0.25F) {
				strafingBackwards = true;
			}
			strafe(strafingBackwards ? -1 : 0.5f, strafingClockwise ? 0.5F : -0.5F, strafeUp);
			if (mob.getControlledVehicle() instanceof Mob veh) {
				veh.lookAt(target, 30, 30);
			}
		}
	}

	private void strafe(float x, float z, float y) {
		if (mob.getMoveControl() instanceof FloaterMoveControl ctrl) {
			ctrl.strafe(x, z, y);
		} else {
			mob.getMoveControl().strafe(x, z);
		}
	}

	protected boolean inAttack = false;

	protected void tickAttack(LivingEntity target, boolean see, boolean reach) {
		if (inAttack) {
			if (!see && seeTime < -60) {
				stopAttack();
				inAttack = false;
			} else if (see) {
				if (checkPerformAttack(target)) {
					attackTime = getWaitTime();
					inAttack = false;
				}
			}
		} else if (--attackTime <= 0 && seeTime >= -60 && reach) {
			startAttack(target);
			inAttack = true;
			if (mob.getMoveControl() instanceof FloaterMoveControl ctrl) ctrl.stop();
		} else {
			mob.getLookControl().setLookAt(target, 30, 30);
		}
	}

	protected int getWaitTime() {
		return attackIntervalMin;
	}

	protected abstract boolean checkPerformAttack(LivingEntity target);

	protected abstract void stopAttack();

	protected abstract void startAttack(LivingEntity target);

}
