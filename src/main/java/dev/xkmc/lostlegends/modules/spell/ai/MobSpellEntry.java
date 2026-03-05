package dev.xkmc.lostlegends.modules.spell.ai;

import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Mob;

import java.util.function.Predicate;

public record MobSpellEntry<T extends Mob>(
		ResourceKey<SpellAction> spell,
		Predicate<T> condition,
		int weight,
		int duration
) {

}
