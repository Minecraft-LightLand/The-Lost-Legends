package dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder;

import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.lostlegends.modules.spell.mob.beholder.PoisonBeholderSpell;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class PoisonBeholderEntity extends BeholderEntity {

	public PoisonBeholderEntity(EntityType<? extends BeholderEntity> type, Level level) {
		super(type, level);
	}

	@Override
	public ResourceKey<SpellAction> getSpell() {
		return PoisonBeholderSpell.SPELL;
	}

}
