package dev.xkmc.lostlegends.foundation.entity.api;

import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeWrapper;

public interface DamageModifierEntity {

	void modify(CreateSourceEvent event, DamageTypeWrapper res);

}
