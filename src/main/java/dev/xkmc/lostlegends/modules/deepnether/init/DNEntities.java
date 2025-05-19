package dev.xkmc.lostlegends.modules.deepnether.init;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer.WandererEntity;
import dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer.WandererRenderer;
import dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether.NetherSlime;
import dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether.NetherSlimeRenderer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class DNEntities extends LLRegBase {

	public final EntityEntry<NetherSlime> NETHER_SLIME;
	public final EntityEntry<WandererEntity> WANDERER;

	public DNEntities(L2Registrate reg, String path) {
		super(reg, path);

		// TODO drop tweaks
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
				.spawnEgg(0x322020, 0xBC8536).build()
				.register();

		//TODO spawn, drop
		WANDERER = reg.entity("wanderer", WandererEntity::new, MobCategory.MONSTER)
				.renderer(() -> WandererRenderer::new)
				.attributes(WandererEntity::createAttributes)
				.properties(p -> p.sized(0.6F, 1.8F).eyeHeight(1.6f)
						.ridingOffset(-0.7f).clientTrackingRange(10).fireImmune())
				.spawnPlacement(SpawnPlacementTypes.ON_GROUND,
						Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
						Monster::checkMonsterSpawnRules,
						RegisterSpawnPlacementsEvent.Operation.AND)
				.tag(EntityTypeTags.UNDEAD)
				.loot((pvd, e) -> pvd.add(e, LootTable.lootTable()))
				.spawnEgg(0x47463D, 0x0AD3D6).build()
				.register();
	}

}
