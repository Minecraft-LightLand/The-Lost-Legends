package dev.xkmc.lostlegends.modules.deepnether.effect;

import dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether.BaseAbsorbingSlime;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public abstract class BaseSlimeCurseEffect extends MobEffect {

	public BaseSlimeCurseEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void onMobHurt(LivingEntity e, int amplifier, DamageSource source, float amount) {
		if (source.getEntity() == null && amount <= 2) return;
		if (source.getEntity() instanceof BaseAbsorbingSlime slime) {
			slime.heal(slime.healthOfSize(amplifier + 1) * slime.getMaxHealth() / slime.healthOfSize(slime.getSize()));
			return;
		}
		this.spawnSlime(e.level(), e, amplifier, e.getX(), e.getY() + (double) e.getBbHeight() / 2.0, e.getZ());
	}

	protected abstract EntityType<? extends BaseAbsorbingSlime> getSlimeType();

	private void spawnSlime(Level level, LivingEntity e, int amp, double x, double y, double z) {
		var slime = getSlimeType().create(level);
		if (slime != null) {
			RandomSource r = e.getRandom();
			float a = Mth.randomBetween(r, (float) (-Math.PI / 2), (float) (Math.PI / 2));
			Vector3f dir = e.getLookAngle().toVector3f().mul(0.3F).mul(1, 1.5F, 1).rotateY(a);
			slime.moveTo(x, y, z, level.getRandom().nextFloat() * 360, 0);
			slime.setDeltaMovement(new Vec3(dir));
			slime.setSize(amp + 1, true);
			slime.setLastHurtByMob(e);
			slime.setTarget(e);
			level.addFreshEntity(slime);
			slime.playSound(SoundEvents.MAGMA_CUBE_SQUISH);
		}
	}

}
