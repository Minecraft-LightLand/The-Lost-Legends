package dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer;

import dev.xkmc.l2core.init.reg.ench.EnchHelper;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

public class WandererConstants {

	public static int attackFrame() {
		return 10;
	}

	public static int jumpDelay() {
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

	public static void launch(WandererEntity mob, LivingEntity target) {
		var dist = mob.distanceTo(target);
		var center = target.position().add(0, target.getBbHeight() / 2 + 0.5, 0);
		var diff = center.subtract(mob.position());
		double dx = diff.x;
		double dy = diff.y;
		double dz = diff.z;
		double g = mob.getGravity();
		double v = Math.min(1.5, dist * 0.2);

		if (target instanceof Player || target instanceof Monster) {
			diff.add(target.getDeltaMovement().multiply(1, 0, 1).scale(dist / v));
		}

		Vec3 dir = diff;
		double c = dx * dx + dz * dz + dy * dy;
		if (g > 0 && c > v * v * 4) {
			double a = g * g / 4;
			double b = dy * g - v * v;
			double delta = b * b - 4 * a * c;
			if (delta > 0) {
				double t21 = (-b + Math.sqrt(delta)) / (2 * a);
				double t22 = (-b - Math.sqrt(delta)) / (2 * a);
				if (t21 > 0 || t22 > 0) {
					double t2 = t21 > 0 ? (t22 > 0 ? Math.min(t21, t22) : t21) : t22;
					dir = new Vec3(dx, dy + g * t2 / 2, dz);
				}
			}
		}

		mob.setDeltaMovement(dir.normalize().scale(v));
	}

	public static boolean mayJumpAttack(WandererEntity mob, LivingEntity e) {
		return mob.onGround() && !e.isInFluidType();
	}

}
