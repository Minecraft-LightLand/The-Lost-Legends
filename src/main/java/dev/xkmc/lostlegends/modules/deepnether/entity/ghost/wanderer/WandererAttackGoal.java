package dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class WandererAttackGoal extends MeleeAttackGoal {

	public final WandererEntity mob;

	private int stuckTime = 0;

	public WandererAttackGoal(WandererEntity mob, double speed, boolean seeThrough) {
		super(mob, speed, seeThrough);
		this.mob = mob;
	}

	@Override
	public void start() {
		super.start();
		stuckTime = 0;
	}

	@Override
	public void tick() {
		var target = mob.getTarget();
		if (target != null) {
			if (mob.state.isReadyToJump()) {
				WandererConstants.launch(mob, target);
			}
			if (mob.state.isHolding()) {
				return;
			}
		}
		super.tick();
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity e) {
		if (!isTimeToAttack()) return;
		if (!mob.state.isReadyToAttack()) return;
		if (!mob.hasLineOfSight(e)) return;
		stuckTime++;
		var attack = mob.state.getAttackType(mob, e);
		var mayMelee = mob.isWithinMeleeAttackRange(e);
		if (attack == WandererStateMachine.AttackType.JUMP_READY) {
			var distSqr = mob.distanceToSqr(e);
			if (distSqr < WandererConstants.jumpStartDistSqr() && !mayMelee) {
				stuckTime = 0;
				mob.state.triggerJump(mob);
				mob.getNavigation().stop();
				return;
			}
		}
		if (!mayMelee) {
			if (stuckTime >= WandererConstants.attackModeResetTime()) {
				stuckTime = 0;
				mob.state.resetAttackMode();
			}
			return;
		}
		this.resetAttackCooldown();
		if (attack == WandererStateMachine.AttackType.HUG_READY) {
			mob.state.triggerHug(mob);
			this.mob.doHurtTarget(e);
			stuckTime = 0;
		} else {
			mob.state.triggerRegular(mob);
			stuckTime = 0;
		}
	}

}
