package dev.xkmc.lostlegends.modules.item;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.lostlegends.foundation.module.LLModuleBase;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class LLEquipments extends LLModuleBase {

	private static final String ID = "equipment";

	public static SimpleEntry<CreativeModeTab> TAB = LostLegends.REGISTRATE.buildModCreativeTab(ID,
			"The Lost Legends - Equipments", b -> b.icon(LLEquipments.GLORIOUS_INGOT::asStack));

	private static final LLRegBase REG = new LLRegBase(LostLegends.REGISTRATE, ID);

	public static final ItemEntry<Item> SHINING_INGOT, GLORIOUS_INGOT, SOUL_RUST_INGOT, SOUL_ALLOY_INGOT;

	static {
		var reg = LostLegends.REGISTRATE;

		SHINING_INGOT = reg.item("shining_ingot", Item::new)
				.model(REG::flatItem)
				.register();

		SOUL_RUST_INGOT = reg.item("soul_rust_ingot", Item::new)
				.model(REG::flatItem)
				.register();

		GLORIOUS_INGOT = reg.item("glorious_ingot", Item::new)
				.properties(Item.Properties::fireResistant)
				.model(REG::flatItem)
				.register();

		SOUL_ALLOY_INGOT = reg.item("soul_alloy_ingot", Item::new)
				.model(REG::flatItem)
				.register();


	}

}
