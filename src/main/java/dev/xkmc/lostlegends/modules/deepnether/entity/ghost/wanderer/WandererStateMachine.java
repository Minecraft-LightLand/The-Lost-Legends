package dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.entity.LivingEntity;

@SerialClass
public class WandererStateMachine {

	@SerialField
	private Action action = Action.IDLE;
	@SerialField
	private AttackType attack = AttackType.CLEAR;
	@SerialField
	private int tick;

	public void tick(WandererEntity e) {
		if (tick > 0) {
			tick--;
			if (action == Action.JUMP) {
				if (e.onGround()) {
					tick = 0;
				}
			}
			if (action == Action.ATTACK) {
				if (tick == 10) {
					var target = e.getTarget();
					if (target != null && e.isWithinMeleeAttackRange(target)) {
						e.doHurtTarget(target);
					}
				}
			}
			return;
		}
		if (action != Action.IDLE) {
			e.level().broadcastEntityEvent(e, WandererIds.IDLE);
		}
		action = Action.IDLE;
		var target = e.getTarget();
		if (target == null) {
			attack = AttackType.CLEAR;
			return;
		}
		checkAttackType(e, target);
	}

	private void checkAttackType(WandererEntity e, LivingEntity target) {
		if (attack != AttackType.CLEAR) return;
		attack = AttackType.REGULAR;
		if (e.getRandom().nextFloat() < e.jumpAttackChance(target)) {
			attack = AttackType.JUMP_READY;
		}
	}

	public AttackType getAttackType(WandererEntity e, LivingEntity target) {
		checkAttackType(e, target);
		if (attack == AttackType.JUMP_READY) {
			if (e.isInWater() || e.isInWall() || !e.onGround() || e.isPassenger())
				return AttackType.REGULAR;
		}
		return attack;
	}

	public void triggerRegular(WandererEntity e) {
		action = Action.ATTACK;
		tick = action.duration;
		attack = AttackType.CLEAR;
		e.level().broadcastEntityEvent(e, WandererIds.ATTACK);
	}

	public void triggerJump(WandererEntity e) {
		action = Action.JUMP;
		tick = action.duration;
		attack = AttackType.HUG_READY;
		e.level().broadcastEntityEvent(e, WandererIds.JUMP);
	}

	public void triggerHug(WandererEntity e) {
		action = Action.HUG;
		tick = action.duration;
		attack = AttackType.CLEAR;
		e.level().broadcastEntityEvent(e, WandererIds.HUG);
	}

	public boolean isReady() {
		return action == Action.IDLE || action == Action.JUMP || tick == 0;
	}

	public enum Action {
		IDLE(0), ATTACK(20), JUMP(40), HUG(20);

		private final int duration;

		Action(int duration) {
			this.duration = duration;
		}

	}

	public enum AttackType {
		CLEAR, REGULAR, JUMP_READY, HUG_READY
	}

}
