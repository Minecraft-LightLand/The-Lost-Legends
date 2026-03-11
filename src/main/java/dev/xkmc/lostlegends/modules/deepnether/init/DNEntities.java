package dev.xkmc.lostlegends.modules.deepnether.init;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.lostlegends.foundation.entity.slime.BaseSlimeRenderer;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder.BeholderEntity;
import dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder.BeholderRenderer;
import dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder.SkullBeholderEntity;
import dev.xkmc.lostlegends.modules.deepnether.entity.flying.reaper.ReaperEntity;
import dev.xkmc.lostlegends.modules.deepnether.entity.flying.reaper.ReaperRenderer;
import dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer.WandererEntity;
import dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer.WandererRenderer;
import dev.xkmc.lostlegends.modules.deepnether.entity.slime.animal.*;
import dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether.NetherSlime;
import dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether.PutridSlime;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

public class DNEntities extends LLRegBase {

	public final EntityEntry<NetherSlime> NETHER_SLIME;
	public final EntityEntry<PutridSlime> PUTRID_SLIME;
	public final EntityEntry<PigSlime> PIG_SLIME;
	public final EntityEntry<SheepSlime> SHEEP_SLIME;
	public final EntityEntry<BeeSlime> BEE_SLIME;
	public final EntityEntry<WandererEntity> WANDERER;
	public final EntityEntry<BeholderEntity> BEHOLDER;
	public final EntityEntry<SkullBeholderEntity> SKULL_BEHOLDER;
	public final EntityEntry<ReaperEntity> REAPER;

	public DNEntities(L2Registrate reg, String path) {
		super(reg, path);

		// TODO drop tweaks
		NETHER_SLIME = reg.entity("nether_slime", NetherSlime::new, MobCategory.MONSTER)
				.renderer(() -> BaseSlimeRenderer::new)
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

		// TODO drop tweaks
		PUTRID_SLIME = reg.entity("putrid_slime", PutridSlime::new, MobCategory.MONSTER)
				.renderer(() -> BaseSlimeRenderer::new)
				.attributes(PutridSlime::createAttributes)
				.properties(p -> p.sized(0.52F, 0.52F).eyeHeight(0.325F)
						.spawnDimensionsScale(4.0F).clientTrackingRange(10).fireImmune())
				.spawnPlacement(SpawnPlacementTypes.ON_GROUND,
						Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
						PutridSlime::checkMagmaCubeSpawnRules,
						RegisterSpawnPlacementsEvent.Operation.AND)
				.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE)
				.loot((pvd, e) -> pvd.add(e, LootTable.lootTable()))
				.spawnEgg(0x231F1F, 0x53718F).build()
				.register();

		PIG_SLIME = reg.entity("pig_slime", PigSlime::new, MobCategory.MONSTER)
				.renderer(() -> PigSlimeModel::createRenderer)
				.attributes(PigSlime::createAttributes)
				.properties(p -> p.sized(0.39F, 0.39F).eyeHeight(0.325F)
						.spawnDimensionsScale(4.0F).clientTrackingRange(10).fireImmune())
				.loot((pvd, e) -> pvd.add(e, PigSlime.buildLoot(pvd)))
				.spawnEgg(0xFFBABD, 0xFC9191).build()
				.register();

		SHEEP_SLIME = reg.entity("sheep_slime", SheepSlime::new, MobCategory.MONSTER)
				.renderer(() -> SheepSlimeModel::createRenderer)
				.attributes(SheepSlime::createAttributes)
				.properties(p -> p.sized(0.39F, 0.39F).eyeHeight(0.325F)
						.spawnDimensionsScale(4.0F).clientTrackingRange(10).fireImmune())
				.loot((pvd, e) -> pvd.add(e, SheepSlime.buildLoot(pvd)))
				.spawnEgg(0xF4F4F4, 0xBD997E).build()
				.register();

		BEE_SLIME = reg.entity("bee_slime", BeeSlime::new, MobCategory.MONSTER)
				.renderer(() -> BeeSlimeModel::createRenderer)
				.attributes(BeeSlime::createAttributes)
				.properties(p -> p.sized(0.39F, 0.39F).eyeHeight(0.325F)
						.spawnDimensionsScale(4.0F).clientTrackingRange(10).fireImmune())
				.loot((pvd, e) -> pvd.add(e, BeeSlime.buildLoot(pvd)))
				.spawnEgg(0xEBAE1A, 0x472012).build()
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

		//TODO spawn, drop
		BEHOLDER = reg.entity("beholder", BeholderEntity::new, MobCategory.MONSTER)
				.renderer(() -> BeholderRenderer::new)
				.attributes(BeholderEntity::createAttributes)
				.properties(p -> p.sized(0.6F, 0.9F).eyeHeight(0.5f)
						.ridingOffset(-0.7f).clientTrackingRange(10).fireImmune())
				.spawnPlacement(SpawnPlacementTypes.ON_GROUND,
						Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
						BeholderEntity::checkSpawnRules,
						RegisterSpawnPlacementsEvent.Operation.AND)
				.loot((pvd, e) -> pvd.add(e, LootTable.lootTable()))
				.spawnEgg(0x413737, 0xCDADAD).build()
				.register();

		//TODO spawn, drop
		SKULL_BEHOLDER = reg.entity("skull_beholder", SkullBeholderEntity::new, MobCategory.MONSTER)
				.renderer(() -> BeholderRenderer::new)
				.attributes(BeholderEntity::createAttributes)
				.properties(p -> p.sized(0.6F, 0.9F).eyeHeight(0.5f)
						.ridingOffset(-0.7f).clientTrackingRange(10).fireImmune())
				.spawnPlacement(SpawnPlacementTypes.ON_GROUND,
						Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
						BeholderEntity::checkSpawnRules,
						RegisterSpawnPlacementsEvent.Operation.AND)
				.tag(EntityTypeTags.UNDEAD)
				.loot((pvd, e) -> pvd.add(e, LootTable.lootTable()))
				.spawnEgg(0x3B1F1F, 0x232222).build()
				.register();

		//TODO spawn, drop
		REAPER = reg.entity("reaper", ReaperEntity::new, MobCategory.MONSTER)
				.renderer(() -> ReaperRenderer::new)
				.attributes(ReaperEntity::createAttributes)
				.properties(p -> p.sized(0.9F, 1.6F).eyeHeight(1f)
						.ridingOffset(-0.7f).clientTrackingRange(10).fireImmune())
				.spawnPlacement(SpawnPlacementTypes.ON_GROUND,
						Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
						BeholderEntity::checkSpawnRules,
						RegisterSpawnPlacementsEvent.Operation.AND)
				.tag(EntityTypeTags.UNDEAD)
				.loot((pvd, e) -> pvd.add(e, LootTable.lootTable()))
				.spawnEgg(0x3B1F1F, 0x232222).build()
				.register();
	}

}
