package dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder;

import dev.xkmc.l2magic.content.engine.context.SpellContext;
import dev.xkmc.lostlegends.foundation.entity.floating.FloaterStrafingAttackGoal;
import dev.xkmc.lostlegends.foundation.entity.floating.MotionBlockGoals;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class BeholderAttackGoal extends FloaterStrafingAttackGoal implements MotionBlockGoals {

	private final BeholderEntity beholder;
	private int attackTick = 0;
	private int attackDelay = 0;

	public BeholderAttackGoal(BeholderEntity e, double speed, int interval, float rad) {
		super(e, speed, interval, rad);
		beholder = e;
	}

	@Override
	protected boolean checkPerformAttack(LivingEntity target) {
		mob.setDeltaMovement(Vec3.ZERO);
		return ++attackTick >= attackDelay;
	}

	@Override
	protected void stopAttack() {
	}

	@Override
	protected boolean startAttack(LivingEntity target) {
		attackTick = 0;
		attackDelay = 0;
		var entry = beholder.getSpell();
		if (entry == null) return false;
		attackDelay = entry.duration();
		var spell = mob.level().registryAccess().holderOrThrow(entry.spell());
		var ctx = SpellContext.castSpell(mob, spell.value(), 0, 1, 64, 0);
		if (ctx != null && !mob.level().isClientSide()) {
			spell.value().execute(spell, ctx);
		}
		return true;
	}

	@Override
	public boolean isImmobile() {
		return inAttack;
	}

}
