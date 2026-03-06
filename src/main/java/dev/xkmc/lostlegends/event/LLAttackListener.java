package dev.xkmc.lostlegends.event;

import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.lostlegends.foundation.entity.api.DamageModifierEntity;
import dev.xkmc.lostlegends.foundation.entity.api.INoFriendlyFireEntity;

public class LLAttackListener implements AttackListener {

	@Override
	public boolean onAttack(DamageData.Attack cache) {
		var le = cache.getAttacker();
		if (le instanceof INoFriendlyFireEntity mob) {
			if (mob.noFriendlyFireTo(cache.getTarget()))
				return true;
		}
		return false;
	}

	@Override
	public void onCreateSource(CreateSourceEvent event) {
		if (event.getAttacker() instanceof DamageModifierEntity e) {
			var res = event.getResult();
			if (res != null) {
				e.modify(event, res);
			}
		}
	}
}
