package dev.xkmc.lostlegends.modules.deepnether.init;

import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.PotionBuilder;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.foundation.module.PotionSet;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.effect.EmptyEffect;
import dev.xkmc.lostlegends.modules.deepnether.effect.SoulDrainEffect;
import dev.xkmc.lostlegends.modules.item.LLEquipments;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;

public class DNEffects extends LLRegBase {

	public final SimpleEntry<MobEffect> SOUL_DRAIN, SOUL_SHELTER, LAVA_AFFINITY, ASH_BOUND, LAVA_WALKER;


	public DNEffects(L2Registrate reg, String path) {
		super(reg, path);
		LAVA_AFFINITY = new SimpleEntry<>(reg.effect("lava_affinity",
						() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xAD7A64),
						"Improve vision under lava. Increase motion speed under lava. Provide fire immunity.")
				.lang(MobEffect::getDescriptionId, "Lava Affinity").register());
		LAVA_WALKER = new SimpleEntry<>(reg.effect("lava_walker",
						() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0xff7f3f),
						"Allows walking on lava. Provide fire immunity.")
				.lang(MobEffect::getDescriptionId, "Lava Walker").register());
		SOUL_SHELTER = new SimpleEntry<>(reg.effect("soul_shelter",
						() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x3f7fff),
						"Reduce soul fog. Allows walking on liquid soul. Prevent liquid soul and soul flower damage. Provide fire immunity.")
				.lang(MobEffect::getDescriptionId, "Soul Shelter").register());
		ASH_BOUND = new SimpleEntry<>(reg.effect("ash_bound",
						() -> new EmptyEffect(MobEffectCategory.BENEFICIAL, 0x7f7f7f),
						"Reduce ash fog. Player would not suffocate in ash, and would be able to walk on and climb in ash")
				.lang(MobEffect::getDescriptionId, "Ash Bound").register());
		SOUL_DRAIN = new SimpleEntry<>(reg.effect("soul_drain",
						() -> new SoulDrainEffect(MobEffectCategory.HARMFUL, 0x003f7f),
						"Reduce player speed, tool use speed, and range")
				.lang(MobEffect::getDescriptionId, "Soul Drain").register());

		var builder = new PotionBuilder(LostLegends.REGISTRATE);
		var lavaAff = PotionSet.potion2(builder, "lava_affinity", LAVA_AFFINITY, Potions.FIRE_RESISTANCE, DeepNether.ITEMS.HEARTH_CRYSTAL, 3600, 9600, 0);
		var lavaWalker = PotionSet.potion2(builder, "lava_walker", LAVA_WALKER, lavaAff, Items.FERMENTED_SPIDER_EYE, 3600, 9600, 0);
		var ashBound = PotionSet.potion2(builder, "ash_bound", ASH_BOUND, Potions.AWKWARD, DeepNether.VEGE.ASH_BLOSSOM, 3600, 9600, 0);
		var soulShelter = PotionSet.potion2(builder, "soul_shelter", SOUL_SHELTER, Potions.FIRE_RESISTANCE, DeepNether.VEGE.SOUL_BLOSSOM, 3600, 9600, 0);
		var soulDrain = PotionSet.potion3(builder, "soul_drain", SOUL_DRAIN, soulShelter, Items.FERMENTED_SPIDER_EYE, 3600, 9600, 1800, 0, 1);

		LostLegends.REGISTRATE.addRegisterCallback(Registries.ITEM, () -> builder.regTab(LLEquipments.TAB.key()));
	}

}
