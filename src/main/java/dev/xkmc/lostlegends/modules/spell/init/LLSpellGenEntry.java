package dev.xkmc.lostlegends.modules.spell.init;

import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.resources.ResourceKey;

public abstract class LLSpellGenEntry extends SpellDataGenEntry {

	protected static ResourceKey<SpellAction> spell(String id) {
		return ResourceKey.create(EngineRegistry.SPELL, LostLegends.loc(id));
	}

	protected static DataGenCachedHolder<ProjectileConfig> projectile(String id) {
		return new DataGenCachedHolder<>(ResourceKey.create(EngineRegistry.PROJECTILE, LostLegends.loc(id)));
	}

}
