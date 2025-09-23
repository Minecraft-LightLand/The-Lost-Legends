package dev.xkmc.lostlegends.modules.spell.init;

import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.entity.core.MotionType;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderType;
import dev.xkmc.l2magic.init.registrate.EngineReg;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.spell.engine.IgniteBlock;
import dev.xkmc.lostlegends.modules.spell.engine.ModelRenderData;
import dev.xkmc.lostlegends.modules.spell.engine.StopMotion;

public class LLSpellRegistry {

	private static final EngineReg REG = new EngineReg(LostLegends.REGISTRATE);

	public static final Val<MotionType<StopMotion>> MT_STOP = REG.reg("stop", () -> StopMotion.CODEC);
	public static final Val<EngineType<IgniteBlock>> MT_FIRE = REG.reg("fire", () -> IgniteBlock.CODEC);

	public static final Val<ProjectileRenderType<ModelRenderData>> PR_MODEL = REG.reg("model", () -> ModelRenderData.CODEC);

	public static void register() {

	}

}
