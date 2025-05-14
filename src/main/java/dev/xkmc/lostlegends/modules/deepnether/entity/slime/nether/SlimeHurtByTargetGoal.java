package dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.GameRules;

import java.util.EnumSet;

public class SlimeHurtByTargetGoal extends TargetGoal {
	private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
	private int timestamp;
	private final Class<?>[] toIgnoreDamage;

	public SlimeHurtByTargetGoal(Slime e, Class<?>... toIgnore) {
		super(e, true);
		this.toIgnoreDamage = toIgnore;
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	@Override
	public boolean canUse() {
		int i = this.mob.getLastHurtByMobTimestamp();
		LivingEntity livingentity = this.mob.getLastHurtByMob();
		if (i != this.timestamp && livingentity != null) {
			if (livingentity.getType() == EntityType.PLAYER && this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
				return false;
			} else {
				for (Class<?> oclass : this.toIgnoreDamage) {
					if (oclass.isAssignableFrom(livingentity.getClass())) {
						return false;
					}
				}

				return this.canAttack(livingentity, HURT_BY_TARGETING);
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
