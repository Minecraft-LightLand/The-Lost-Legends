package dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder;

import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.spell.ai.MobSpellEntry;
import dev.xkmc.lostlegends.modules.spell.mob.beholder.PoisonBeholderSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PoisonBeholderEntity extends BeholderEntity {

	public PoisonBeholderEntity(EntityType<? extends BeholderEntity> type, Level level) {
		super(type, level);
	}

	private static final MobSpellEntry<BeholderEntity> SPELL = new MobSpellEntry<>(PoisonBeholderSpell.SPELL, e -> true, 100, 20);

	@Nullable
	public MobSpellEntry<? extends BeholderEntity> getSpell() {
		return SPELL;
	}


	public static final ResourceLocation TEX = LostLegends.loc("textures/entity/deepnether/beholder/poison_beholder.png");

	@Override
	public ResourceLocation getTexture() {
		return TEX;
	}

}
