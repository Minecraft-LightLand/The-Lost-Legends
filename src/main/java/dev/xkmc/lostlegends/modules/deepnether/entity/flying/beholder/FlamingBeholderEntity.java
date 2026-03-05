package dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder;

import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.spell.ai.MobSpellEntry;
import dev.xkmc.lostlegends.modules.spell.mob.beholder.FlameBeholderSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class FlamingBeholderEntity extends BeholderEntity {

	public FlamingBeholderEntity(EntityType<? extends BeholderEntity> type, Level level) {
		super(type, level);
	}


	private static final MobSpellEntry<BeholderEntity> SPELL = new MobSpellEntry<>(FlameBeholderSpell.SPELL, e -> true, 100, 30);

	@Nullable
	public MobSpellEntry<? extends BeholderEntity> getSpell() {
		return SPELL;
	}


	public static final ResourceLocation TEX = LostLegends.loc("textures/entity/deepnether/beholder/flaming_beholder.png");

	@Override
	public ResourceLocation getTexture() {
		return TEX;
	}

}
