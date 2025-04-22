package dev.xkmc.lostlegends.foundation.fogblock;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;

public interface IFogBlock {

	FogConfig getFogConfig();

	boolean isClear(Entity entity);

}
