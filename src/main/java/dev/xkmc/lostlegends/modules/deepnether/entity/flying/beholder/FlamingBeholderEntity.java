package dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder;

import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.spell.mob.beholder.FlameBeholderSpell;
import dev.xkmc.lostlegends.modules.spell.mob.beholder.PoisonBeholderSpell;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class FlamingBeholderEntity extends BeholderEntity {

	public FlamingBeholderEntity(EntityType<? extends BeholderEntity> type, Level level) {
		super(type, level);
	}

	@Override
	public ResourceKey<SpellAction> getSpell() {
		return FlameBeholderSpell.SPELL;
	}

	public static final ResourceLocation TEX = LostLegends.loc("textures/entity/deepnether/beholder/flaming_beholder.png");

	@Override
	public ResourceLocation getTexture() {
		return TEX;
	}

}
