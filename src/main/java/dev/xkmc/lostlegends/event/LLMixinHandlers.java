package dev.xkmc.lostlegends.event;

import dev.xkmc.lostlegends.foundation.block.LLFlowingFluid;
import dev.xkmc.lostlegends.foundation.block.LLFluidType;
import dev.xkmc.lostlegends.modules.deepnether.util.LavaEffectsHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;

public class LLMixinHandlers {

	public static double swimUp(LivingEntity le, FluidType fluid, double y) {
		if (fluid == NeoForgeMod.LAVA_TYPE.value())
			return LavaEffectsHelper.lavaSwim(le, y);
		if (fluid instanceof LLFluidType) {
			return y * le.getAttributeValue(NeoForgeMod.SWIM_SPEED);
		}
		return y;
	}

	public static double swimDown(LivingEntity le, FluidType fluid, double y) {
		if (fluid == NeoForgeMod.LAVA_TYPE.value())
			return LavaEffectsHelper.lavaSwim(le, y);
		if (fluid instanceof LLFluidType) {
			return y * le.getAttributeValue(NeoForgeMod.SWIM_SPEED);
		}
		return y;
	}

	public static boolean canStandOnFluid(LivingEntity le, FluidState state) {
		if (le.isShiftKeyDown() || le.getFluidTypeHeight(state.getFluidType()) >= 0.5)
			return false;
		if (state.getFluidType() == NeoForgeMod.LAVA_TYPE.value()) {
			return LavaEffectsHelper.lavaWalk(le);
		}
		if (state.getType() instanceof LLFlowingFluid fluid) {
			return fluid.canStandOn(le);
		}
		return false;
	}

	public static float blockJumpPower(LivingEntity le, float old) {
		var state = le.level().getBlockState(le.blockPosition()).getFluidState();
		if (canStandOnFluid(le, state)) {
			return Math.max(1.2f, old);
		}
		return old;
	}
}
