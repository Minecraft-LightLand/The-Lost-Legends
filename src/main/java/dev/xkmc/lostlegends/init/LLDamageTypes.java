package dev.xkmc.lostlegends.init;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2damagetracker.init.data.DamageTypeAndTagsGen;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.Tags;

public class LLDamageTypes extends DamageTypeAndTagsGen {

	public static final TagKey<DamageType> NO_SLIME_SPLIT = tag("no_slime_split");

	public LLDamageTypes(L2Registrate reg) {
		super(reg);
	}

	@Override
	protected void addDamageTypeTags(RegistrateTagsProvider.Impl<DamageType> pvd) {
		super.addDamageTypeTags(pvd);
		pvd.addTag(NO_SLIME_SPLIT).addTags(
				DamageTypeTags.BYPASSES_EFFECTS,
				DamageTypeTags.BYPASSES_INVULNERABILITY,
				DamageTypeTags.BYPASSES_RESISTANCE,
				DamageTypeTags.IS_DROWNING,
				DamageTypeTags.IS_EXPLOSION,
				DamageTypeTags.IS_FREEZING,
				DamageTypeTags.IS_LIGHTNING,
				Tags.DamageTypes.IS_MAGIC,
				Tags.DamageTypes.IS_TECHNICAL
		);
	}

	private static TagKey<DamageType> tag(String id) {
		return TagKey.create(Registries.DAMAGE_TYPE, LostLegends.loc(id));
	}

}
