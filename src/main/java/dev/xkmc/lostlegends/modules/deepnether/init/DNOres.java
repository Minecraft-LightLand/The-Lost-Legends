package dev.xkmc.lostlegends.modules.deepnether.init;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.modules.deepnether.block.misc.SoulBlobBlock;
import dev.xkmc.lostlegends.modules.deepnether.block.surface.WeepingSandBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class DNOres extends LLRegBase {

	public final BlockEntry<Block> HEARTH_ORE, BURIED_GOLD_DEBRIS, AMARAST_ORE, RESONANT_TWISTONE, CRYSTALIZED_BONE;
	public final BlockEntry<Block> DEMENTING_LAZURITE, DEMENTING_RUST, INSCRIBED_BLEAKSTONE;
	public final BlockEntry<WeepingSandBlock> WEEPING_LAZURITE, WEEPING_RUST;

	public final BlockEntry<HalfTransparentBlock> AMBER_MAGMA, ECTOPLASM;
	public final BlockEntry<SoulBlobBlock> SOUL_BLOB;

	public DNOres(L2Registrate reg, String path) {
		super(reg, path);

		{

			HEARTH_ORE = block("hearth_ore", Block::new)
					.prop(MapColor.NETHER, SoundType.STONE).strength(2f)
					.cubeAll().pickaxe()
					.tag(BlockTags.INFINIBURN_OVERWORLD)
					.simpleItem()
					.multiOreLoot(() -> DeepNether.ITEMS.HEARTH_CRYSTAL.get(), 1, 2)
					.register();

			BURIED_GOLD_DEBRIS = block("buried_gold_debris", Block::new)
					.prop(MapColor.NETHER, SoundType.STONE).strength(2f)
					.cubeAll().pickaxe()
					.tag(BlockTags.INFINIBURN_OVERWORLD)
					.simpleItem()
					.multiOreLoot(() -> DeepNether.ITEMS.GOLD_DEBRIS.get(), 1, 2)
					.register();

			AMARAST_ORE = block("amarast_ore", Block::new)
					.prop(MapColor.COLOR_BLACK, SoundType.STONE).strength(3f)
					.cubeAll().pickaxe()
					.simpleItem()
					.multiOreLoot(() -> DeepNether.ITEMS.AMARAST.get(), 1, 2)
					.register();

			CRYSTALIZED_BONE = block("crystalized_bone", Block::new)
					.prop(MapColor.TERRACOTTA_WHITE, SoundType.STONE).strength(3f)
					.cubeAll().pickaxe()
					.simpleItem()
					.multiOreLoot(() -> DeepNether.ITEMS.BONE_CRYSTAL.get(), 1, 2)
					.register();

			RESONANT_TWISTONE = block("resonant_twistone", Block::new)
					.prop(MapColor.COLOR_BLACK, SoundType.STONE).strength(1f)
					.cubeAll().pickaxe()
					.tag(BlockTags.SOUL_FIRE_BASE_BLOCKS)
					.simpleItem()
					.oreLoot(() -> DeepNether.ITEMS.RESONANT_SOULGEM.get())
					.register();

			WEEPING_LAZURITE = block("weeping_lazurite", WeepingSandBlock::new)
					.prop(MapColor.COLOR_BROWN, SoundType.SOUL_SAND).strength(0.5f)
					.prop(p -> p.speedFactor(0.4F))
					.fullBlock().highlightCube().shovel()
					.tag(BlockTags.SOUL_SPEED_BLOCKS, BlockTags.SOUL_FIRE_BASE_BLOCKS, BlockTags.NETHER_CARVER_REPLACEABLES)
					.simpleItem()
					.multiOreLoot(() -> DeepNether.ITEMS.SOUL_LAZURITE.get(), 1, 2)
					.register();

			DEMENTING_LAZURITE = block("dementing_lazurite", Block::new)
					.prop(MapColor.COLOR_BROWN, SoundType.SOUL_SOIL).strength(0.5f)
					.highlightCube().shovel()
					.tag(BlockTags.SOUL_SPEED_BLOCKS, BlockTags.SOUL_FIRE_BASE_BLOCKS, BlockTags.NETHER_CARVER_REPLACEABLES)
					.simpleItem()
					.multiOreLoot(() -> DeepNether.ITEMS.SOUL_LAZURITE.get(), 1, 2)
					.register();

			WEEPING_RUST = block("weeping_rust", WeepingSandBlock::new)
					.prop(MapColor.COLOR_BROWN, SoundType.SOUL_SAND).strength(0.5f)
					.prop(p -> p.speedFactor(0.4F))
					.fullBlock().cubeAll().shovel()
					.tag(BlockTags.SOUL_SPEED_BLOCKS, BlockTags.SOUL_FIRE_BASE_BLOCKS, BlockTags.NETHER_CARVER_REPLACEABLES)
					.simpleItem()
					.multiOreLoot(() -> DeepNether.ITEMS.SOUL_RUST.get(), 1, 2)
					.register();

			DEMENTING_RUST = block("dementing_rust", Block::new)
					.prop(MapColor.COLOR_BROWN, SoundType.SOUL_SOIL).strength(0.5f)
					.cubeAll().shovel()
					.tag(BlockTags.SOUL_SPEED_BLOCKS, BlockTags.SOUL_FIRE_BASE_BLOCKS, BlockTags.NETHER_CARVER_REPLACEABLES)
					.simpleItem()
					.multiOreLoot(() -> DeepNether.ITEMS.SOUL_RUST.get(), 1, 2)
					.register();

			INSCRIBED_BLEAKSTONE = block("inscribed_bleakstone", Block::new)
					.prop(MapColor.COLOR_GRAY, SoundType.STONE).strength(2f)
					.blockstate((ctx, pvd) ->
							pvd.simpleBlock(ctx.get(), pvd.models().getBuilder(ctx.getName())
									.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/cube_top_highlight")))
									.texture("base", pvd.modLoc("block/" + DeepNether.BLOCKS.path + "/bleakstone"))
									.texture("top", pvd.modLoc("block/" + path + "/" + ctx.getName()))
									.texture("highlight", pvd.modLoc("block/" + path + "/" + ctx.getName() + "_highlight"))
									.texture("particle", pvd.modLoc("block/" + path + "/" + ctx.getName()))
									.renderType("cutout")
							))
					.pickaxe()
					.simpleItem()
					.register();

		}


		// deco
		{

			AMBER_MAGMA = block("amber_magma", HalfTransparentBlock::new)
					.prop(MapColor.NETHER, SoundType.STONE).strength(1f).light(15)
					.prop(BlockBehaviour.Properties::noOcclusion)
					.blockstate((ctx, pvd) ->
							pvd.simpleBlock(ctx.get(), pvd.models().getBuilder(ctx.getName())
									.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/slime_like")))
									.texture("overlay", blockLoc(ctx.getName() + "_overlay"))
									.texture("base", blockLoc(ctx.getName()))
									.renderType("translucent")))
					.pickaxe()
					.simpleItem().register();

			ECTOPLASM = block("ectoplasm", HalfTransparentBlock::new)
					.prop(MapColor.COLOR_CYAN, SoundType.STONE).strength(1f).light(15)
					.prop(BlockBehaviour.Properties::noOcclusion)
					.blockstate((ctx, pvd) ->
							pvd.simpleBlock(ctx.get(), pvd.models().getBuilder(ctx.getName())
									.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/slime_like")))
									.texture("overlay", blockLoc(ctx.getName() + "_overlay"))
									.texture("base", blockLoc(ctx.getName()))
									.renderType("translucent")))
					.pickaxe()
					.simpleItem().register();

			SOUL_BLOB = block("soul_blob", SoulBlobBlock::new)
					.prop(MapColor.COLOR_CYAN, SoundType.WET_GRASS).strength(0).light(13)
					.fragile()
					.blockstate(SoulBlobBlock::buildBlockState)
					.simpleItem().register();

		}

	}

}
