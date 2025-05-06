package dev.xkmc.lostlegends.modules.deco;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.lostlegends.foundation.module.LLModuleBase;
import dev.xkmc.lostlegends.foundation.module.LLRecipeGen;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class LLDecoBlocks extends LLModuleBase {

	private static final String ID = "deco";
	private static final LLRegBase reg = new LLRegBase(LostLegends.REGISTRATE, ID);

	public static SimpleEntry<CreativeModeTab> TAB = LostLegends.REGISTRATE.buildModCreativeTab(ID,
			"The Lost Legends - Building Blocks", b -> b.icon(LLDecoBlocks.RESONANT_TWISTONE::asStack));

	public static final BrickSet DEEP_NETHERRACK, DEEP_BLACKSTONE, ASH_STONE, TWISTONE,
			TWISTONE_BRICKS, CHISELED_TWISTONE, CHISELED_RESONANT_TWISTONE;
	public static final BlockEntry<Block> RESONANT_TWISTONE, DARK_STONE;
	public static final BlockEntry<HalfTransparentBlock> AMBER_MAGMA_BRICKS, ECTOPLASM_BRICKS;

	static {
		DEEP_NETHERRACK = BrickSet.from(reg, DeepNether.BLOCKS, DeepNether.BLOCKS.DEEP_NETHERRACK);
		DEEP_BLACKSTONE = BrickSet.from(reg, DeepNether.BLOCKS, DeepNether.BLOCKS.DEEP_BLACKSTONE);
		ASH_STONE = BrickSet.from(reg, DeepNether.BLOCKS, DeepNether.BLOCKS.ASH_STONE);
		TWISTONE = BrickSet.from(reg, DeepNether.BLOCKS, DeepNether.BLOCKS.TWISTONE);

		TWISTONE_BRICKS = BrickSet.of(reg, "twistone", DeepNether.BLOCKS.TWISTONE);
		CHISELED_TWISTONE = BrickSet.of(reg, "chiseled_twistone", DeepNether.BLOCKS.TWISTONE);
		RESONANT_TWISTONE = reg.block("resonant_twistone_bricks", Block::new)
				.copyProp(DeepNether.BLOCKS.RESONANT_TWISTONE).cubeAll().pickaxe().simpleItem().register();
		DARK_STONE = reg.block("dark_stone_bricks", Block::new)
				.copyProp(DeepNether.BLOCKS.DARK_STONE).cubeAll().pickaxe().simpleItem().register();
		CHISELED_RESONANT_TWISTONE = BrickSet.of(reg, "chiseled_resonant_twistone", DeepNether.BLOCKS.RESONANT_TWISTONE);

		{
			AMBER_MAGMA_BRICKS = reg.block("amber_magma_bricks", HalfTransparentBlock::new)
					.copyProp(DeepNether.BLOCKS.AMBER_MAGMA)
					.blockstate((ctx, pvd) ->
							pvd.simpleBlock(ctx.get(), pvd.models().getBuilder(ctx.getName())
									.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/slime_like")))
									.texture("overlay", DeepNether.BLOCKS.blockLoc("amber_magma_overlay"))
									.texture("base", reg.blockLoc(ctx.getName()))
									.renderType("translucent")))
					.pickaxe().simpleItem().register();

			ECTOPLASM_BRICKS = reg.block("ectoplasm_bricks", HalfTransparentBlock::new)
					.copyProp(DeepNether.BLOCKS.ECTOPLASM)
					.blockstate((ctx, pvd) ->
							pvd.simpleBlock(ctx.get(), pvd.models().getBuilder(ctx.getName())
									.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/slime_like")))
									.texture("overlay", DeepNether.BLOCKS.blockLoc("ectoplasm_overlay"))
									.texture("base", reg.blockLoc(ctx.getName()))
									.renderType("translucent")))
					.pickaxe().simpleItem().register();
		}
	}

	public LLDecoBlocks() {

	}

	@Override
	public void gatherData(GatherDataEvent event) {
		LostLegends.REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
	}

	public static class RecipeGen extends LLRecipeGen {

		public static void genRecipe(RegistrateRecipeProvider pvd) {
			square(pvd, DeepNether.BLOCKS.TWISTONE, TWISTONE_BRICKS.block);
			square(pvd, DeepNether.BLOCKS.DARK_STONE, DARK_STONE);
			square(pvd, TWISTONE_BRICKS.block, CHISELED_TWISTONE.block);
			square(pvd, RESONANT_TWISTONE, CHISELED_RESONANT_TWISTONE.block);
			square(pvd, DeepNether.BLOCKS.AMBER_MAGMA, AMBER_MAGMA_BRICKS);
			square(pvd, DeepNether.BLOCKS.ECTOPLASM, ECTOPLASM_BRICKS);

			cut(pvd, DeepNether.BLOCKS.TWISTONE, TWISTONE_BRICKS.block);
			cut(pvd, DeepNether.BLOCKS.TWISTONE, CHISELED_TWISTONE.block);
			cut(pvd, DeepNether.BLOCKS.DARK_STONE, DARK_STONE);
			cut(pvd, TWISTONE_BRICKS.block, CHISELED_TWISTONE.block);
			cut(pvd, RESONANT_TWISTONE, CHISELED_RESONANT_TWISTONE.block);
			cut(pvd, DeepNether.BLOCKS.AMBER_MAGMA, AMBER_MAGMA_BRICKS);
			cut(pvd, DeepNether.BLOCKS.ECTOPLASM, ECTOPLASM_BRICKS);

			shaped(pvd, RESONANT_TWISTONE.get(), 8, DeepNether.ITEMS.RESONANT_SOULGEM)
					.pattern("XXX").pattern("XAX").pattern("XXX")
					.define('X', DeepNether.BLOCKS.TWISTONE)
					.define('A', DeepNether.ITEMS.RESONANT_SOULGEM)
					.save(pvd);

			shaped(pvd, RESONANT_TWISTONE.get(), 8, DeepNether.ITEMS.RESONANT_SOULGEM)
					.pattern("XXX").pattern("XAX").pattern("XXX")
					.define('X', TWISTONE_BRICKS.block)
					.define('A', DeepNether.ITEMS.RESONANT_SOULGEM)
					.save(pvd, RESONANT_TWISTONE.getId().withSuffix("_alt"));

		}

		public static void cut(RegistrateRecipeProvider pvd, ItemLike in, ItemLike out) {
			pvd.stonecutting(DataIngredient.items(in.asItem()), RecipeCategory.BUILDING_BLOCKS, out::asItem);
		}

		public static void square(RegistrateRecipeProvider pvd, ItemLike in, ItemLike out) {
			shaped(pvd, out, 4, in).pattern("XX").pattern("XX").define('X', in).save(pvd);
		}

		public static ShapedRecipeBuilder shaped(RegistrateRecipeProvider pvd, ItemLike out, int count, ItemLike in) {
			return unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, out, count)::unlockedBy, in.asItem());
		}

	}

}
