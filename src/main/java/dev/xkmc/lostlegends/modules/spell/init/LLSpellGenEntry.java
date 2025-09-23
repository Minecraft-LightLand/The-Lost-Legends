package dev.xkmc.lostlegends.modules.spell.init;

import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public abstract class LLSpellGenEntry extends SpellDataGenEntry {

	protected static ResourceKey<SpellAction> spell(String id) {
		return ResourceKey.create(EngineRegistry.SPELL, LostLegends.loc(id));
	}

	protected static DataGenCachedHolder<ProjectileConfig> projectile(String id) {
		return new DataGenCachedHolder<>(ResourceKey.create(EngineRegistry.PROJECTILE, LostLegends.loc(id)));
	}

	protected static ResourceLocation tex(String path) {
		return LostLegends.loc("block/spell/" + path);
	}

	public List<ResourceLocation> additionalModels() {
		return List.of();
	}

	public void genModel(RegistrateItemModelProvider pvd) {
	}

}
