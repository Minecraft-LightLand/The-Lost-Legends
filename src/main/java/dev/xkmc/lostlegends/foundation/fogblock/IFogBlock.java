package dev.xkmc.lostlegends.foundation.fogblock;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;

public interface IFogBlock {

	FogType getFogType();

	FogConfig getFogConfig();

	boolean isClear(Entity entity);

}
