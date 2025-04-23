package dev.xkmc.lostlegends.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.lostlegends.foundation.util.LavaHandlers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ILivingEntityExtension.class)
public interface ILivingEntityExtensionMixin {

	@WrapOperation(method = "jumpInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;"))
	default Vec3 lostlegends$jumpInFluid$lavaSwim(Vec3 vec, double x, double y, double z, Operation<Vec3> original) {
		if (this instanceof LivingEntity le)
			y = LavaHandlers.lavaSwim(le, y);
		return original.call(vec, x, y, z);
	}

	@WrapOperation(method = "sinkInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;"))
	default Vec3 lostlegends$sinkInFluid$lavaSwim(Vec3 vec, double x, double y, double z, Operation<Vec3> original) {
		if (this instanceof LivingEntity le)
			y = LavaHandlers.lavaSwim(le, y);
		return original.call(vec, x, y, z);
	}

}
