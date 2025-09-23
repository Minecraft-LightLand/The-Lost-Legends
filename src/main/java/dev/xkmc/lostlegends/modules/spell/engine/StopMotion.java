package dev.xkmc.lostlegends.modules.spell.engine;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.entity.core.Motion;
import dev.xkmc.l2magic.content.entity.core.MotionType;
import dev.xkmc.lostlegends.modules.spell.init.LLSpellRegistry;
import net.minecraft.world.phys.Vec3;

public record StopMotion(BooleanVariable cond) implements Motion<StopMotion> {

	public static final MapCodec<StopMotion> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BooleanVariable.CODEC.fieldOf("cond").forGetter(StopMotion::cond)
	).apply(i, StopMotion::new));

	@Override
	public MotionType<StopMotion> type() {
		return LLSpellRegistry.MT_STOP.get();
	}

	@Override
	public ProjectileMovement move(EngineContext ctx, Vec3 vel, Vec3 rot) {
		return cond.test(ctx) ? new ProjectileMovement(Vec3.ZERO, rot) : new ProjectileMovement(vel, rot);
	}

}
