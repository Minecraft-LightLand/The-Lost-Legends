package dev.xkmc.lostlegends.foundation.util;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class LavaHandlers {

	public static float lavaSwim(LivingEntity le, double v) {
		return (float) (le.hasEffect(MobEffects.FIRE_RESISTANCE) ? v * 2 : v);
	}

	public static boolean canLavaSwim(LivingEntity le) {
		return le.hasEffect(MobEffects.FIRE_RESISTANCE);
	}

	public static boolean noFire(LivingEntity le) {
		return le.hasEffect(MobEffects.FIRE_RESISTANCE);
	}

	public static boolean lavaVision(LivingEntity le) {
		return le.hasEffect(MobEffects.FIRE_RESISTANCE);
	}

}
