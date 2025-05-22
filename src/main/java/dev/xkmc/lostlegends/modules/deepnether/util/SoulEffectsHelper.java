package dev.xkmc.lostlegends.modules.deepnether.util;

import dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer.WandererEntity;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SoulEffectsHelper {

	public static void deal(Entity e) {
		if (e instanceof LivingEntity le) {
			if (isClear(le)) {
				return;
			}
			le.addEffect(new MobEffectInstance(DeepNether.EFFECTS.SOUL_DRAIN, 40));
			le.setDeltaMovement(le.getDeltaMovement().add(0, -0.012, 0));
			float dmg = 4;
			if (le.fireImmune() || le.isInvulnerableTo(e.damageSources().lava())) dmg /= 2;
			boolean hurt = e.hurt(e.damageSources().magic(), dmg) || e.hurt(e.damageSources().lava(), dmg);
			if (hurt) {
				e.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2 + e.getRandom().nextFloat() * 0.4F);
			}
		}
	}

	public static boolean canStand(LivingEntity le) {
		return le.hasEffect(DeepNether.EFFECTS.SOUL_SHELTER);
	}

	public static boolean isClear(LivingEntity le) {
		if (le instanceof WandererEntity) return true;//TODO tagify
		return le.hasEffect(DeepNether.EFFECTS.SOUL_SHELTER);
	}

	public static BlockState getFluidBlock() {
		return DeepNether.BLOCKS.LIQUID_SOUL.getSource().defaultFluidState().createLegacyBlock();
	}
}
