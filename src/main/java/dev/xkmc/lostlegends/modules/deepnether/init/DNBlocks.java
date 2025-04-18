package dev.xkmc.lostlegends.modules.deepnether.init;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.modules.deepnether.block.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class DNBlocks extends LLRegBase {

	public final BlockEntry<Block> NETHER_SOIL, ASH_STONE, DEMENTING_SOIL, RAGING_OBSIDIAN, DEEP_NETHERRACK, HEARTH_ORE, BURIED_GOLD_DEBRIS;
	public final BlockEntry<WeepingSandBlock> WEEPING_SAND;
	public final BlockEntry<AshBlock> ASH_BLOCK;
	public final BlockEntry<DarkStoneBlock> DARK_STONE;
	public final BlockEntry<DNNyliumBlock> NETHER_NYLIUM;
	public final BlockEntry<AshBlossomBlock> ASH_BLOSSOM;


	DNBlocks(L2Registrate reg, String path) {
		super(reg, path);

		// stone
		{
			DEEP_NETHERRACK = block("deep_netherrack", Block::new)
					.prop(MapColor.NETHER, SoundType.STONE).strength(1f)
					.cubeAll().pickaxe()
					.tag(BlockTags.INFINIBURN_OVERWORLD, BlockTags.BASE_STONE_NETHER)
					.simpleItem()
					.register();

			HEARTH_ORE = block("hearth_ore", Block::new)
					.prop(MapColor.NETHER, SoundType.STONE).strength(1f)
					.cubeAll().pickaxe()
					.tag(BlockTags.INFINIBURN_OVERWORLD)
					.simpleItem()
					.multiOreLoot(() -> DeepNether.ITEMS.HEARTH_CRYSTAL.get(), 1, 2)
					.register();

			BURIED_GOLD_DEBRIS = block("buried_gold_debris", Block::new)
					.prop(MapColor.NETHER, SoundType.STONE).strength(1f)
					.cubeAll().pickaxe()
					.tag(BlockTags.INFINIBURN_OVERWORLD)
					.simpleItem()
					.multiOreLoot(() -> DeepNether.ITEMS.GOLD_DEBRIS.get(), 1, 2)
					.register();

			NETHER_SOIL = block("nether_soil", Block::new)
					.prop(MapColor.NETHER, SoundType.GRAVEL).strength(0.5F)
					.cubeAll().shovel()
					.tag(BlockTags.INFINIBURN_OVERWORLD, BlockTags.NETHER_CARVER_REPLACEABLES)
					.simpleItem()
					.register();

			NETHER_NYLIUM = block("nether_nylium", DNNyliumBlock::new)
					.prop(MapColor.NETHER, SoundType.GRASS).strength(0.5f)
					.prop(BlockBehaviour.Properties::randomTicks)
					.blockstate((ctx, pvd) ->
							pvd.simpleBlock(ctx.get(), pvd.models().cubeBottomTop(ctx.getName(),
									blockLoc(ctx.getName() + "_side"),
									blockLoc("nether_soil"),
									blockLoc(ctx.getName() + "_top"))))
					.shovel()
					.tag(BlockTags.NYLIUM, BlockTags.NETHER_CARVER_REPLACEABLES)
					.silkTouchOr(NETHER_SOIL)
					.simpleItem()
					.register();

			ASH_BLOCK = block("ash_block", AshBlock::new)
					.prop(MapColor.COLOR_BLACK, SoundType.SAND).strength(0.5f, 5f)
					.blockstate((ctx, pvd) ->
							pvd.simpleBlock(ctx.get(), pvd.models().withExistingParent(ctx.getName(),
											pvd.mcLoc("block/powder_snow"))
									.texture("texture", blockLoc(ctx.getName()))))
					.shovel()
					.tag(BlockTags.NETHER_CARVER_REPLACEABLES, BlockTags.SCULK_REPLACEABLE)
					.simpleItem()
					.register();

			ASH_STONE = block("ash_stone", Block::new)
					.prop(MapColor.COLOR_BLACK, SoundType.STONE).strength(0.8F, 12f)
					.cubeAll().pickaxe()
					.tag(BlockTags.BASE_STONE_NETHER)
					.simpleItem()
					.register();

			WEEPING_SAND = block("weeping_sand", WeepingSandBlock::new)
					.prop(MapColor.COLOR_BROWN, SoundType.SOUL_SAND).strength(0.5f)
					.prop(p -> p.speedFactor(0.4F))
					.fullBlock().cubeAll().shovel()
					.tag(BlockTags.SOUL_SPEED_BLOCKS, BlockTags.SOUL_FIRE_BASE_BLOCKS, BlockTags.NETHER_CARVER_REPLACEABLES, BlockTags.SCULK_REPLACEABLE)
					.simpleItem()
					.register();

			DEMENTING_SOIL = block("dementing_soil", Block::new)
					.prop(MapColor.COLOR_BROWN, SoundType.SOUL_SOIL).strength(0.5f)
					.cubeAll().shovel()
					.tag(BlockTags.SOUL_SPEED_BLOCKS, BlockTags.SOUL_FIRE_BASE_BLOCKS, BlockTags.NETHER_CARVER_REPLACEABLES, BlockTags.SCULK_REPLACEABLE)
					.simpleItem()
					.register();

			DARK_STONE = block("dark_stone", DarkStoneBlock::new)
					.prop(MapColor.COLOR_BLACK, SoundType.STONE).strength(1f, 5f)
					.prop(p -> p.lightLevel(state -> 7))
					.cubeAll().pickaxe()
					.simpleItem()
					.shardLoot(() -> DeepNether.ITEMS.DARK_COBBLE.get(), 2, 4)
					.register();

			RAGING_OBSIDIAN = block("raging_obsidian", Block::new)
					.prop(MapColor.COLOR_BLACK, SoundType.STONE).strength(50, 1200)
					.cubeAll().obsidian()
					.simpleItem()
					.register();

		}

		{
			ASH_BLOSSOM = block("ash_blossom", p -> new AshBlossomBlock(MobEffects.WEAKNESS, 6.0F, p))
					.prop(p -> p.lightLevel(stata -> 8))
					.foliage()
					.cross()
					.simpleItem()
					.register();

		}

	}
}
