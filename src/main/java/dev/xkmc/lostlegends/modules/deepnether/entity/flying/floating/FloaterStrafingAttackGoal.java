package dev.xkmc.lostlegends.modules.deepnether.entity.flying.floating;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public abstract class FloaterStrafingAttackGoal extends Goal {

	protected final Mob mob;
	private final double speedModifier;
	private final float attackRadius, attackRadiusSqr;

	private int attackIntervalMin;
	private int attackTime = -1;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public FloaterStrafingAttackGoal(Mob e, double speed, int interval, float rad) {
		mob = e;
		speedModifier = speed;
		attackIntervalMin = interval;
		attackRadius = rad;
		attackRadiusSqr = rad * rad;
		setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	public void setMinAttackInterval(int interval) {
		attackIntervalMin = interval;
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
		double d0 = mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
		boolean see = mob.getSensing().hasLineOfSight(target);
		boolean seen = seeTime > 0;
		if (see != seen) {
			seeTime = 0;
		}
		if (see) {
			seeTime++;
		} else {
			seeTime--;
		}

		if (d0 < attackRadiusSqr && seeTime >= 20) {
			strafingTime++;
		} else {
			double dist = Math.min(1, Math.sqrt(d0) - attackRadius);
			if (dist > 0.1) {
				var pos = target.position().subtract(mob.position()).normalize().scale(dist).add(mob.position());
				mob.getMoveControl().setWantedPosition(pos.x, pos.y, pos.z, speedModifier);
				strafingTime = -1;
			}
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
			if (d0 > attackRadiusSqr * 0.75F) {
				strafingBackwards = false;
			} else if (d0 < attackRadiusSqr * 0.25F) {
				strafingBackwards = true;
			}
			mob.getMoveControl().strafe(strafingBackwards ? -1 : 0.5f, strafingClockwise ? 0.5F : -0.5F);
			if (mob.getControlledVehicle() instanceof Mob veh) {
				veh.lookAt(target, 30, 30);
			}
			mob.getLookControl().setLookAt(target, 30, 30);
		} else {
			mob.getLookControl().setLookAt(target, 30, 30);
		}

		tickAttack(target, see);
	}

	private boolean inAttack = false;

	protected void tickAttack(LivingEntity target, boolean see) {
		if (inAttack) {
			if (!see && seeTime < -60) {
				stopAttack();
				inAttack = false;
			} else if (see) {
				if (checkPerformAttack(target)) {
					attackTime = attackIntervalMin;
					inAttack = false;
				}
			}
		} else if (--attackTime <= 0 && seeTime >= -60) {
			startAttack(target);
			inAttack = true;
		}
	}

	protected abstract boolean checkPerformAttack(LivingEntity target);

	protected abstract void stopAttack();

	protected abstract void startAttack(LivingEntity target);

}
