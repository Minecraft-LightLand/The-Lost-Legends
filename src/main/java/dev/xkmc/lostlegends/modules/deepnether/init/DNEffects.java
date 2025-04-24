package dev.xkmc.lostlegends.modules.deepnether.init;

import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.modules.deepnether.effect.EmptyEffect;
import dev.xkmc.lostlegends.modules.deepnether.effect.SoulDrainEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class DNEffects extends LLRegBase {

	public final SimpleEntry<MobEffect> SOUL_DRAIN, SOUL_SHELTER;


	public DNEffects(L2Registrate reg, String path) {
		super(reg, path);
		SOUL_DRAIN = new SimpleEntry<>(reg.effect("soul_drain",
				() -> new SoulDrainEffect(MobEffectCategory.HARMFUL, 0x007fff),
				"Reduce player speed, tool use speed, and range").register());
		SOUL_SHELTER = new SimpleEntry<>(reg.effect("soul_shelter",
				() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x007fff),
				"Clears soul fog").register());
	}

}
