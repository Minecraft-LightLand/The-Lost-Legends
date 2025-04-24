package dev.xkmc.lostlegends.modules.deepnether.util;

import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class LavaEffectsHelper {

	public static float lavaSwim(LivingEntity le, double v) {
		return (float) (le.hasEffect(DeepNether.EFFECTS.LAVA_AFFINITY) ? v * 2 : v);
	}

	public static boolean canLavaSwim(LivingEntity le) {
		return le.hasEffect(DeepNether.EFFECTS.LAVA_AFFINITY);
	}

	public static boolean noFire(LivingEntity le) {
		return le.hasEffect(DeepNether.EFFECTS.LAVA_AFFINITY) || le.hasEffect(DeepNether.EFFECTS.SOUL_SHELTER);
	}

	public static boolean lavaVision(LivingEntity le) {
		return le.hasEffect(DeepNether.EFFECTS.LAVA_AFFINITY);
	}

	public static boolean fireImmune(Entity entity, DamageSource source) {
		if (entity instanceof LivingEntity le && source.is(DamageTypeTags.IS_FIRE)) {
			return le.hasEffect(DeepNether.EFFECTS.LAVA_AFFINITY) || le.hasEffect(DeepNether.EFFECTS.SOUL_SHELTER);
		}
		return false;
	}

}
