package dev.xkmc.lostlegends.modules.deco;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class BrickSet {

	public final BlockEntry<Block> block;
	public final BlockEntry<StairBlock> stairs;
	public final BlockEntry<SlabBlock> slab;
	public final BlockEntry<WallBlock> wall;

	public static BrickSet from(LLRegBase reg, LLRegBase source, BlockEntry<Block> base) {
		return new BrickSet(reg, base.getId().getPath(), base, source.blockLoc(base.getId().getPath()), base);
	}

	public static BrickSet of(LLRegBase reg, String id, BlockEntry<Block> base) {
		return new BrickSet(reg, id + "_brick", base, reg.blockLoc(id + "_bricks"),
				reg.block(id + "_bricks", Block::new).copyProp(base).cubeAll().pickaxe().simpleItem().register());
	}

	private BrickSet(LLRegBase reg, String id, BlockEntry<Block> base, ResourceLocation side, BlockEntry<Block> block) {
		this.block = block;
		stairs = reg.block(id + "_stairs", p -> new StairBlock(block.getDefaultState(), p))
				.copyProp(base)
				.blockstate((ctx, pvd) ->
						pvd.stairsBlock(ctx.get(), id, side))
				.tag(BlockTags.STAIRS).simpleItem()
				.recipe(this::genStair).register();

		slab = reg.block(id + "_slab", SlabBlock::new)
				.copyProp(base)
				.blockstate((ctx, pvd) -> pvd.slabBlock(ctx.get(),
						pvd.models().slab(ctx.getName(), side, side, side),
						pvd.models().slabTop(ctx.getName() + "_top", side, side, side),
						new ModelFile.UncheckedModelFile(block.getId().withPrefix("block/"))))
				.tag(BlockTags.SLABS).simpleItem()
				.recipe(this::genSlab).register();

		wall = reg.block(id + "_wall", WallBlock::new)
				.copyProp(base)
				.blockstate((ctx, pvd) -> pvd.wallBlock(ctx.get(), side))
				.tag(BlockTags.WALLS).simpleItem()
				.itemModel((ctx, pvd) -> pvd.wallInventory(ctx.getName(), side))
				.recipe(this::genWall).register();
	}

	private void genStair(DataGenContext<Block, StairBlock> ctx, RegistrateRecipeProvider pvd) {
		pvd.stairs(DataIngredient.items(block.get()), RecipeCategory.BUILDING_BLOCKS, ctx, null, true);
	}

	private void genSlab(DataGenContext<Block, SlabBlock> ctx, RegistrateRecipeProvider pvd) {
		pvd.slab(DataIngredient.items(block.get()), RecipeCategory.BUILDING_BLOCKS, ctx, null, true);
	}

	private void genWall(DataGenContext<Block, WallBlock> ctx, RegistrateRecipeProvider pvd) {
		pvd.wall(DataIngredient.items(block.get()), RecipeCategory.BUILDING_BLOCKS, ctx);
	}

}