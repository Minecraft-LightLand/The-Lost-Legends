package dev.xkmc.lostlegends.modules.deepnether.entity.slime.base;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.function.BooleanSupplier;

public class BaseNetherSlime extends Slime {

	public BaseNetherSlime(EntityType<? extends BaseNetherSlime> type, Level level) {
		super(type, level);
	}

	@Override
	public boolean isSensitiveToWater() {
		return true;
	}

	protected int healthOfSize(int size) {
		return size * Math.max(size, 4);
	}

	protected int damageOfSize(int size) {
		return size * 2 + 1;
	}

	@Override
	public void setSize(int size, boolean heal) {
		super.setSize(size, heal);
		int i = Mth.clamp(size, 1, 127);
		this.reapplyPosition();
		this.refreshDimensions();
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(healthOfSize(i));
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2F + 0.1F * (float) i);
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(damageOfSize(i));
		this.getAttribute(Attributes.ARMOR).setBaseValue(size * 3);
		if (heal) {
			this.setHealth(this.getMaxHealth());
		}
		this.xpReward = i * 4;
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
		DamageSource source = this.damageSources().mobAttack(this);
		if (le.hurt(source, this.getAttackDamage())) {
			this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
			if (this.level() instanceof ServerLevel serverlevel) {
				EnchantmentHelper.doPostAttackEffects(serverlevel, le, source);
			}
			postHurt(le);
		}
	}

	protected boolean isValidTarget(LivingEntity e) {
		return e instanceof IronGolem || e instanceof Player;
	}

	protected boolean mayAttackPrimary(LivingEntity e) {
		return getSize() > 1 && Math.abs(e.getY() - this.getY()) <= getSize() + 4 && isValidTarget(e);
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
	public float getLightLevelDependentMagicValue() {
		return 1.0F;
	}

	@Override
	protected ParticleOptions getParticleType() {
		return ParticleTypes.FLAME;
	}

	@Override
	public boolean isOnFire() {
		return false;
	}

	@Override
	public void jumpFromGround() {
		Vec3 vec3 = this.getDeltaMovement();
		float f = (float) this.getSize() * 0.1F;
		this.setDeltaMovement(vec3.x, this.getJumpPower() + f, vec3.z);
		this.hasImpulse = true;
		CommonHooks.onLivingJump(this);
	}

	@Override
	@Deprecated
	protected void jumpInLiquid(TagKey<Fluid> tag) {
		this.jumpInLiquidInternal(() -> tag == FluidTags.LAVA, () -> super.jumpInLiquid(tag));
	}

	private void jumpInLiquidInternal(BooleanSupplier isLava, Runnable onSuper) {
		if (isLava.getAsBoolean()) {
			Vec3 vec3 = this.getDeltaMovement();
			this.setDeltaMovement(vec3.x, 0.22F + (float) this.getSize() * 0.05F, vec3.z);
			this.hasImpulse = true;
		} else {
			onSuper.run();
		}
	}

	@Override
	public void jumpInFluid(FluidType type) {
		this.jumpInLiquidInternal(() -> type == NeoForgeMod.LAVA_TYPE.value(), () -> super.jumpInFluid(type));
	}

	@Override
	protected boolean isDealsDamage() {
		return this.isEffectiveAi();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_32992_) {
		return this.isTiny() ? SoundEvents.MAGMA_CUBE_HURT_SMALL : SoundEvents.MAGMA_CUBE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return this.isTiny() ? SoundEvents.MAGMA_CUBE_DEATH_SMALL : SoundEvents.MAGMA_CUBE_DEATH;
	}

	@Override
	protected SoundEvent getSquishSound() {
		return this.isTiny() ? SoundEvents.MAGMA_CUBE_SQUISH_SMALL : SoundEvents.MAGMA_CUBE_SQUISH;
	}

	@Override
	protected SoundEvent getJumpSound() {
		return SoundEvents.MAGMA_CUBE_JUMP;
	}

}
