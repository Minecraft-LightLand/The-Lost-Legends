package dev.xkmc.lostlegends.modules.deepnether.init;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.modules.deepnether.block.AshBlock;
import dev.xkmc.lostlegends.modules.deepnether.block.DNNyliumBlock;
import dev.xkmc.lostlegends.modules.deepnether.block.DarkStoneBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class DNBlocks extends LLRegBase {

	public final BlockEntry<Block> NETHER_SOIL, ASH_STONE, RAGING_OBSIDIAN;
	public final BlockEntry<AshBlock> ASH_BLOCK;
	public final BlockEntry<DarkStoneBlock> DARK_STONE;
	public final BlockEntry<DNNyliumBlock> NETHER_NYLIUM;
	public final BlockEntry<FlowerBlock> ASH_BLOSSOM;


	DNBlocks(L2Registrate reg, String path) {
		super(reg, path);

		// stone
		{
			NETHER_SOIL = block("nether_soil", Block::new)
					.prop(MapColor.NETHER, SoundType.GRAVEL).strength(0.5F)
					.cubeAll().shovel()
					.tag(BlockTags.INFINIBURN_OVERWORLD)
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
					.simpleItem()
					.register();

			ASH_STONE = block("ash_stone", Block::new)
					.prop(MapColor.COLOR_BLACK, SoundType.STONE).strength(0.8F, 12f)
					.cubeAll().pickaxe()
					.simpleItem()
					.register();

			DARK_STONE = block("dark_stone", DarkStoneBlock::new)
					.prop(MapColor.COLOR_BLACK, SoundType.STONE).strength(1f, 5f)
					.prop(p -> p.lightLevel(state -> 7))
					.cubeAll().pickaxe()
					.simpleItem()
					.register();

			RAGING_OBSIDIAN = block("raging_obsidian", Block::new)
					.prop(MapColor.COLOR_BLACK, SoundType.STONE).strength(50, 1200)
					.cubeAll().obsidian()
					.simpleItem()
					.register();

		}

		{
			ASH_BLOSSOM = block("ash_blossom", p -> new FlowerBlock(MobEffects.WEAKNESS, 6.0F, p))
					.foliage()
					.cross()
					.simpleItem()
					.register();

		}

	}
}
