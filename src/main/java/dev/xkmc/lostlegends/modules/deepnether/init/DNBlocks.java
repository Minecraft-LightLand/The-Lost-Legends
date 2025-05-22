package dev.xkmc.lostlegends.modules.deepnether.init;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.FluidEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.modules.deepnether.block.fluid.LiquidSoulFluid;
import dev.xkmc.lostlegends.modules.deepnether.block.fluid.LiquidSoulFluidType;
import dev.xkmc.lostlegends.modules.deepnether.block.fluid.MoltenGoldFluid;
import dev.xkmc.lostlegends.modules.deepnether.block.fluid.MoltenGoldFluidType;
import dev.xkmc.lostlegends.modules.deepnether.block.misc.SoulBlobBlock;
import dev.xkmc.lostlegends.modules.deepnether.block.portal.LavaPortalBlock;
import dev.xkmc.lostlegends.modules.deepnether.block.surface.*;
import dev.xkmc.lostlegends.modules.deepnether.data.DNFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.Tags;

import java.util.Set;

public class DNBlocks extends LLRegBase {

	public final BlockEntry<Block> DEEP_NETHERRACK, DEEP_BLACKSTONE, TWISTONE, DENSE_BONE, ASH_STONE,
			NETHER_SOIL, DEMENTING_SOIL, SOUL_SHELL, SCORCHED_NETHERRACK;

	public final BlockEntry<SoilNyliumBlock> CRIMSON_MYCELIUM, GOLDEN_MYCELIUM;
	public final BlockEntry<AshBlock> ASH_BLOCK;
	public final BlockEntry<WeepingSandBlock> WEEPING_SAND;
	public final BlockEntry<BonePileBlock> BONE_PILE;
	public final BlockEntry<DeepMagmaBlock> MAGMA;

	public final BlockEntry<HalfTransparentBlock> AMBER_MAGMA, ECTOPLASM;
	public final BlockEntry<SoulBlobBlock> SOUL_BLOB;

	public final BlockEntry<Block> RAGING_OBSIDIAN;
	public final BlockEntry<DarkStoneBlock> DARK_STONE;


	public final BlockEntry<LavaPortalBlock> PORTAL;
	public final SimpleEntry<PoiType> PORTAL_POI;
	public final FluidEntry<LiquidSoulFluid.Flowing> LIQUID_SOUL;
	public final FluidEntry<MoltenGoldFluid.Flowing> MOLTEN_GOLD;

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

			SCORCHED_NETHERRACK = block("scorched_deep_netherrack", Block::new)
					.prop(MapColor.NETHER, SoundType.STONE).strength(0.5f)
					.blockstate((ctx, pvd) ->
							pvd.simpleBlock(ctx.get(), pvd.models().cubeBottomTop(ctx.getName(),
									blockLoc(ctx.getName() + "_side"),
									blockLoc("deep_netherrack"),
									blockLoc(ctx.getName() + "_top"))))
					.pickaxe()
					.tag(BlockTags.INFINIBURN_OVERWORLD, BlockTags.NETHER_CARVER_REPLACEABLES)
					.silkTouchOrElse(DEEP_NETHERRACK)
					.simpleItem()
					.register();

			DEEP_BLACKSTONE = block("deep_blackstone", Block::new)
					.prop(MapColor.COLOR_BLACK, SoundType.STONE).strength(2f)
					.cubeAll().pickaxe()
					.tag(BlockTags.BASE_STONE_NETHER)
					.simpleItem().itemTag(ItemTags.STONE_CRAFTING_MATERIALS, ItemTags.STONE_TOOL_MATERIALS)
					.register();

			MAGMA = block("deep_magma", DeepMagmaBlock::new)
					.prop(MapColor.NETHER, SoundType.STONE).strength(1f).light(15)
					.cubeAll().pickaxe()
					.simpleItem().register();

			NETHER_SOIL = block("nether_soil", Block::new)
					.prop(MapColor.NETHER, SoundType.GRAVEL).strength(0.5F)
					.cubeAll().shovel()
					.tag(BlockTags.INFINIBURN_OVERWORLD, BlockTags.NETHER_CARVER_REPLACEABLES)
					.simpleItem()
					.register();

			CRIMSON_MYCELIUM = block("crimson_mycelium", p -> new SoilNyliumBlock(p,
					DNFeatures.INS.vege.crimsonBonemeal.cf))
					.prop(MapColor.NETHER, SoundType.GRASS).strength(0.5f)
					.prop(BlockBehaviour.Properties::randomTicks)
					.blockstate((ctx, pvd) ->
							pvd.simpleBlock(ctx.get(), pvd.models().cubeBottomTop(ctx.getName(),
									blockLoc(ctx.getName() + "_side"),
									blockLoc("nether_soil"),
									blockLoc(ctx.getName() + "_top"))))
					.shovel()
					.tag(BlockTags.NYLIUM, BlockTags.NETHER_CARVER_REPLACEABLES)
					.silkTouchOrElse(NETHER_SOIL)
					.simpleItem()
					.register();

			GOLDEN_MYCELIUM = block("golden_mycelium", p -> new SoilNyliumBlock(p,
					DNFeatures.INS.vege.goldenBonemeal.cf))
					.prop(MapColor.NETHER, SoundType.GRASS).strength(0.5f)
					.prop(BlockBehaviour.Properties::randomTicks)
					.blockstate((ctx, pvd) ->
							pvd.simpleBlock(ctx.get(), pvd.models().cubeBottomTop(ctx.getName(),
									blockLoc(ctx.getName() + "_side"),
									blockLoc("nether_soil"),
									blockLoc(ctx.getName() + "_top"))))
					.shovel()
					.tag(BlockTags.NYLIUM, BlockTags.NETHER_CARVER_REPLACEABLES)
					.silkTouchOrElse(NETHER_SOIL)
					.simpleItem()
					.register();

