package dev.xkmc.lostlegends.modules.deepnether.init;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.lostlegends.foundation.block.SimpleLavaloggedBlock;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.modules.deepnether.block.fluid.SimpleSoulLoggedBlock;
import dev.xkmc.lostlegends.modules.deepnether.block.vegetation.*;
import dev.xkmc.lostlegends.modules.deepnether.data.DNFeatures;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.RootsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class DNVegatation extends LLRegBase {

	public final BlockEntry<AshBlossomBlock> ASH_BLOSSOM;
	public final BlockEntry<ScarletBlossomBlock> SCARLET_BLOSSOM;
	public final BlockEntry<RootsBlock> SCARLET_ROOTS;
	public final BlockEntry<SoulBlossomBlock> SOUL_BLOSSOM;
	public final BlockEntry<SoulBlossomBlock> PHANTOM_FLOWER;
	public final BlockEntry<BoneVineHead> SCORCHED_BONE_VINE;
	public final BlockEntry<BoneVineBody> SCORCHED_BONE_VINE_PLANT;
	public final BlockEntry<SoulVineHead> SCREAMING_SOUL_VINE;
	public final BlockEntry<SoulVineBody> SCREAMING_SOUL_VINE_PLANT;
	public final BlockEntry<SoulTentacleHead> SOUL_TENTACLE_DOWN;
	public final BlockEntry<SoulTentacleBody> SOUL_TENTACLE_DOWN_BODY;

	public DNVegatation(L2Registrate reg, String path) {
		super(reg, path);

		// simple
		{
			ASH_BLOSSOM = block("ash_blossom", p -> new AshBlossomBlock(
					DeepNether.EFFECTS.ASH_BOUND, 10, p, DNFeatures.INS.vege.ashBlossom.cf))
					.light(8).foliage().cross()
					.tag(BlockTags.FLOWERS)
					.simpleItem()
					.register();

			SCARLET_BLOSSOM = block("scarlet_blossom", p -> new ScarletBlossomBlock(
					DeepNether.EFFECTS.LAVA_AFFINITY, 30, p, DNFeatures.INS.vege.scarletBlossom.cf))
					.foliage().cross()
					.tag(BlockTags.FLOWERS)
					.simpleItem()
					.register();

			SOUL_BLOSSOM = block("soul_blossom", p -> new SoulBlossomBlock(
					DeepNether.EFFECTS.SOUL_SHELTER, 10, p, DNFeatures.INS.vege.soulBlossom.cf))
					.light(12).foliage().cross()
					.tag(BlockTags.FLOWERS)
					.simpleItem()
					.register();

			//TODO block class, feature gen
			PHANTOM_FLOWER = block("phantom_flower", p -> new SoulBlossomBlock(
					DeepNether.EFFECTS.SOUL_SHELTER, 10, p, DNFeatures.INS.vege.soulBlossom.cf))
					.light(12).foliage().cross()
					.tag(BlockTags.FLOWERS)
					.simpleItem()
					.register();

			SCARLET_ROOTS = block("scarlet_roots", RootsBlock::new)
					.foliage().cross()
					.tag(BlockTags.COMBINATION_STEP_SOUND_BLOCKS, BlockTags.SWORD_EFFICIENT, BlockTags.REPLACEABLE_BY_TREES)
					.simpleItem()
					.silkTouchOrShear()
					.register();
		}

		// vines
		{
			SCORCHED_BONE_VINE = block("scorched_bone_vines", BoneVineHead::new)
					.prop(MapColor.TERRACOTTA_WHITE, SoundType.BONE_BLOCK)
					.light(SimpleLavaloggedBlock.LAVALOGGED, 15, 10)
					.prop(BlockBehaviour.Properties::randomTicks)
					.fragile().cross()
					.tag(BlockTags.CLIMBABLE)
					.simpleItem()
					.itemModel(this::flatBlockItem)
					.register();

			SCORCHED_BONE_VINE_PLANT = block("scorched_bone_vines_plant", BoneVineBody::new)
					.prop(MapColor.TERRACOTTA_WHITE, SoundType.BONE_BLOCK)
					.light(SimpleLavaloggedBlock.LAVALOGGED, 15, 10)
					.fragile().cross()
					.tag(BlockTags.CLIMBABLE)
					.lootChance(0.1f)
					.register();

			SCREAMING_SOUL_VINE = block("screaming_soul_vines", SoulVineHead::new)
					.prop(MapColor.COLOR_CYAN, SoundType.WEEPING_VINES)
					.light(SimpleSoulLoggedBlock.LOGGED, 15, 7)
					.prop(BlockBehaviour.Properties::randomTicks)
					.fragile().cross()
					.tag(BlockTags.CLIMBABLE)
					.simpleItem()
					.itemModel(this::flatBlockItem)
					.register();

			SCREAMING_SOUL_VINE_PLANT = block("screaming_soul_vines_plant", SoulVineBody::new)
					.prop(MapColor.COLOR_CYAN, SoundType.WEEPING_VINES)
					.light(SimpleSoulLoggedBlock.LOGGED, 15, 7)
					.fragile().cross()
					.tag(BlockTags.CLIMBABLE)
					.lootChance(0.1f)
					.register();

			SOUL_TENTACLE_DOWN = block("soul_tentacles",
					p -> new SoulTentacleHead(p, Direction.DOWN))
					.prop(MapColor.COLOR_CYAN, SoundType.WEEPING_VINES)
					.light(SimpleSoulLoggedBlock.LOGGED, 15, 7)
					.prop(BlockBehaviour.Properties::randomTicks)
					.fragile().cross()
					.tag(BlockTags.CLIMBABLE)
					.simpleItem()
					.itemModel(this::flatBlockItem)
					.register();

			SOUL_TENTACLE_DOWN_BODY = block("soul_tentacles_body",
					p -> new SoulTentacleBody(p, Direction.DOWN))
					.prop(MapColor.COLOR_CYAN, SoundType.WEEPING_VINES)
					.light(SimpleSoulLoggedBlock.LOGGED, 15, 7)
					.fragile().blockstate(SoulTentacleBody::buildBlockStates)
					.tag(BlockTags.CLIMBABLE)
					.lootChance(0.1f)
					.register();

		}

	}

}
