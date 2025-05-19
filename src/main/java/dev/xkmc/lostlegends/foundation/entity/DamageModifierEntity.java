package dev.xkmc.lostlegends.foundation.entity;

import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeWrapper;

public interface DamageModifierEntity {

	void modify(CreateSourceEvent event, DamageTypeWrapper res);

}
