package dev.xkmc.lostlegends.modules.deepnether.entity.flying.base;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class FlyerMeleeAttackGoal extends Goal {
	protected final Mob mob;
	private final double speedModifier;
	private final boolean followingTargetEvenIfNotSeen;
	private Path path;
	private double pathedTargetX;
	private double pathedTargetY;
	private double pathedTargetZ;
	private int ticksUntilNextPathRecalculation;
	private int ticksUntilNextAttack;
	private final int attackInterval = 20;
	private long lastCanUseCheck;
	private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;
	private int failedPathFindingPenalty = 0;
	private boolean canPenalize = false;

	public FlyerMeleeAttackGoal(Mob e, double speed, boolean mustSee) {
		mob = e;
		speedModifier = speed;
		followingTargetEvenIfNotSeen = mustSee;
		setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		long i = mob.level().getGameTime();
		if (i - lastCanUseCheck < 20L) {
			return false;
		} else {
			lastCanUseCheck = i;
			LivingEntity target = mob.getTarget();
			if (target == null) {
				return false;
			} else if (!target.isAlive()) {
				return false;
			} else {
				if (canPenalize) {
					if (--ticksUntilNextPathRecalculation <= 0) {
						path = mob.getNavigation().createPath(target, 0);
						ticksUntilNextPathRecalculation = 4 + mob.getRandom().nextInt(7);
						return path != null;
					} else {
						return true;
					}
				}
				path = mob.getNavigation().createPath(target, 0);
				return path != null || mob.isWithinMeleeAttackRange(target);
			}
		}
	}

	@Override
	public boolean canContinueToUse() {
		LivingEntity target = mob.getTarget();
		if (target == null)
			return false;
		if (!target.isAlive())
			return false;
		if (!followingTargetEvenIfNotSeen)
			return !mob.getNavigation().isDone();
		if (!mob.isWithinRestriction(target.blockPosition()))
			return false;
		return !(target instanceof Player player) || !target.isSpectator() && !player.isCreative();

	}

	@Override
	public void start() {
		mob.getNavigation().moveTo(path, speedModifier);
		mob.setAggressive(true);
		ticksUntilNextPathRecalculation = 0;
		ticksUntilNextAttack = 0;
	}

	@Override
	public void stop() {
		LivingEntity target = mob.getTarget();
		if (target != null && !EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
			mob.setTarget(null);
		}

		mob.setAggressive(false);
		mob.getNavigation().stop();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		LivingEntity target = mob.getTarget();
		if (target != null) {
			mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
			ticksUntilNextPathRecalculation = Math.max(ticksUntilNextPathRecalculation - 1, 0);
			if ((followingTargetEvenIfNotSeen || mob.getSensing().hasLineOfSight(target))
					&& ticksUntilNextPathRecalculation <= 0
					&& (
					pathedTargetX == 0.0 && pathedTargetY == 0.0 && pathedTargetZ == 0.0
							|| target.distanceToSqr(pathedTargetX, pathedTargetY, pathedTargetZ) >= 1.0
							|| mob.getRandom().nextFloat() < 0.05F
			)) {
				pathedTargetX = target.getX();
				pathedTargetY = target.getY();
				pathedTargetZ = target.getZ();
				ticksUntilNextPathRecalculation = 4 + mob.getRandom().nextInt(7);
				double d0 = mob.distanceToSqr(target);
				if (canPenalize) {
					ticksUntilNextPathRecalculation += failedPathFindingPenalty;
					if (mob.getNavigation().getPath() != null) {
						Node finalPathPoint = mob.getNavigation().getPath().getEndNode();
						if (finalPathPoint != null && target.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
							failedPathFindingPenalty = 0;
						else
							failedPathFindingPenalty += 10;
					} else {
						failedPathFindingPenalty += 10;
					}
				}
				if (d0 > 1024.0) {
					ticksUntilNextPathRecalculation += 10;
				} else if (d0 > 256.0) {
					ticksUntilNextPathRecalculation += 5;
				}

				if (!mob.getNavigation().moveTo(target, speedModifier)) {
					ticksUntilNextPathRecalculation += 15;
				}

				ticksUntilNextPathRecalculation = adjustedTickDelay(ticksUntilNextPathRecalculation);
			}

			ticksUntilNextAttack = Math.max(ticksUntilNextAttack - 1, 0);
			checkAndPerformAttack(target);
		}
	}

	protected void checkAndPerformAttack(LivingEntity target) {
		if (canPerformAttack(target)) {
			resetAttackCooldown();
			mob.swing(InteractionHand.MAIN_HAND);
			mob.doHurtTarget(target);
		}
	}

	protected void resetAttackCooldown() {
		ticksUntilNextAttack = adjustedTickDelay(20);
	}

	protected boolean isTimeToAttack() {
		return ticksUntilNextAttack <= 0;
	}

	protected boolean canPerformAttack(LivingEntity target) {
		return isTimeToAttack() && mob.isWithinMeleeAttackRange(target) && mob.getSensing().hasLineOfSight(target);
	}

	protected int getTicksUntilNextAttack() {
		return ticksUntilNextAttack;
	}

	protected int getAttackInterval() {
		return adjustedTickDelay(20);
	}

}
