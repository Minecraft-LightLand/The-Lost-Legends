package dev.xkmc.lostlegends.foundation.entity.flying;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class FlyerLookAtEntityGoal extends Goal {

	public static final float DEFAULT_PROBABILITY = 0.02F;

	protected final Mob mob;
	@Nullable
	protected Entity lookAt;
	protected final float lookDistance;
	private int lookTime;
	protected final float probability;
	private final boolean onlyHorizontal;
	protected final Class<? extends LivingEntity> lookAtType;
	protected final TargetingConditions lookAtContext;

	public FlyerLookAtEntityGoal(Mob e, Class<? extends LivingEntity> cls, float dist) {
		this(e, cls, dist, 0.02F);
	}

	public FlyerLookAtEntityGoal(Mob e, Class<? extends LivingEntity> cls, float dist, float prob) {
		this(e, cls, dist, prob, false);
	}

	public FlyerLookAtEntityGoal(Mob e, Class<? extends LivingEntity> cls, float dist, float prob, boolean hor) {
		this.mob = e;
		this.lookAtType = cls;
		this.lookDistance = dist;
		this.probability = prob;
		this.onlyHorizontal = hor;
		this.setFlags(EnumSet.of(Goal.Flag.LOOK));
		if (cls == Player.class) {
			this.lookAtContext = TargetingConditions.forNonCombat().range(dist).selector(pl -> EntitySelector.notRiding(e).test(pl));
		} else {
			this.lookAtContext = TargetingConditions.forNonCombat().range(dist);
		}
	}

	@Override
	public boolean canUse() {
		if (this.mob.getRandom().nextFloat() >= this.probability) {
			return false;
		} else {
			if (this.mob.getTarget() != null) {
				this.lookAt = this.mob.getTarget();
			}

			if (this.lookAtType == Player.class) {
				this.lookAt = this.mob.level().getNearestPlayer(this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
			} else {
				this.lookAt = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(this.lookAtType,
								this.mob.getBoundingBox().inflate(this.lookDistance, 3.0, this.lookDistance),
								e -> true),
						this.lookAtContext, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
			}

			return this.lookAt != null;
		}
	}

	@Override
	public boolean canContinueToUse() {
		if (lookAt == null) return false;
		if (!this.lookAt.isAlive()) return false;
		return this.mob.distanceToSqr(lookAt) <= this.lookDistance * this.lookDistance && this.lookTime > 0;
	}

	@Override
	public void start() {
		this.lookTime = this.adjustedTickDelay(40 + this.mob.getRandom().nextInt(40));
	}

	@Override
	public void stop() {
		this.lookAt = null;
	}

	@Override
	public void tick() {
		if (lookAt == null) return;
		if (!this.lookAt.isAlive()) return;
		double d0 = this.onlyHorizontal ? this.mob.getEyeY() : this.lookAt.getEyeY();
		this.mob.getLookControl().setLookAt(this.lookAt.getX(), d0, this.lookAt.getZ());
		this.lookTime--;
	}
}
