package dev.xkmc.lostlegends.modules.deepnether.init;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.modules.deepnether.item.PortalDissonator;
import net.minecraft.world.item.Item;

public class DNItems extends LLRegBase {

	public final ItemEntry<Item> HEARTH_CRYSTAL, GOLD_DEBRIS, DARK_COBBLE, AMARAST, RESONANT_SOULGEM, SOUL_LAZURITE;
	public final ItemEntry<PortalDissonator> PORTAL_DISSONATOR;

	public DNItems(L2Registrate reg, String path) {
		super(reg, path);
		HEARTH_CRYSTAL = reg.item("hearth_crystal", Item::new)
				.model(this::flatItem)
				.register();
		GOLD_DEBRIS = reg.item("gold_debris", Item::new)
				.model(this::flatItem)
				.register();
		DARK_COBBLE = reg.item("dark_cobble", Item::new)
				.model(this::flatItem)
				.register();
		AMARAST = reg.item("amarast", Item::new)
				.model(this::flatItem)
				.register();
		RESONANT_SOULGEM = reg.item("resonant_soulgem", Item::new)
				.model(this::flatItem)
				.register();
		SOUL_LAZURITE = reg.item("soul_lazurite", Item::new)
				.model(this::flatItem)
				.register();

		PORTAL_DISSONATOR = descItem("portal_dissonator", PortalDissonator::new, "Right click nether / deep nether portal blocks to destroy them.")
				.model(this::flatItem)
				.register();

	}


	public <T extends Item> ItemBuilder<T, L2Registrate> descItem(String id, NonNullFunction<Item.Properties, T> factory, String str) {
		var builder = reg.item(id, factory);
		builder.getOwner().addRawLang("item." + builder.getOwner().getModid() + "." + builder.getName() + ".desc", str);
		return builder;
	}

}
