package dev.xkmc.lostlegends.modules.deepnether.entity.slime.base;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

public class BaseAbsorbingSlime extends BaseNetherSlime {

	private static int MAX_SIZE = 6;

	public BaseAbsorbingSlime(EntityType<? extends BaseAbsorbingSlime> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(
				this, Slime.class, 10, true, false,
				this::mayHunt));
	}

	protected boolean mayHunt(LivingEntity e) {
		return e instanceof Slime n && wantToAbsorb() && mayAbsorbSlime(n);
	}

	protected boolean wantToAbsorb() {
		return getHealth() < getMaxHealth() || getSize() < MAX_SIZE;
	}

	protected boolean mayAbsorbSlime(Slime n) {
		return getSize() * 0.6 > n.getSize() && getHealth() * 0.6 > n.getHealth() ||
				getSize() > n.getSize() && getAttackDamage() > n.getHealth();
	}

	protected void absorbSlime(Slime e) {
		float health = e.getHealth();
		e.discard();
		if (getMaxHealth() - getHealth() < health && getSize() < MAX_SIZE) {
			setSize(getSize() + 1, false);
		}
		heal(health);
	}

	@Override
	protected void dealDamage(LivingEntity le) {
		if (le instanceof Slime e && wantToAbsorb() && mayAbsorbSlime(e)) {
			absorbSlime(e);
			return;
		}
		super.dealDamage(le);
	}

	@Override
	protected void actuallyHurt(DamageSource source, float amount) {
		super.actuallyHurt(source, amount);
		int size = getSize();
		float health = getHealth();
		if (health > 0) {
			while (size > 1 && healthOfSize(size - 1) > health) {
				size--;
			}
			if (size < getSize()) {
				setSize(size, false);
			}
		}
	}

}
