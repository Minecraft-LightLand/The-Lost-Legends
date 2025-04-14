package dev.xkmc.lostlegends.modules.deepnether.init;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import net.minecraft.world.item.Item;

public class DNItems extends LLRegBase {

	public final ItemEntry<Item> DARK_COBBLE;

	public DNItems(L2Registrate reg, String path) {
		super(reg, path);
		DARK_COBBLE = reg.item("dark_cobble",Item::new)
				.model(this::flatItem)
				.register();

	}
}
