
package dev.xkmc.lostlegends.modules.deepnether.entity.flying.base;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;

import java.util.EnumSet;

public class FlyerHurtByTargetGoal extends TargetGoal {

	private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

	private int timestamp;
	private final Class<?>[] toIgnoreDamage;

	public FlyerHurtByTargetGoal(Mob mob, Class<?>... allies) {
		super(mob, true);
		this.toIgnoreDamage = allies;
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	@Override
	public boolean canUse() {
		int i = this.mob.getLastHurtByMobTimestamp();
		LivingEntity attacker = this.mob.getLastHurtByMob();
		if (i != this.timestamp && attacker != null) {
			if (attacker.getType() == EntityType.PLAYER && this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
				return false;
			} else {
				for (Class<?> oclass : this.toIgnoreDamage) {
					if (oclass.isAssignableFrom(attacker.getClass())) {
						return false;
					}
				}
				return this.canAttack(attacker, HURT_BY_TARGETING);
			}
		} else {
			return false;
		}
	}

	@Override
	public void start() {
		this.mob.setTarget(this.mob.getLastHurtByMob());
		this.targetMob = this.mob.getTarget();
		this.timestamp = this.mob.getLastHurtByMobTimestamp();
		this.unseenMemoryTicks = 300;
		super.start();
	}

}
