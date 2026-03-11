package dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder;

import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.spell.ai.MobSpellEntry;
import dev.xkmc.lostlegends.modules.spell.ai.MobSpellPool;
import dev.xkmc.lostlegends.modules.spell.mob.beholder.FlameBeholderSpell;
import dev.xkmc.lostlegends.modules.spell.mob.beholder.SkullBombSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SkullBeholderEntity extends BeholderEntity {

	private static final MobSpellPool<BeholderEntity> POOL = new MobSpellPool<>(List.of(
			MobSpellEntry.far(FlameBeholderSpell.SPELL, 100, 30, 12),
			MobSpellEntry.near(SkullBombSpell.SPELL, 100, 20, 16)
	));

	public SkullBeholderEntity(EntityType<? extends BeholderEntity> type, Level level) {
		super(type, level);
	}

	@Nullable
	public MobSpellEntry<? extends BeholderEntity> getSpell() {
		return POOL.poll(this);
	}


	public static final ResourceLocation TEX = LostLegends.loc("textures/entity/deepnether/beholder/skull_beholder.png");

	@Override
	public ResourceLocation getTexture() {
		return TEX;
	}

}
