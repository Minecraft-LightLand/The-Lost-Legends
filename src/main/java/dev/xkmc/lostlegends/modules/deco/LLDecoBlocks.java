package dev.xkmc.lostlegends.modules.deco;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.lostlegends.foundation.module.LLModuleBase;
import dev.xkmc.lostlegends.foundation.module.LLRecipeGen;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class LLDecoBlocks extends LLModuleBase {

	private static final String ID = "deco";
	private static final LLRegBase reg = new LLRegBase(LostLegends.REGISTRATE, ID);

	public static final BrickSet TWISTONE, CHISELED_TWISTONE, CHISELED_RESONANT_TWISTONE;
	public static final BlockEntry<Block> RESONANT_TWISTONE;

	static {
		TWISTONE = new BrickSet(reg, "twistone", DeepNether.BLOCKS.TWISTONE);
		CHISELED_TWISTONE = new BrickSet(reg, "chiseled_twistone", DeepNether.BLOCKS.TWISTONE);
		RESONANT_TWISTONE = reg.block("resonant_twistone_bricks", Block::new)
				.copyProp(DeepNether.BLOCKS.RESONANT_TWISTONE)
				.cubeAll().pickaxe().simpleItem().register();
		CHISELED_RESONANT_TWISTONE = new BrickSet(reg, "chiseled_resonant_twistone", DeepNether.BLOCKS.RESONANT_TWISTONE);
	}

	public LLDecoBlocks() {

	}

	@Override
	public void gatherData(GatherDataEvent event) {
		LostLegends.REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
	}

	public static class RecipeGen extends LLRecipeGen {

		public static void genRecipe(RegistrateRecipeProvider pvd) {
			square(pvd, DeepNether.BLOCKS.TWISTONE, TWISTONE.bricks);
			square(pvd, TWISTONE.bricks, CHISELED_TWISTONE.bricks);
			square(pvd, RESONANT_TWISTONE, CHISELED_RESONANT_TWISTONE.bricks);

			cut(pvd, DeepNether.BLOCKS.TWISTONE, TWISTONE.bricks);
			cut(pvd, DeepNether.BLOCKS.TWISTONE, CHISELED_TWISTONE.bricks);
			cut(pvd, TWISTONE.bricks, CHISELED_TWISTONE.bricks);
			cut(pvd, RESONANT_TWISTONE, CHISELED_RESONANT_TWISTONE.bricks);

			shaped(pvd, RESONANT_TWISTONE.get(), 8, DeepNether.ITEMS.RESONANT_SOULGEM)
					.pattern("XXX").pattern("XAX").pattern("XXX")
					.define('X', DeepNether.BLOCKS.TWISTONE)
					.define('A', DeepNether.ITEMS.RESONANT_SOULGEM)
					.save(pvd);

			shaped(pvd, RESONANT_TWISTONE.get(), 8, DeepNether.ITEMS.RESONANT_SOULGEM)
					.pattern("XXX").pattern("XAX").pattern("XXX")
					.define('X', TWISTONE.bricks)
					.define('A', DeepNether.ITEMS.RESONANT_SOULGEM)
					.save(pvd, RESONANT_TWISTONE.getId().withSuffix("_alt"));

		}

		public static void cut(RegistrateRecipeProvider pvd, BlockEntry<Block> in, BlockEntry<Block> out) {
			pvd.stonecutting(DataIngredient.items(in.asItem()), RecipeCategory.BUILDING_BLOCKS, out);
		}

		public static void square(RegistrateRecipeProvider pvd, BlockEntry<Block> in, BlockEntry<Block> out) {
			shaped(pvd, out, 4, in).pattern("XX").pattern("XX").define('X', in).save(pvd);
		}

		public static ShapedRecipeBuilder shaped(RegistrateRecipeProvider pvd, ItemLike out, int count, ItemLike in) {
			return unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, out, count)::unlockedBy, in.asItem());
		}

	}

}
