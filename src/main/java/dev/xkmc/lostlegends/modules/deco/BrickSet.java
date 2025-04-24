package dev.xkmc.lostlegends.modules.deco;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class BrickSet {

	public final BlockEntry<Block> bricks;
	public final BlockEntry<StairBlock> stairs;
	public final BlockEntry<SlabBlock> slab;

	public BrickSet(LLRegBase reg, String id, BlockEntry<Block> base) {
		var side = reg.blockLoc(id + "_bricks");
		bricks = reg.block(id + "_bricks", Block::new)
				.copyProp(base)
				.cubeAll().pickaxe()
				.simpleItem().register();

		stairs = reg.block(id + "_brick_stairs", p -> new StairBlock(bricks.getDefaultState(), p))
				.copyProp(base)
				.blockstate((ctx, pvd) ->
						pvd.stairsBlock(ctx.get(), id + "_brick", side))
				.tag(BlockTags.STAIRS).simpleItem()
				.recipe(this::genStair).register();

		slab = reg.block(id + "_brick_slab", SlabBlock::new)
				.copyProp(base)
				.blockstate((ctx, pvd) -> pvd.slabBlock(ctx.get(),
						pvd.models().slab(ctx.getName(), side, side, side),
						pvd.models().slabTop(ctx.getName() + "_top", side, side, side),
						new ModelFile.UncheckedModelFile(bricks.getId().withPrefix("block/"))))
				.tag(BlockTags.SLABS).simpleItem()
				.recipe(this::genSlab).register();
	}

	private void genStair(DataGenContext<Block, StairBlock> ctx, RegistrateRecipeProvider pvd) {
		pvd.stairs(DataIngredient.items(bricks.get()), RecipeCategory.BUILDING_BLOCKS, ctx, null, true);
	}

	private void genSlab(DataGenContext<Block, SlabBlock> ctx, RegistrateRecipeProvider pvd) {
		pvd.slab(DataIngredient.items(bricks.get()), RecipeCategory.BUILDING_BLOCKS, ctx, null, true);
	}

}