package dev.xkmc.lostlegends.foundation.util;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

import java.util.List;

public class LootUtils {

	public static AnyOfCondition.Builder shouldSmeltLoot(RegistrateEntityLootTables pvd) {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = pvd.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);
		return AnyOfCondition.anyOf(
				LootItemEntityPropertyCondition.hasProperties(
						LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true))
				),
				LootItemEntityPropertyCondition.hasProperties(
						LootContext.EntityTarget.DIRECT_ATTACKER,
						EntityPredicate.Builder.entity()
								.equipment(
										EntityEquipmentPredicate.Builder.equipment()
												.mainhand(
														ItemPredicate.Builder.item()
																.withSubPredicate(
																		ItemSubPredicates.ENCHANTMENTS,
																		ItemEnchantmentsPredicate.enchantments(
																				List.of(new EnchantmentPredicate(registrylookup.getOrThrow(EnchantmentTags.SMELTS_LOOT), MinMaxBounds.Ints.ANY))
																		)
																)
												)
								)
				)
		);
	}


}
