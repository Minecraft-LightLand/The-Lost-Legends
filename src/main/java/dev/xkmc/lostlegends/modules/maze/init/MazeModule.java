package dev.xkmc.lostlegends.modules.maze.init;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.lostlegends.foundation.module.LLModuleBase;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.maze.block.MazeWallBlock;
import dev.xkmc.lostlegends.modules.maze.structure.MazePiece;
import dev.xkmc.lostlegends.modules.maze.structure.MazeStructure;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Map;

public class MazeModule extends LLModuleBase {

	public static final LLRegBase BLOCKS = new LLRegBase(LostLegends.REGISTRATE, "maze");

	public static final BlockEntry<DelegateBlock> MAZE_WALL;

	public static final Val<StructureType<MazeStructure>> MAZE;
	public static final Val<StructurePieceType> MAZE_PIECE;

	static {

		MAZE_WALL = BLOCKS.block("maze_wall", p -> DelegateBlock.newBaseBlock(p,
						MazeWallBlock.NEIGHBOR, MazeWallBlock.ALL_DIRE_STATE))
				.copyProp(() -> Blocks.OBSIDIAN)
				.prop(e -> e.pushReaction(PushReaction.BLOCK).noLootTable())
				.blockstate(MazeWallBlock.AllDireState::buildModel)
				.tag(BlockTags.WITHER_IMMUNE, BlockTags.DRAGON_IMMUNE)
				.tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL)
				.simpleItem().register();

		var sreg = SR.of(LostLegends.REG, BuiltInRegistries.STRUCTURE_TYPE);
		MAZE = sreg.reg("maze", () -> () -> MazeStructure.CODEC);

		var preg = SR.of(LostLegends.REG, BuiltInRegistries.STRUCTURE_PIECE);
		MAZE_PIECE = preg.reg("maze", () -> MazePiece::new);

	}

	@Override
	public void commonInit() {
	}

	@Override
	public void gatherData(GatherDataEvent event) {
		StructureDef def = new StructureDef(
				LostLegends.loc("maze"), Tags.Biomes.IS_OCEAN, 80, 60,
				List.of(), Map.of()
		);

		var init = LostLegends.REGISTRATE.getDataGenInitializer();
		init.add(Registries.STRUCTURE, ctx -> {
			var biome = ctx.lookup(Registries.BIOME).getOrThrow(def.biomes());
			ctx.register(ResourceKey.create(Registries.STRUCTURE, def.id()), new MazeStructure(
					new Structure.StructureSettings(biome, def.spawns(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_BOX),
					def.id, List.of("hallway", "library", "laboratory", "prison"), 15, 11, 7, 3)
			);
		});
		init.add(Registries.STRUCTURE_SET, ctx -> {
			var str = ctx.lookup(Registries.STRUCTURE).getOrThrow(ResourceKey.create(Registries.STRUCTURE, def.id));
			ctx.register(ResourceKey.create(Registries.STRUCTURE_SET, def.id), new StructureSet(
					str, new RandomSpreadStructurePlacement(def.spacing(), def.separation(), RandomSpreadType.LINEAR, def.id.hashCode() & 0x7fffffff)));
		});
	}


	private record StructureDef(
			ResourceLocation id, TagKey<Biome> biomes, int spacing, int separation,
			List<StructureProcessor> processors,
			Map<MobCategory, StructureSpawnOverride> spawns) {
	}

}
