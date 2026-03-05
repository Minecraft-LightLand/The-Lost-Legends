package dev.xkmc.lostlegends.modules.deepnether.entity.flying.reaper;

import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder.BeholderEntity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ReaperEntity extends BeholderEntity {

	public ReaperEntity(EntityType<? extends BeholderEntity> type, Level level) {
		super(type, level);
	}

	@Override
	public ResourceKey<SpellAction> getSpell() {
		return super.getSpell();
	}

	@Override
	public int spellDuration() {
		return super.spellDuration();
	}

	public static final ResourceLocation TEX = LostLegends.loc("textures/entity/deepnether/reaper/reaper.png");

	@Override
	public ResourceLocation getTexture() {
		return super.getTexture();
	}

}
