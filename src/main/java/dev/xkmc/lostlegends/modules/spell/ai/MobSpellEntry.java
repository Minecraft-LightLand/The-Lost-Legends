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

	public static <T extends Mob> MobSpellEntry<T> near(ResourceKey<SpellAction> spell, int weight, int duration, int dist) {
		return new MobSpellEntry<>(spell, e -> e.getTarget() != null && e.getTarget().distanceTo(e) < dist, weight, duration);
	}

	public static <T extends Mob> MobSpellEntry<T> far(ResourceKey<SpellAction> spell, int weight, int duration, int dist) {
		return new MobSpellEntry<>(spell, e -> e.getTarget() != null && e.getTarget().distanceTo(e) > dist, weight, duration);
	}

}
