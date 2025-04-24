package dev.xkmc.lostlegends.foundation.fogblock;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface IFogBlock {

	FogConfig getFogConfig();

	default FogConfig.Range fogRange(Entity entity) {
		return entity.isSpectator() ? FogConfig.Range.SPECTATOR :
				entity instanceof LivingEntity le && isClear(le) ?
						FogConfig.Range.CLEAR : FogConfig.Range.OBSCURE;
	}

	default boolean isClear(LivingEntity le) {
		return false;
	}

}
