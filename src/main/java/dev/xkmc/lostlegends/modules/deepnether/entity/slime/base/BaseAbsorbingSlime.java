package dev.xkmc.lostlegends.modules.deepnether.entity.slime.base;

import dev.xkmc.lostlegends.init.LLDamageTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BaseAbsorbingSlime extends BaseNetherSlime {

	private int maxSize = 6;

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
		return maxSize > 0 && getHealth() < getMaxHealth() || getSize() < maxSize;
	}

	protected boolean mayAbsorbSlime(Slime n) {
		return getSize() * 0.6 > n.getSize() && getHealth() * 0.6 > n.getHealth() ||
				getSize() > n.getSize() && getAttackDamage() > n.getHealth();
	}

	protected void absorbSlime(Slime e) {
		float health = e.getHealth();
		e.discard();
		if (getMaxHealth() - getHealth() < health && getSize() < maxSize) {
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
		float old = getHealth();
		float max = getMaxHealth();
		super.actuallyHurt(source, amount);
		int size = getSize();
		float health = getHealth();
		if (health > 0) {
			int current = healthOfSize(size);
			while (size > 1 && scaledHealth(healthOfSize(size - 1)) > health) {
				size--;
			}
			if (size < getSize()) {
				setSize(size, false);
			}
			float ratio = (old - health) / getMaxHealth() * current;
			splitOnHurt(size, ratio, source);
		}
	}

	public void splitOnHurt(int max, float ratio, DamageSource source) {
		if (source.is(LLDamageTypes.NO_SLIME_SPLIT)) return;
		if (!source.is(DamageTypeTags.IS_PROJECTILE)) ratio *= 0.5f;
		int ans = 0;
		for (int i = 1; i <= max; i++) {
			if (healthOfSize(i) < ratio) ans = i;
			else break;
		}
		if (ans == 0) return;
		var e = getType().create(level());
		if (!(e instanceof BaseAbsorbingSlime s)) return;
		if (isPersistenceRequired())
			e.setPersistenceRequired();
		e.setCustomName(getCustomName());
		e.setNoAi(isNoAi());
		e.setInvulnerable(isInvulnerable());
		e.setSize(ans, true);
		s.maxSize = 0;
		e.moveTo(getX(), getY() + getBbHeight(), getZ(), random.nextFloat() * 360, 0);
		if (EventHooks.onMobSplit(this, List.of(e)).isCanceled()) return;
		level().addFreshEntity(e);
	}

	@Override
	public void remove(RemovalReason p_149847_) {
		super.remove(p_149847_);
	}

	protected double scaledHealth(float base) {
		var ins = getAttribute(Attributes.MAX_HEALTH);
		if (ins == null) return base;
		double old = ins.getBaseValue();
		ins.setBaseValue(base);
		double ans = ins.getValue();
		ins.setBaseValue(old);
		return ans;
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("MaxAbsorbSize", Tag.TAG_ANY_NUMERIC)) {
			maxSize = tag.getInt("MaxAbsorbSize");
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("MaxAbsorbSize", maxSize);
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance ins, MobSpawnType type, @Nullable SpawnGroupData data) {
		int diff = ins.getDifficulty().getId();
		maxSize = level.getRandom().nextInt(4, 5 + diff);
		if (level.getRandom().nextFloat() > diff * diff * 0.12f) maxSize = 0;
		return super.finalizeSpawn(level, ins, type, data);
	}

}
