package dev.xkmc.lostlegends.modules.deepnether.entity.slime.base;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;

import java.util.List;

public abstract class BaseSlime extends Slime {

	public BaseSlime(EntityType<? extends BaseSlime> type, Level level) {
		super(type, level);
	}

	protected int healthOfSize(int size) {
		return size * Math.max(size, 4);
	}

	protected int damageOfSize(int size) {
		return size;
	}

	@Override
	public void setSize(int size, boolean heal) {
		super.setSize(size, heal);
		int i = Mth.clamp(size, 1, 127);
		reapplyPosition();
		refreshDimensions();
		setAttributes(i);
		if (heal) {
			setHealth(getMaxHealth());
		}
		xpReward = i * 4;
	}

	protected void setAttributes(int size) {
		getAttribute(Attributes.MAX_HEALTH).setBaseValue(healthOfSize(size));
		getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(damageOfSize(size));
		getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2F + 0.1F * size);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		targetSelector.removeAllGoals(e -> true);
		targetSelector.addGoal(1, new SlimeHurtByTargetGoal(this, Slime.class));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
				this, Player.class, 10, true, false,
				this::mayAttackPrimary));
		targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(
				this, Mob.class, 10, true, false,
				this::mayAttackSecondary));
	}

	@Override
	public void push(Entity e) {
		super.push(e);
		if (isDealsDamage() && !(e instanceof Player) && wouldAttack(e)) {
			dealDamage((LivingEntity) e);
		}
	}

	@Override
	public void playerTouch(Player player) {
		if (isDealsDamage() && wouldAttack(player)) {
			dealDamage(player);
		}
	}

	protected void dealDamage(LivingEntity le) {
		if (!isAlive() || !isWithinMeleeAttackRange(le) || !hasLineOfSight(le)) return;
		DamageSource source = damageSources().mobAttack(this);
		if (le.hurt(source, getAttackDamage())) {
			playSound(SoundEvents.SLIME_ATTACK, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
			if (level() instanceof ServerLevel serverlevel) {
				EnchantmentHelper.doPostAttackEffects(serverlevel, le, source);
			}
			postHurt(le);
		}
	}

	protected boolean isValidTarget(LivingEntity e) {
		return e instanceof IronGolem || e instanceof Player;
	}

	protected boolean mayAttackPrimary(LivingEntity e) {
		return getSize() > 1 && Math.abs(e.getY() - getY()) <= getSize() + 4 && isValidTarget(e);
	}

	protected boolean mayAttackSecondary(LivingEntity e) {
		return getSize() > 1 && isValidTarget(e);
	}

	protected boolean wouldAttack(Entity e) {
		return e == getTarget() || e instanceof LivingEntity le && isValidTarget(le);
	}

	protected void postHurt(LivingEntity le) {
	}

	@Override
	public void jumpFromGround() {
		Vec3 vec3 = getDeltaMovement();
		setDeltaMovement(vec3.x, getJumpPower() + getSize() * extraJumpFactor(), vec3.z);
		hasImpulse = true;
		CommonHooks.onLivingJump(this);
	}

	protected float extraJumpFactor() {
		return 0;
	}

	@Override
	protected boolean isDealsDamage() {
		return isEffectiveAi() && damageOfSize(getSize()) > 1;
	}

	public abstract ResourceLocation getTexture();

	public void onDeathSplit(List<Mob> children) {
	}

}
