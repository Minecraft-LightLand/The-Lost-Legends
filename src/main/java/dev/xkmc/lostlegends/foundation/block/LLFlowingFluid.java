package dev.xkmc.lostlegends.foundation.block;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface LLFlowingFluid {

	void entityInside(Entity e);

	boolean canStandOn(LivingEntity le);

}
