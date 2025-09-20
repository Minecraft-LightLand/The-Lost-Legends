package dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder;

import dev.xkmc.l2magic.content.engine.context.SpellContext;
import dev.xkmc.lostlegends.modules.deepnether.entity.flying.floating.FloaterStrafingAttackGoal;
import dev.xkmc.lostlegends.modules.spell.mob.BeholderSpell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class BeholderAttackGoal extends FloaterStrafingAttackGoal {

	private int attackTick = 0;

	public BeholderAttackGoal(Mob e, double speed, int interval, float rad) {
		super(e, speed, interval, rad);
	}

	@Override
	protected boolean checkPerformAttack(LivingEntity target) {
		return ++attackTick >= 20;
	}

	@Override
	protected void stopAttack() {
	}

	@Override
	protected void startAttack(LivingEntity target) {
		attackTick = 0;
		var spell = mob.level().registryAccess().holderOrThrow(BeholderSpell.SPELL);
		var ctx = SpellContext.castSpell(mob, spell.value(), 0, 1, 64, 0);
		if (ctx != null && !mob.level().isClientSide()) {
			spell.value().execute(spell, ctx);
		}
	}

}
