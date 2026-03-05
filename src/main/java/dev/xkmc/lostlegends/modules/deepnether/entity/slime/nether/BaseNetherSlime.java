package dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether;

import dev.xkmc.lostlegends.foundation.entity.api.LavaSwimEntity;
import dev.xkmc.lostlegends.foundation.entity.slime.BaseSlime;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.function.BooleanSupplier;

public abstract class BaseNetherSlime extends BaseSlime implements LavaSwimEntity {

	public BaseNetherSlime(EntityType<? extends BaseNetherSlime> type, Level level) {
		super(type, level);
		setPathfindingMalus(PathType.LAVA, 0);
		setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
		setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
	}

	@Override
	public boolean canStandOnFluid(FluidState state) {
		var type = state.getFluidType();
		return type == NeoForgeMod.LAVA_TYPE.value();
	}

	@Override
	public boolean canSwimInFluidType(FluidType type) {
		return type == NeoForgeMod.LAVA_TYPE.value() ||
				super.canSwimInFluidType(type);
	}

	@Override
	public float getLavaSwimFactor() {
		return 3;
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
	protected ParticleOptions getParticleType() {
		return ParticleTypes.FLAME;
	}

	@Override
	public boolean isOnFire() {
		return false;
	}

	@Override
	@Deprecated
	protected void jumpInLiquid(TagKey<Fluid> tag) {
		jumpInLiquidInternal(() -> tag == FluidTags.LAVA, () -> super.jumpInLiquid(tag));
	}

	@Override
	public void jumpInFluid(FluidType type) {
		jumpInLiquidInternal(() -> type == NeoForgeMod.LAVA_TYPE.value(), () -> super.jumpInFluid(type));
	}

	private void jumpInLiquidInternal(BooleanSupplier isLava, Runnable onSuper) {
		if (isLava.getAsBoolean()) {
			Vec3 vec3 = getDeltaMovement();
			setDeltaMovement(vec3.x, 0.22F + (float) getSize() * 0.05F, vec3.z);
			hasImpulse = true;
		} else {
			onSuper.run();
		}
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_32992_) {
		return isTiny() ? SoundEvents.MAGMA_CUBE_HURT_SMALL : SoundEvents.MAGMA_CUBE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return isTiny() ? SoundEvents.MAGMA_CUBE_DEATH_SMALL : SoundEvents.MAGMA_CUBE_DEATH;
	}

	@Override
	protected SoundEvent getSquishSound() {
		return isTiny() ? SoundEvents.MAGMA_CUBE_SQUISH_SMALL : SoundEvents.MAGMA_CUBE_SQUISH;
	}

	@Override
	protected float extraJumpFactor() {
		return 0.1f;
	}

	@Override
	protected SoundEvent getJumpSound() {
		return SoundEvents.MAGMA_CUBE_JUMP;
	}

	public abstract ResourceLocation getTexture();

}
