package dev.xkmc.lostlegends.modules.deepnether.init;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import net.minecraft.world.item.Item;

public class DNItems extends LLRegBase {

	public final ItemEntry<Item> HEARTH_CRYSTAL, GOLD_DEBRIS, DARK_COBBLE, AMARAST, RESONATING_SOULGEM;

	public DNItems(L2Registrate reg, String path) {
		super(reg, path);
		HEARTH_CRYSTAL = reg.item("hearth_crystal", Item::new)
				.model(this::flatItem)
				.register();
		GOLD_DEBRIS = reg.item("gold_debris", Item::new)
				.model(this::flatItem)
				.register();
		DARK_COBBLE = reg.item("dark_cobble",Item::new)
				.model(this::flatItem)
				.register();
		AMARAST = reg.item("amarast", Item::new)
				.model(this::flatItem)
				.register();
		RESONATING_SOULGEM = reg.item("resonating_soulgem", Item::new)
				.model(this::flatItem)
				.register();

	}
}
