package dev.xkmc.lostlegends.modules.deepnether.entity.flying.base;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class FlyerAttackGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

	public FlyerAttackGoal(Mob self, Class<T> cls, boolean mustSee) {
		super(self, cls, mustSee);
	}

	public FlyerAttackGoal(Mob self, Class<T> cls, int interval, boolean mustSee, boolean mustReach, @Nullable Predicate<LivingEntity> pred) {
		super(self, cls, interval, mustSee, mustReach, pred);
	}

	@Override
	protected AABB getTargetSearchArea(double range) {
		return this.mob.getBoundingBox().inflate(range, range, range);
	}

}
