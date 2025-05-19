package dev.xkmc.lostlegends.init;

import dev.xkmc.l2damagetracker.contents.attack.AttackListener;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.lostlegends.foundation.entity.DamageModifierEntity;

public class LLAttackListener implements AttackListener {

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
