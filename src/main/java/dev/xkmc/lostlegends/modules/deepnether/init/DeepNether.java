package dev.xkmc.lostlegends.modules.deepnether.init;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.lostlegends.foundation.module.LLModuleBase;
import dev.xkmc.lostlegends.foundation.module.LLRecipeGen;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.data.DNBiomeGen;
import dev.xkmc.lostlegends.modules.deepnether.data.DNDimensionGen;
import dev.xkmc.lostlegends.modules.deepnether.data.DNFeatures;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class DeepNether extends LLModuleBase {

	public static final String ID = "deepnether";

	public static SimpleEntry<CreativeModeTab> TAB = LostLegends.REGISTRATE.buildModCreativeTab(ID,
			"The Lost Legends - Deep Nether", b -> b.icon(DeepNether.BLOCKS.BURIED_GOLD_DEBRIS::asStack));

	public static DNBlocks BLOCKS = new DNBlocks(LostLegends.REGISTRATE, ID);
	public static DNShrooms SHROOM = new DNShrooms(LostLegends.REGISTRATE, ID + "/shroom");
	public static DNItems ITEMS = new DNItems(LostLegends.REGISTRATE, ID);
	public static DNEntities ENTITY = new DNEntities(LostLegends.REGISTRATE, ID);
	public static DNEffects EFFECTS = new DNEffects(LostLegends.REGISTRATE, ID);
	public static DNWorldGenReg WG = new DNWorldGenReg(LostLegends.REG);

	@Override
	public void commonInit() {
		registerFluidInteraction(
				BLOCKS.LIQUID_SOUL.get().getFluidType(),
				NeoForgeMod.LAVA_TYPE.value(),
				Blocks.CRYING_OBSIDIAN.defaultBlockState(),
				BLOCKS.SOUL_SHELL.getDefaultState(),
				BLOCKS.RAGING_OBSIDIAN.getDefaultState()
		);

		registerFluidInteraction(
				BLOCKS.LIQUID_SOUL.get().getFluidType(),
				NeoForgeMod.WATER_TYPE.value(),
				Blocks.CRYING_OBSIDIAN.defaultBlockState(),
				BLOCKS.DEMENTING_SOIL.getDefaultState(),
				BLOCKS.TWISTONE.getDefaultState()
		);

		registerFluidInteraction(
				BLOCKS.MOLTEN_GOLD.get().getFluidType(),
				NeoForgeMod.WATER_TYPE.value(),
				Blocks.GOLD_BLOCK.defaultBlockState(),//TODO
				Blocks.NETHERRACK.defaultBlockState(),
				BLOCKS.DEEP_NETHERRACK.getDefaultState()
		);

	}

	@Override
	public void gatherData(GatherDataEvent event) {
		LostLegends.REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
		var init = LostLegends.REGISTRATE.getDataGenInitializer();
		DNFeatures.INS.init(init);
		DNBiomeGen.init(init);
		DNDimensionGen.init(init);
	}

	public static ResourceLocation loc(String id) {
		return LostLegends.loc(ID + "/" + id);
	}

	public static class RecipeGen extends LLRecipeGen {

		public static void genRecipe(RegistrateRecipeProvider pvd) {
			classic(pvd, ITEMS.HEARTH_CRYSTAL, Blocks.MAGMA_BLOCK, BLOCKS.DEEP_NETHERRACK, BLOCKS.MAGMA, 8);
			circle(pvd, BLOCKS.DEEP_NETHERRACK, ITEMS.HEARTH_CRYSTAL, BLOCKS.SCORCHED_NETHERRACK, 8);
			circle(pvd, Blocks.SOUL_SAND, BLOCKS.LIQUID_SOUL.getBucket().orElseThrow(), BLOCKS.WEEPING_SAND, 8);
			circle(pvd, Blocks.SOUL_SOIL, BLOCKS.LIQUID_SOUL.getBucket().orElseThrow(), BLOCKS.DEMENTING_SOIL, 8);
			classic(pvd, ITEMS.HEARTH_CRYSTAL, BLOCKS.MAGMA, Items.BLAZE_POWDER, BLOCKS.AMBER_MAGMA, 4);
			circle(pvd, Items.GLOWSTONE_DUST, BLOCKS.LIQUID_SOUL.getBucket().orElseThrow(), BLOCKS.ECTOPLASM, 2);
			square(pvd, BLOCKS.ASH_BLOCK, BLOCKS.ASH_STONE);

			pvd.singleItemUnfinished(DataIngredient.items(BLOCKS.SCORCHED_BONE_VINE.asItem()),
							RecipeCategory.MISC, () -> Items.BONE_MEAL, 1, 1)
					.save(pvd, BLOCKS.SCORCHED_BONE_VINE.getId().withSuffix("_to_bonemeal"));

			pvd.singleItemUnfinished(DataIngredient.items(BLOCKS.BONE_PILE.asItem()),
							RecipeCategory.MISC, () -> Items.BONE_MEAL, 1, 8)
					.save(pvd, BLOCKS.BONE_PILE.getId().withSuffix("_to_bonemeal"));

			pvd.singleItemUnfinished(DataIngredient.items(BLOCKS.DENSE_BONE.asItem()),
							RecipeCategory.MISC, () -> Items.BONE_MEAL, 1, 32)
					.save(pvd, BLOCKS.DENSE_BONE.getId().withSuffix("_to_bonemeal"));

			full(pvd, BLOCKS.SCORCHED_BONE_VINE, BLOCKS.BONE_PILE);
			square(pvd, BLOCKS.BONE_PILE, BLOCKS.DENSE_BONE);

			shaped(pvd, ITEMS.PORTAL_DISSONATOR, 1, ITEMS.RESONANT_SOULGEM)
					.pattern(" B ").pattern("AXA").pattern(" B ")
					.define('X', ITEMS.RESONANT_SOULGEM)
					.define('A', Items.AMETHYST_SHARD)
					.define('B', ITEMS.AMARAST)
					.save(pvd);

		}

		public static void full(RegistrateRecipeProvider pvd, ItemLike in, ItemLike out) {
			shaped(pvd, out, 1, in).pattern("XXX").pattern("XXX").pattern("XXX")
					.define('X', in).save(pvd);
		}

		public static void circle(RegistrateRecipeProvider pvd, ItemLike in, ItemLike center, ItemLike out, int count) {
			shaped(pvd, out, count, center).pattern("XXX").pattern("XOX").pattern("XXX")
					.define('X', in).define('O', center).save(pvd);
		}

		public static void classic(RegistrateRecipeProvider pvd, ItemLike center, ItemLike side, ItemLike corner, ItemLike out, int count) {
			shaped(pvd, out, count, center).pattern("CBC").pattern("BAB").pattern("CBC")
					.define('A', center)
					.define('B', side)
					.define('C', corner)
					.save(pvd);
		}

		public static void square(RegistrateRecipeProvider pvd, ItemLike in, ItemLike out) {
			shaped(pvd, out, 1, in).pattern("XX").pattern("XX").define('X', in).save(pvd);
		}

		public static ShapedRecipeBuilder shaped(RegistrateRecipeProvider pvd, ItemLike out, int count, ItemLike in) {
			return unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, out, count)::unlockedBy, in.asItem());
		}

	}

}
