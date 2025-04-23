package dev.xkmc.lostlegends.foundation.fogblock;

import dev.xkmc.lostlegends.foundation.util.LavaHandlers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface IFogBlock {

	FogConfig getFogConfig();

	default FogConfig.Range isClear(Entity entity) {
		return entity.isSpectator() ? FogConfig.Range.SPECTATOR :
				entity instanceof LivingEntity le && LavaHandlers.lavaVision(le) ?//TODO
						FogConfig.Range.CLEAR : FogConfig.Range.OBSCURE;
	}

}
