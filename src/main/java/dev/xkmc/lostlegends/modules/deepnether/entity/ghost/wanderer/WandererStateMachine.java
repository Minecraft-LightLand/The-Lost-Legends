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

	public void clientTick(WandererEntity e) {
		if (tick > 0) tick--;
		if (isHolding()) {
			WandererParticles.chargingParticles(e);
		} else if (action == Action.JUMP && tick > 0 && !e.onGround()) {
			WandererParticles.flyingParticles(e);
		}
	}

	public void serverTick(WandererEntity e) {
		if (attack == AttackType.HUG_READY && action != Action.JUMP)
			attack = AttackType.CLEAR;
		var target = e.getTarget();
		if (tick > 0) {
			tick--;
			if (action == Action.JUMP) {
				boolean clear = false;
				if (isHolding()) {
					clear = target == null;
				} else if (e.onGround()) {
					clear = e.getDeltaMovement().y() < 0.01;
				}
				if (clear) {
					tick = 0;
					attack = AttackType.CLEAR;
				}
			}
			if (action == Action.ATTACK) {
				if (tick == WandererConstants.attackFrame()) {
					if (target != null && e.isWithinMeleeAttackRange(target, WandererConstants.attackBuffer())) {
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
		if (e.level().isClientSide()) {
			e.attack.startIfStopped(e.tickCount);
		} else {
			attack = AttackType.CLEAR;
			e.level().broadcastEntityEvent(e, WandererIds.ATTACK);
		}
	}

	public void triggerJump(WandererEntity e) {
		action = Action.JUMP;
		tick = action.duration;
		if (e.level().isClientSide()) {
			e.jump.startIfStopped(e.tickCount);
		} else {
			attack = AttackType.HUG_READY;
			e.level().broadcastEntityEvent(e, WandererIds.JUMP);
		}
	}

	public void triggerHug(WandererEntity e) {
		action = Action.HUG;
		tick = action.duration;
		if (e.level().isClientSide()) {
			e.hug.startIfStopped(e.tickCount);
			WandererParticles.huggingParticles(e);
		} else {
			attack = AttackType.CLEAR;
			e.level().broadcastEntityEvent(e, WandererIds.HUG);
		}
	}

	public boolean isHolding() {
		return action == Action.JUMP && tick >= action.duration - WandererConstants.jumpDelay();
	}

	public boolean isReadyToJump() {
		return action == Action.JUMP && tick == action.duration - WandererConstants.jumpDelay();
	}

	public boolean isReadyToAttack() {
		return action == Action.IDLE || tick == 0 || !isHolding();
	}

	public void resetAttackMode() {
		if (attack != AttackType.HUG_READY)
			attack = AttackType.CLEAR;
	}

	public void onEntityEvent(WandererEntity e, byte id) {
		if (id == WandererIds.ATTACK) triggerRegular(e);
		else e.attack.stop();

		if (id == WandererIds.JUMP) triggerJump(e);
		else e.jump.stop();

		if (id == WandererIds.HUG) triggerHug(e);
		else e.hug.stop();
	}

	public enum Action {
		IDLE(0), ATTACK(20), JUMP(30), HUG(15);

		private final int duration;

		Action(int duration) {
			this.duration = duration;
		}

	}

	public enum AttackType {
		CLEAR, REGULAR, JUMP_READY, HUG_READY
	}

}