			DENSE_BONE = block("dense_bone", Block::new)
					.prop(MapColor.TERRACOTTA_WHITE, SoundType.BONE_BLOCK).strength(3f, 8f)
					.cubeColumn().pickaxe()
					.tag(BlockTags.BASE_STONE_NETHER)
					.simpleItem()
					.register();

			BONE_PILE = block("bone_pile", BonePileBlock::new)
					.prop(MapColor.TERRACOTTA_WHITE, SoundType.BONE_BLOCK).strength(1f)
					.fakeSolid().cubeColumn().pickaxe()
					.tag(BlockTags.CLIMBABLE)
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
					.prop(MapColor.COLOR_BLACK, SoundType.STONE).strength(1F, 12f)
					.cubeAll().pickaxe()
					.tag(BlockTags.BASE_STONE_NETHER)
					.simpleItem()
					.itemTag(ItemTags.STONE_CRAFTING_MATERIALS, ItemTags.STONE_TOOL_MATERIALS)
					.register();

			WEEPING_SAND = block("weeping_sand", WeepingSandBlock::new)
					.prop(MapColor.COLOR_BROWN, SoundType.SOUL_SAND).strength(0.5f)
					.prop(p -> p.speedFactor(0.4F))
					.fullBlock().cubeAll().shovel()
					.tag(BlockTags.SOUL_SPEED_BLOCKS, BlockTags.SOUL_FIRE_BASE_BLOCKS, BlockTags.NETHER_CARVER_REPLACEABLES, BlockTags.SCULK_REPLACEABLE)
					.simpleItem()
					.itemTag(ItemTags.SOUL_FIRE_BASE_BLOCKS)
					.register();

			DEMENTING_SOIL = block("dementing_soil", Block::new)
					.prop(MapColor.COLOR_BROWN, SoundType.SOUL_SOIL).strength(0.5f)
					.cubeAll().shovel()
					.tag(BlockTags.SOUL_SPEED_BLOCKS, BlockTags.SOUL_FIRE_BASE_BLOCKS, BlockTags.NETHER_CARVER_REPLACEABLES, BlockTags.SCULK_REPLACEABLE)
					.simpleItem()
					.itemTag(ItemTags.SOUL_FIRE_BASE_BLOCKS)
					.register();

			TWISTONE = block("twistone", Block::new)
					.prop(MapColor.COLOR_BLACK, SoundType.STONE).strength(0.5f)
					.cubeAll().pickaxe()
					.tag(BlockTags.SOUL_FIRE_BASE_BLOCKS, BlockTags.BASE_STONE_NETHER)
					.simpleItem()
					.itemTag(ItemTags.SOUL_FIRE_BASE_BLOCKS, ItemTags.STONE_CRAFTING_MATERIALS, ItemTags.STONE_TOOL_MATERIALS)
					.register();

			SOUL_SHELL = block("soul_shell", Block::new)
					.prop(MapColor.COLOR_BLACK, SoundType.STONE).strength(0.5f)
					.cubeAll().pickaxe()
					.tag(BlockTags.SOUL_FIRE_BASE_BLOCKS, BlockTags.BASE_STONE_NETHER)
					.simpleItem()
					.itemTag(ItemTags.SOUL_FIRE_BASE_BLOCKS)
					.register();

			DARK_STONE = block("dark_stone", DarkStoneBlock::new)
					.prop(MapColor.COLOR_BLACK, SoundType.STONE).strength(1f, 5f).light(7)
					.cubeAll().pickaxe()
					.simpleItem()
					.shardLoot(() -> DeepNether.ITEMS.DARK_COBBLE.get(), 2, 4)
					.register();

			RAGING_OBSIDIAN = block("raging_obsidian", Block::new)
					.prop(MapColor.COLOR_BLACK, SoundType.STONE).strength(50, 1200)
					.cubeAll().obsidian()
					.tag(Tags.Blocks.OBSIDIANS)
					.simpleItem()
					.itemTag(Tags.Items.OBSIDIANS)
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

		// portal
		{
			PORTAL = block("lava_portal", LavaPortalBlock::new)
					.prop(MapColor.FIRE, SoundType.GLASS).light(15)
					.prop(p -> p.noCollission()
							.strength(-1.0F)
							.pushReaction(PushReaction.BLOCK))
					.noModel()
					.register();
			PORTAL_POI = new SimpleEntry<>(reg.simple("lava_portal", Registries.POINT_OF_INTEREST_TYPE, () -> new PoiType(
					Set.of(PORTAL.getDefaultState()), 0, 1)));
		}

		// fluid
		{
			LIQUID_SOUL = fluid("liquid_soul", LiquidSoulFluidType::new,
					LiquidSoulFluid.Flowing::new, LiquidSoulFluid.Source::new)
					.properties(p -> p.lightLevel(15).temperature(1500))
					.fluidProperties(p -> p.explosionResistance(100).tickRate(10))
					.register();

			MOLTEN_GOLD = fluid("molten_gold", MoltenGoldFluidType::new,
					MoltenGoldFluid.Flowing::new, MoltenGoldFluid.Source::new)
					.properties(p -> p.lightLevel(15).temperature(1500))
					.fluidProperties(p -> p.explosionResistance(100).tickRate(20))
					.register();
		}

	}

}
