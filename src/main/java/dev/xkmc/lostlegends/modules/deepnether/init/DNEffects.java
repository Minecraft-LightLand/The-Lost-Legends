package dev.xkmc.lostlegends.modules.deepnether.init;

import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.PotionBuilder;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.effect.EmptyEffect;
import dev.xkmc.lostlegends.modules.deepnether.effect.SoulDrainEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;

public class DNEffects extends LLRegBase {

	public final SimpleEntry<MobEffect> SOUL_DRAIN, SOUL_SHELTER, LAVA_AFFINITY;


	public DNEffects(L2Registrate reg, String path) {
		super(reg, path);
		LAVA_AFFINITY = new SimpleEntry<>(reg.effect("lava_affinity",
						() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xAD7A64),
						"Improve vision under lava. Increase motion speed under lava. Provide fire immunity.")
				.lang(MobEffect::getDescriptionId, "Lava Affinity").register());
		SOUL_SHELTER = new SimpleEntry<>(reg.effect("soul_shelter",
						() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x3f7fff),
						"Clears soul fog. Prevent environmental soul damage. Provide fire immunity.")
				.lang(MobEffect::getDescriptionId, "Soul Shelter").register());
		SOUL_DRAIN = new SimpleEntry<>(reg.effect("soul_drain",
						() -> new SoulDrainEffect(MobEffectCategory.HARMFUL, 0x003f7f),
						"Reduce player speed, tool use speed, and range")
				.lang(MobEffect::getDescriptionId, "Soul Drain").register());

		var builder = new PotionBuilder(LostLegends.REGISTRATE);
		var lava_affinity = builder.regPotion("lava_affinity", "lava_affinity", LAVA_AFFINITY, Potions.FIRE_RESISTANCE, DeepNether.ITEMS.HEARTH_CRYSTAL, 3600, 0);
		var lava_affinity_long = builder.regPotion("long_lava_affinity", "lava_affinity", LAVA_AFFINITY, lava_affinity, Items.REDSTONE, 9600, 0);
		builder.addMix(Potions.LONG_FIRE_RESISTANCE, DeepNether.ITEMS.HEARTH_CRYSTAL, lava_affinity_long);

		var soul_shelter = builder.regPotion("soul_shelter", "soul_shelter", SOUL_SHELTER, Potions.FIRE_RESISTANCE, DeepNether.BLOCKS.SOUL_BLOSSOM, 3600, 0);
		var soul_shelter_long = builder.regPotion("long_soul_shelter", "soul_shelter", SOUL_SHELTER, soul_shelter, Items.REDSTONE, 9600, 0);
		builder.addMix(Potions.LONG_FIRE_RESISTANCE, DeepNether.BLOCKS.SOUL_BLOSSOM, soul_shelter_long);

		var soul_drain = builder.regPotion("soul_drain", "soul_drain", SOUL_DRAIN, soul_shelter, Items.FERMENTED_SPIDER_EYE, 3600, 0);
		var soul_drain_long = builder.regPotion("long_soul_drain", "soul_drain", SOUL_DRAIN, soul_drain, Items.REDSTONE, 9600, 0);
		builder.addMix(soul_shelter_long, Items.FERMENTED_SPIDER_EYE, soul_drain_long);
		builder.regPotion("strong_soul_drain", "soul_drain", SOUL_DRAIN, soul_drain, Items.GLOWSTONE_DUST, 1800, 1);

		LostLegends.REGISTRATE.addRegisterCallback(Registries.ITEM, () -> builder.regTab(DeepNether.TAB.key()));
	}

}
