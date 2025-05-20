package dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer;

import dev.xkmc.l2core.init.reg.ench.EnchHelper;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.Tags;

public class WandererConstants {

	public static int attackFrame() {
		return 10;
	}

	public static float attackBuffer() {
		return 0.8f;
	}

	public static int attackModeResetTime() {
		return 60;
	}

	public static float jumpMaxChance() {
		return 0.5f;
	}

	public static double jumpStrength(double distSqr) {
		return Math.sqrt(distSqr) * 0.2;
	}

	public static double jumpStartDistSqr() {
		return 50;
	}

	public static float modifyDamage(DamageSource source, float amount) {
		if (source.is(Tags.DamageTypes.IS_MAGIC)) {
			amount *= 2;
		} else if (source.is(DamageTypeTags.IS_LIGHTNING)) {
			amount *= 1.5f;
		} else if (source.is(L2DamageTypes.DIRECT)) {
			float factor = 0.5f;
			float min = 0;
			if (source.getDirectEntity() instanceof LivingEntity le) {
				int lv = EnchHelper.getLv(le.getMainHandItem(), Enchantments.SMITE);
				factor = Math.min(1, factor + lv * 0.1f);
				min = lv * 4;
			}
			amount = Math.max(min, amount * factor);
		} else if (source.is(DamageTypeTags.IS_PROJECTILE)) {
			amount *= 0.25f;
		}
		return amount;
	}

}
