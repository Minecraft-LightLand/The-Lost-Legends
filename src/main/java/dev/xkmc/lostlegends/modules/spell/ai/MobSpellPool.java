package dev.xkmc.lostlegends.modules.spell.ai;

import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record MobSpellPool<T extends Mob>(
		List<MobSpellEntry<T>> pool
) {

	@Nullable
	public MobSpellEntry<T> poll(T mob) {
		List<MobSpellEntry<T>> valid = new ArrayList<>();
		int weights = 0;
		for (var e : pool) {
			if (e.condition().test(mob)) {
				valid.add(e);
				weights += e.weight();
			}
		}
		int sel = mob.getRandom().nextInt(weights);
		for (var e : valid) {
			if (sel < e.weight()) {
				return e;
			}
			sel -= e.weight();
		}
		return null;
	}

}
