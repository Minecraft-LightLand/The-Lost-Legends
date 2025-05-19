package dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class WandererAttackGoal extends MeleeAttackGoal {

	public final WandererEntity mob;

	public WandererAttackGoal(WandererEntity mob, double speed, boolean seeThrough) {
		super(mob, speed, seeThrough);
		this.mob = mob;
	}

	@Override
	protected void checkAndPerformAttack(LivingEntity e) {
		if (!isTimeToAttack()) return;
		if (!mob.state.isReady()) return;
		if (!mob.hasLineOfSight(e)) return;
		var attack = mob.state.getAttackType(mob, e);
		var mayMelee = mob.isWithinMeleeAttackRange(e);
		if (attack == WandererStateMachine.AttackType.JUMP_READY) {
			var distSqr = mob.distanceToSqr(e);
			if (distSqr < 25 && !mayMelee) {
				mob.state.triggerJump(mob);
				var vec = e.getEyePosition().subtract(mob.position()).normalize();
				mob.setDeltaMovement(mob.getDeltaMovement().add(vec.scale(1.5)));
				return;
			}
		}
		if (!mayMelee) return;
		this.resetAttackCooldown();
		if (attack == WandererStateMachine.AttackType.HUG_READY) {
			mob.state.triggerHug(mob);
			this.mob.doHurtTarget(e);
		} else {
			mob.state.triggerRegular(mob);
		}
	}

}
