package dev.xkmc.lostlegends.modules.deepnether.data;

import dev.xkmc.lostlegends.foundation.module.FeatureGroup;
import dev.xkmc.lostlegends.foundation.module.FeatureKey;
import dev.xkmc.lostlegends.foundation.module.LLFeatureReg;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import dev.xkmc.lostlegends.modules.deepnether.worldgen.feature.*;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class DNFeatures extends LLFeatureReg {

	public static final DNFeatures INS = new DNFeatures(DeepNether.ID);

	public final Ores ore = new Ores(this, "ore");
	public final Blobs blob = new Blobs(this, "blob");
	public final Simple simple = new Simple(this, "simple");
	public final Vegetation vege = new Vegetation(this, "vegetation");
	public final Structs struct = new Structs(this, "struct");
	public final Tree tree = new Tree(this, "tree");
	public final Delta delta = new Delta(this, "delta");

	public DNFeatures(String path) {
		super(path);
	}

	public static class Ores extends FeatureGroup {

		public final FeatureKey gold = uni("gold_debris");
		public final FeatureKey goldClose = uni("gold_debris_close");
		public final FeatureKey hearth = uni("hearth_crystal");
		public final FeatureKey amarast = uni("amarast");
		public final FeatureKey resonant = uni("resonating_twistone");
		public final FeatureKey debrisSmall = uni("debris_small");
		public final FeatureKey debrisLarge = uni("debris_large");

		public Ores(LLFeatureReg parent, String type) {
			super(parent, type);
		}

		@Override
		public void regFeatures(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
			var rack = new BlockMatchTest(DeepNether.BLOCKS.DEEP_NETHERRACK.get());
			var black = new BlockMatchTest(DeepNether.BLOCKS.DEEP_BLACKSTONE.get());
			var warped = new BlockMatchTest(DeepNether.BLOCKS.TWISTONE.get());
			var all = new TagMatchTest(BlockTags.BASE_STONE_NETHER);
			FeatureUtils.register(ctx, gold.cf, Feature.ORE, new OreConfiguration(rack,
					DeepNether.BLOCKS.BURIED_GOLD_DEBRIS.get().defaultBlockState(), 10));
			FeatureUtils.register(ctx, goldClose.cf, Feature.ORE, new OreConfiguration(rack,
					DeepNether.BLOCKS.BURIED_GOLD_DEBRIS.get().defaultBlockState(), 10, 0.8f));
			FeatureUtils.register(ctx, hearth.cf, Feature.ORE, new OreConfiguration(rack,
					DeepNether.BLOCKS.HEARTH_ORE.get().defaultBlockState(), 6));
			FeatureUtils.register(ctx, amarast.cf, Feature.ORE, new OreConfiguration(black,
					DeepNether.BLOCKS.AMARAST_ORE.get().defaultBlockState(), 4));
			FeatureUtils.register(ctx, resonant.cf, Feature.ORE, new OreConfiguration(warped,
					DeepNether.BLOCKS.RESONANT_TWISTONE.get().defaultBlockState(), 4));
			FeatureUtils.register(ctx, debrisSmall.cf, Feature.ORE, new OreConfiguration(all,
					Blocks.ANCIENT_DEBRIS.defaultBlockState(), 3, 0.8f));
			FeatureUtils.register(ctx, debrisLarge.cf, Feature.ORE, new OreConfiguration(all,
					Blocks.ANCIENT_DEBRIS.defaultBlockState(), 5, 1));
		}

		@Override
		public void regPlacements(BootstrapContext<PlacedFeature> ctx, HolderGetter<ConfiguredFeature<?, ?>> cf) {
			gold.place(ctx, cf, spread(20, uniform(10, 250)));
			goldClose.place(ctx, cf, spread(10, uniform(10, 120)));
			hearth.place(ctx, cf, spread(8, uniform(5, 30)));
			amarast.place(ctx, cf, spread(40, uniform(10, 250)));
			resonant.place(ctx, cf, spread(40, uniform(10, 250)));
			debrisSmall.place(ctx, cf, spread(15, uniform(10, 250)));
			debrisLarge.place(ctx, cf, spread(4, uniform(8, 70)));
		}
	}

	public static class Blobs extends FeatureGroup {

		public final FeatureKey blackstone = uni("blackstone");
		public final FeatureKey twist = uni("twistone");
		public final FeatureKey rackSmall = uni("netherrack_small");
		public final FeatureKey rackLarge = uni("netherrack_large");
		public final FeatureKey magma = uni("magma");

		public Blobs(LLFeatureReg parent, String type) {
			super(parent, type);
		}

		@Override
		public void regFeatures(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
			var deepRack = DeepNether.BLOCKS.DEEP_NETHERRACK;
			var deep = new BlockMatchTest(deepRack.get());
			FeatureUtils.register(ctx, blackstone.cf, Feature.ORE, new OreConfiguration(deep,
					DeepNether.BLOCKS.DEEP_BLACKSTONE.getDefaultState(), 33));
			FeatureUtils.register(ctx, twist.cf, Feature.ORE, new OreConfiguration(deep,
					DeepNether.BLOCKS.TWISTONE.getDefaultState(), 33));
			FeatureUtils.register(ctx, rackSmall.cf, Feature.ORE, new OreConfiguration(deep,
					Blocks.NETHERRACK.defaultBlockState(), 44));
			FeatureUtils.register(ctx, rackLarge.cf, Feature.ORE, new OreConfiguration(deep,
					Blocks.NETHERRACK.defaultBlockState(), 22));
			FeatureUtils.register(ctx, magma.cf, Feature.ORE, new OreConfiguration(deep,
					DeepNether.BLOCKS.MAGMA.getDefaultState(), 18));
		}

		@Override
		public void regPlacements(BootstrapContext<PlacedFeature> ctx, HolderGetter<ConfiguredFeature<?, ?>> reg) {
			blackstone.place(ctx, reg, spread(3, uniform(10, 250)));
			twist.place(ctx, reg, spread(5, uniform(10, 250)));
			rackSmall.place(ctx, reg, spread(3, uniform(128, 250)));
			rackLarge.place(ctx, reg, spread(2, uniform(180, 250)));
			magma.place(ctx, reg, spreadRare(2, uniform(4, 34)));
		}
	}

	public static class Simple extends FeatureGroup {

		public final FeatureKey springOpen = uni("spring_open");
		public final FeatureKey springClose = uni("spring_close");
		public final FeatureKey springClose2 = springClose.variant("_double");

		public final FeatureKey firePatch = uni("fire_patch");
		public final FeatureKey soulfirePatch = uni("soul_fire_patch");
		public final FeatureKey amber = uni("amber_magma");
		public final FeatureKey ecto = uni("ectoplasm");

		public Simple(LLFeatureReg parent, String type) {
			super(parent, type);
		}

		@Override
		public void regFeatures(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
			var deepRack = DeepNether.BLOCKS.DEEP_NETHERRACK;
			var ashStone = DeepNether.BLOCKS.ASH_STONE;
			var soulSoil = DeepNether.BLOCKS.DEMENTING_SOIL;
			var soulSand = DeepNether.BLOCKS.WEEPING_SAND;

			FeatureUtils.register(ctx, springOpen.cf, Feature.SPRING, new SpringConfiguration(
					Fluids.LAVA.defaultFluidState(), false, 4, 1, HolderSet.direct(deepRack)));
			FeatureUtils.register(ctx, springClose.cf, Feature.SPRING, new SpringConfiguration(
					Fluids.LAVA.defaultFluidState(), false, 5, 0, HolderSet.direct(deepRack)));

			FeatureUtils.register(ctx, firePatch.cf, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
					Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.FIRE)),
					List.of(Blocks.NETHERRACK, deepRack.get())));
			FeatureUtils.register(ctx, soulfirePatch.cf, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
					Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.SOUL_FIRE)),
					List.of(soulSoil.get(), soulSand.get())));

			FeatureUtils.register(ctx, amber.cf, DeepNether.WG.IN_GROUND.get(), new InGroundFeature.Data(
					DeepNether.BLOCKS.AMBER_MAGMA.getDefaultState(), new BlockMatchTest(DeepNether.BLOCKS.DEEP_NETHERRACK.get())));
			FeatureUtils.register(ctx, ecto.cf, DeepNether.WG.IN_GROUND.get(), new InGroundFeature.Data(
					DeepNether.BLOCKS.ECTOPLASM.getDefaultState(), new TagMatchTest(BlockTags.SOUL_SPEED_BLOCKS)));
		}

		@Override
		public void regPlacements(BootstrapContext<PlacedFeature> ctx, HolderGetter<ConfiguredFeature<?, ?>> cf) {
			springOpen.place(ctx, cf, spread(8, PlacementUtils.RANGE_4_4));
			springClose.place(ctx, cf, spread(16, PlacementUtils.RANGE_10_10));
			springClose2.place(ctx, cf, spread(32, PlacementUtils.RANGE_10_10));

			firePatch.place(ctx, cf, spread(2, 5, PlacementUtils.RANGE_4_4));
			soulfirePatch.place(ctx, cf, spread(2, 5, PlacementUtils.RANGE_4_4));
			amber.place(ctx, cf, spread(20, PlacementUtils.RANGE_4_4));
			ecto.place(ctx, cf, spread(20, PlacementUtils.RANGE_4_4));

		}

	}

	public static class Vegetation extends FeatureGroup {

		public final FeatureKey weepingVine = uni("weeping_vines");
		public final FeatureKey weepingVineSparse = weepingVine.variant("_sparse");
		public final FeatureKey boneVine = uni("scorched_bone_vines");
		public final FeatureKey soulVine = uni("screaming_soul_vines");
		public final FeatureKey ashBlossom = uni("ash_blossom");
		public final FeatureKey scarletBlossom = uni("scarlet_blossom");
		public final FeatureKey soulBlossom = uni("soul_blossom");
		public final FeatureKey scarletRoot = uni("scarlet_root");
		public final FeatureKey hearthroom = uni("hearthroom");
		public final FeatureKey ghoshroom = uni("ghoshroom");

		public final FeatureKey crimsonVegetation = uni("crimson_vegetation");
		public final FeatureKey crimsonBonemeal = uni("crimson_bonemeal");
		public final FeatureKey goldenVegetation = uni("golden_vegetation");
		public final FeatureKey goldenBonemeal = uni("golden_bonemeal");

		public Vegetation(LLFeatureReg parent, String type) {
			super(parent, type);
		}

		@Override
		public void regFeatures(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
			var deepRack = DeepNether.BLOCKS.DEEP_NETHERRACK;
			var ashStone = DeepNether.BLOCKS.ASH_STONE;
			var soulSoil = DeepNether.BLOCKS.DEMENTING_SOIL;
			var soulSand = DeepNether.BLOCKS.WEEPING_SAND;

			FeatureUtils.register(ctx, weepingVine.cf, DeepNether.WG.WEEPING_VINE.get(), new WeepingVinesFeature.Data(
					32, 6, 16, 8, 1 / 8f));
			FeatureUtils.register(ctx, boneVine.cf, DeepNether.WG.FLUID_VINE.get(), new FluidLoggedVinesFeature.Data(
					DeepNether.BLOCKS.SCORCHED_BONE_VINE.get(), 8, 16, 3, 7));
			FeatureUtils.register(ctx, soulVine.cf, DeepNether.WG.FLUID_VINE.get(), new FluidLoggedVinesFeature.Data(
					DeepNether.BLOCKS.SCREAMING_SOUL_VINE.get(), 8, 16, 3, 12));
			FeatureUtils.register(ctx, ashBlossom.cf, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
					Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(DeepNether.BLOCKS.ASH_BLOSSOM.get())),
					List.of(ashStone.get())));
			FeatureUtils.register(ctx, scarletBlossom.cf, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
					Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(DeepNether.BLOCKS.SCARLET_BLOSSOM.get())),
					List.of(DeepNether.BLOCKS.CRIMSON_MYCELIUM.get(), Blocks.CRIMSON_NYLIUM, Blocks.NETHER_WART_BLOCK)));
			FeatureUtils.register(ctx, soulBlossom.cf, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
					Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(DeepNether.BLOCKS.SOUL_BLOSSOM.get())),
					List.of(soulSoil.get(), soulSand.get())));
			FeatureUtils.register(ctx, scarletRoot.cf, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
					Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(DeepNether.BLOCKS.SCARLET_ROOTS.get())),
					List.of(soulSoil.get(), soulSand.get())));
			FeatureUtils.register(ctx, hearthroom.cf, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
					Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(DeepNether.SHROOM.HEARTHROOM.get())),
					List.of(deepRack.get())));
			FeatureUtils.register(ctx, ghoshroom.cf, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
					Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(DeepNether.SHROOM.GHOSHROOM.get())),
					List.of(soulSoil.get(), soulSand.get())));


			WeightedStateProvider crimson = new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
					.add(DeepNether.BLOCKS.SCARLET_ROOTS.getDefaultState(), 87)
					.add(DeepNether.SHROOM.HEARTHROOM.getDefaultState(), 11)
			);
			FeatureUtils.register(ctx, crimsonVegetation.cf, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig(crimson, 8, 4));
			FeatureUtils.register(ctx, crimsonBonemeal.cf, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig(crimson, 3, 1));

			WeightedStateProvider golden = new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
					.add(DeepNether.BLOCKS.SCARLET_ROOTS.getDefaultState(), 87)
					.add(DeepNether.SHROOM.HEARTHROOM.getDefaultState(), 11)//TODO
			);
			FeatureUtils.register(ctx, goldenVegetation.cf, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig(golden, 8, 4));
			FeatureUtils.register(ctx, goldenBonemeal.cf, Feature.NETHER_FOREST_VEGETATION, new NetherForestVegetationConfig(golden, 3, 1));
		}

		@Override
		public void regPlacements(BootstrapContext<PlacedFeature> ctx, HolderGetter<ConfiguredFeature<?, ?>> cf) {
			weepingVine.place(ctx, cf, spread(40, PlacementUtils.RANGE_4_4));
			weepingVineSparse.place(ctx, cf, spread(20, PlacementUtils.RANGE_4_4));
			boneVine.place(ctx, cf, layer(4));
			soulVine.place(ctx, cf, spread(4, uniform(24, 33)));
			ashBlossom.place(ctx, cf, spread(3, 6, PlacementUtils.RANGE_10_10));
			scarletBlossom.place(ctx, cf, spread(3, 6, PlacementUtils.RANGE_10_10));
			soulBlossom.place(ctx, cf, spread(3, 6, PlacementUtils.RANGE_10_10));
			scarletRoot.place(ctx, cf, spread(2, 5, PlacementUtils.RANGE_10_10));
			hearthroom.place(ctx, cf, spread(5, PlacementUtils.RANGE_10_10));
			ghoshroom.place(ctx, cf, spread(5, PlacementUtils.RANGE_10_10));

			crimsonVegetation.place(ctx, cf, layer(6));
			goldenVegetation.place(ctx, cf, layer(6));

		}

	}

	public static class Structs extends FeatureGroup {

		public final FeatureKey deepPortal = uni("deep_nether_portal");
		public final FeatureKey netherPortal = uni("nether_portal");
		public final FeatureKey darkPile = uni("darkstone_pile");

		public final FeatureKey lavaLake = uni("lava_lake");
		public final FeatureKey soulLake = uni("soul_lake");
		public final FeatureKey goldLake = uni("gold_lake");
		public final FeatureKey lavaIsland = uni("lava_island");
		public final FeatureKey soulIsland = uni("soul_island");

		public Structs(LLFeatureReg parent, String type) {
			super(parent, type);
		}

		@Override
		public void regFeatures(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
			FeatureUtils.register(ctx, deepPortal.cf, DeepNether.WG.DEEP_PORTAL.get(),
					new DeepNetherPortal.Data(2, 6, 10, 30));
			FeatureUtils.register(ctx, netherPortal.cf, DeepNether.WG.NETHER_PORTAL.get(),
					new NetherVolcanoPortal.Data(2, 6, 32, 52, 4, 6, 1.5f,
							0.3f, DeepNether.BLOCKS.DEEP_NETHERRACK.get().defaultBlockState(),
							0.5f, Blocks.MAGMA_BLOCK.defaultBlockState()
					));
			FeatureUtils.register(ctx, darkPile.cf, DeepNether.WG.STONE_PILE.get(), new StonePile.Data(
					3, 1.5f, 0.5f, DeepNether.BLOCKS.DEEP_BLACKSTONE.get().defaultBlockState(),
					DeepNether.BLOCKS.DARK_STONE.getDefaultState()));

			FeatureUtils.register(ctx, lavaLake.cf, DeepNether.WG.LAKE.get(), new LakeFeature.Data(
					Blocks.LAVA.defaultBlockState(), DeepNether.BLOCKS.MAGMA.getDefaultState(),
					4, 16, 8, 4, 8, 6));
			FeatureUtils.register(ctx, soulLake.cf, DeepNether.WG.LAKE.get(), new LakeFeature.Data(
					DeepNether.BLOCKS.LIQUID_SOUL.getSource().defaultFluidState().createLegacyBlock(),
					DeepNether.BLOCKS.TWISTONE.getDefaultState(),
					4, 16, 8, 4, 8, 6));
			FeatureUtils.register(ctx, goldLake.cf, DeepNether.WG.LAKE.get(), new LakeFeature.Data(
					DeepNether.BLOCKS.MOLTEN_GOLD.getSource().defaultFluidState().createLegacyBlock(),
					DeepNether.BLOCKS.BURIED_GOLD_DEBRIS.getDefaultState(),
					4, 10, 8, 4, 6, 4));

			FeatureUtils.register(ctx, lavaIsland.cf, DeepNether.WG.LAKE_ISLAND.get(), new LakeIslandFeature.Data(
					Blocks.LAVA.defaultBlockState(),
					DeepNether.BLOCKS.MAGMA.getDefaultState(),
					DeepNether.BLOCKS.DEEP_NETHERRACK.getDefaultState(),
					DeepNether.BLOCKS.SCORCHED_NETHERRACK.getDefaultState(),
					6, 24, 10, 10, 14, 6,
					4, 16, 6, List.of(
					patch(16, 6, 0, DeepNether.SHROOM.SCORCHROOM.get(), DeepNether.BLOCKS.SCORCHED_NETHERRACK.get()),
					patch(2, 6, 0, DeepNether.SHROOM.BISCORCHROOM.get(), DeepNether.BLOCKS.SCORCHED_NETHERRACK.get())
			)));

			FeatureUtils.register(ctx, soulIsland.cf, DeepNether.WG.LAKE_ISLAND.get(), new LakeIslandFeature.Data(
					DeepNether.BLOCKS.LIQUID_SOUL.getSource().defaultFluidState().createLegacyBlock(),
					DeepNether.BLOCKS.TWISTONE.getDefaultState(),
					DeepNether.BLOCKS.TWISTONE.getDefaultState(),
					DeepNether.BLOCKS.TWISTONE.getDefaultState(),
					6, 24, 10, 10, 14, 6,
					3, 16, 6, List.of(
					patch(16, 6, 0, DeepNether.SHROOM.GHOSHROOM.get(), DeepNether.BLOCKS.TWISTONE.get()),
					patch(2, 6, 0, DeepNether.SHROOM.EYED_GHOSHROOM.get(), DeepNether.BLOCKS.TWISTONE.get())
			)));
		}

		@Override
		public void regPlacements(BootstrapContext<PlacedFeature> ctx, HolderGetter<ConfiguredFeature<?, ?>> cf) {
			deepPortal.place(ctx, cf, spreadRare(16));
			netherPortal.place(ctx, cf, spreadRare(16));
			darkPile.place(ctx, cf, spread(1, PlacementUtils.RANGE_10_10));
			lavaLake.place(ctx, cf, spreadRare(2, uniform(60, 200)));
			soulLake.place(ctx, cf, spreadRare(2, uniform(60, 200)));
			goldLake.place(ctx, cf, spreadRare(2, uniform(35, 200)));
			lavaIsland.place(ctx, cf, spreadRare(16, uniform(90, 200)));
			soulIsland.place(ctx, cf, spreadRare(16, uniform(90, 200)));
		}

		private static ConfiguredFeature<?, ?> patch(int tries, int xz, int y, Block block, Block... base) {
			return new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(tries, xz, y,
					PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block)),
							BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(),
									List.of(base))))));
		}

	}

	public static class Tree extends FeatureGroup {

		public final FeatureKey crimson = uni("crimson_fungus");
		public final FeatureKey crimsonShort = uni("crimson_fungus_short");

		public Tree(LLFeatureReg parent, String type) {
			super(parent, type);
		}

		@Override
		public void regFeatures(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
			FeatureUtils.register(ctx, crimson.cf, DeepNether.WG.TREE.get(),
					new HugeFungus.Data(
							Blocks.CRIMSON_STEM.defaultBlockState(),
							Blocks.NETHER_WART_BLOCK.defaultBlockState(),
							Blocks.SHROOMLIGHT.defaultBlockState(),
							4, 13, 1 / 8f, 1 / 16f, true, false
					)
			);
			FeatureUtils.register(ctx, crimsonShort.cf, DeepNether.WG.TREE.get(),
					new HugeFungus.Data(
							Blocks.CRIMSON_STEM.defaultBlockState(),
							Blocks.NETHER_WART_BLOCK.defaultBlockState(),
							Blocks.SHROOMLIGHT.defaultBlockState(),
							4, 7, 0, 1 / 8f, true, false
					)
			);

		}

		@Override
		public void regPlacements(BootstrapContext<PlacedFeature> ctx, HolderGetter<ConfiguredFeature<?, ?>> reg) {
			crimson.place(ctx, reg, layer(8));
			crimsonShort.place(ctx, reg, layer(0, 2));
		}
	}

	public static class Delta extends FeatureGroup {

		public final FeatureKey columnSmall = uni("small_bone_columns");
		public final FeatureKey columnLarge = uni("large_bone_columns");

		public Delta(LLFeatureReg parent, String type) {
			super(parent, type);
		}

		@Override
		public void regFeatures(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
			var dense = DeepNether.BLOCKS.DENSE_BONE.get().defaultBlockState();
			var bone = DeepNether.BLOCKS.BONE_PILE.get().defaultBlockState();
			FeatureUtils.register(ctx, columnSmall.cf, DeepNether.WG.COLUMN_CLUSTERS.get(), new ColumnClusters.Data(dense, bone, ConstantInt.of(1), UniformInt.of(1, 4)));
			FeatureUtils.register(ctx, columnLarge.cf, DeepNether.WG.COLUMN_CLUSTERS.get(), new ColumnClusters.Data(dense, bone, UniformInt.of(2, 3), UniformInt.of(5, 10))
			);
		}

		@Override
		public void regPlacements(BootstrapContext<PlacedFeature> ctx, HolderGetter<ConfiguredFeature<?, ?>> reg) {
			columnSmall.place(ctx, reg, layer(4));
			columnLarge.place(ctx, reg, layer(2));
		}
	}

}
