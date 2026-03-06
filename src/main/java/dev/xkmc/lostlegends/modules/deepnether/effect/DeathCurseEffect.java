package dev.xkmc.lostlegends.modules.deepnether.effect;

import dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether.BaseAbsorbingSlime;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;

public class DeathCurseEffect extends BaseSlimeCurseEffect {

	public DeathCurseEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	protected EntityType<? extends BaseAbsorbingSlime> getSlimeType() {
		return DeepNether.ENTITY.PUTRID_SLIME.get();
	}

}
