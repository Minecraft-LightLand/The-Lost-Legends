package dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;

public class WandererParticles {

	public static void chargingParticles(WandererEntity e) {
		double r = 1.5, v = 0.1;
		int n = 3;
		var rand = e.getRandom();
		for (int i = 0; i < n; i++) {
			Vec3 dir = new Vec3(rand.nextGaussian(), 0, rand.nextGaussian()).normalize()
					.add(0, 0.3, 0);
			Vec3 pos = e.position().add(dir.scale(r)).add(0, 0.1, 0);
			Vec3 vel = dir.scale(-v);
			e.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
		}

	}

	public static void flyingParticles(WandererEntity e) {
		Vec3 pos = e.position().add(0, 0.1, 0);
		Vec3 vel = e.getDeltaMovement().scale(0.1);
		e.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
	}

	public static void huggingParticles(WandererEntity e) {
		double v = 0.3;
		int n = 60;
		var rand = e.getRandom();
		for (int i = 0; i < n; i++) {
			Vec3 dir = new Vec3(rand.nextGaussian(), rand.nextGaussian() / 2, rand.nextGaussian()).normalize();
			Vec3 pos = e.position().add(0, 1.3, 0);
			Vec3 vel = dir.scale(v);
			e.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
		}
	}

}
