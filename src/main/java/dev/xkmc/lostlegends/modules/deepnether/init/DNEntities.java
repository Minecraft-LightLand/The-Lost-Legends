package dev.xkmc.lostlegends.modules.deepnether.init;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether.NetherSlime;
import dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether.NetherSlimeRenderer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class DNEntities extends LLRegBase {

	public final EntityEntry<NetherSlime> NETHER_SLIME;

	public DNEntities(L2Registrate reg, String path) {
		super(reg, path);

		NETHER_SLIME = reg.entity("nether_slime", NetherSlime::new, MobCategory.MONSTER)
				.renderer(() -> NetherSlimeRenderer::new)
				.attributes(NetherSlime::createAttributes)
				.properties(p -> p.sized(0.52F, 0.52F).eyeHeight(0.325F)
						.spawnDimensionsScale(4.0F).clientTrackingRange(10).fireImmune())
				.spawnPlacement(SpawnPlacementTypes.ON_GROUND,
						Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
						NetherSlime::checkMagmaCubeSpawnRules,
						RegisterSpawnPlacementsEvent.Operation.AND)
				.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE)
				.loot((pvd, e) -> pvd.add(e, LootTable.lootTable()))
				.register();
	}

}
