package dev.xkmc.lostlegends.event;

import dev.xkmc.lostlegends.foundation.block.LLFluidType;
import dev.xkmc.lostlegends.foundation.util.LavaHandlers;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;

public class LLMixinHandlers {

	public static double swimUp(LivingEntity le, FluidType fluid, double y) {
		if (fluid == NeoForgeMod.LAVA_TYPE.value())
			return LavaHandlers.lavaSwim(le, y);
		if (fluid instanceof LLFluidType) {
			return y * le.getAttributeValue(NeoForgeMod.SWIM_SPEED);
		}
		return y;
	}

	public static double swimDown(LivingEntity le, FluidType fluid, double y) {
		if (fluid == NeoForgeMod.LAVA_TYPE.value())
			return LavaHandlers.lavaSwim(le, y);
		if (fluid instanceof LLFluidType) {
			return y * le.getAttributeValue(NeoForgeMod.SWIM_SPEED);
		}
		return y;
	}

}
