package dev.xkmc.lostlegends.modules.spell.mob.reaper;

import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.particle.engine.ParticleRenderData;
import dev.xkmc.lostlegends.modules.spell.mob.beholder.BeholderUtils;
import net.minecraft.core.Holder;

import java.util.List;

public class ReaperUtils extends BeholderUtils {

	public static ConfiguredEngine<?> spell(
			DataGenContext ctx, int dist, int col, int moveTime,
			int count, int chargeMoveTime, double chargeRadius,
			int delay,
			Holder<ProjectileConfig> proj, ParticleRenderData<?> charge) {
		return new ListLogic(List.of(
				warn(ctx, dist, col, 20),
				charge(ctx, charge, delay - chargeMoveTime, count, chargeRadius, chargeMoveTime, 0.1),
				shoot(ctx, dist, moveTime, proj).delay(IntVariable.of("" + delay))
		));
	}

}
