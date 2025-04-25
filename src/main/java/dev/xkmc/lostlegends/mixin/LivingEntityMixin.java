package dev.xkmc.lostlegends.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.lostlegends.event.LLMixinHandlers;
import dev.xkmc.lostlegends.modules.deepnether.util.LavaEffectsHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@WrapOperation(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V", ordinal = 1))
	public void lostlegends$travel$lavaSwim(LivingEntity instance, float v, Vec3 vec3, Operation<Void> original) {
		v = LavaEffectsHelper.lavaSwim(instance, v);
		original.call(instance, v, vec3);
	}

	@Inject(method = "canStandOnFluid", at = @At(value = "HEAD"), cancellable = true)
	public void lostlegends$canStandOnFluid(FluidState state, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (LLMixinHandlers.canStandOnFluid(self, state)) {
			cir.setReturnValue(true);
		}
	}

	@WrapOperation(method = "getJumpPower(F)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getBlockJumpFactor()F"))
	public float lostlegends$jump$fluid(LivingEntity e, Operation<Float> original) {
		return LLMixinHandlers.blockJumpPower(e, original.call(e));
	}


}
