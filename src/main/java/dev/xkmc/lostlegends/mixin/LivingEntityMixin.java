package dev.xkmc.lostlegends.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.lostlegends.modules.deepnether.util.LavaEffectsHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@WrapOperation(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V", ordinal = 1))
	public void lostlegends$travel$lavaSwim(LivingEntity instance, float v, Vec3 vec3, Operation<Void> original) {
		v = LavaEffectsHelper.lavaSwim(instance, v);
		original.call(instance, v, vec3);
	}

}
