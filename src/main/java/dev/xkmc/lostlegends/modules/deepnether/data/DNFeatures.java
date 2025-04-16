package dev.xkmc.lostlegends.modules.deepnether.data;

import dev.xkmc.lostlegends.foundation.module.FeatureGroup;
import dev.xkmc.lostlegends.foundation.module.FeatureKey;
import dev.xkmc.lostlegends.foundation.module.LLFeatureReg;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import dev.xkmc.lostlegends.modules.deepnether.worldgen.feature.StonePile;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class DNFeatures extends LLFeatureReg {

	public static final DNFeatures INS = new DNFeatures(DeepNether.ID);

	public final Ores ore = new Ores(this, "ore");
	public final Blobs blob = new Blobs(this, "blob");
	public final Simple simple = new Simple(this, "simple");
	public final Structs struct = new Structs(this, "struct");

	public DNFeatures(String path) {
		super(path);
	}

	public static class Ores extends FeatureGroup {

		public final FeatureKey gold = uni("gold_debris");
		public final FeatureKey goldClose = uni("gold_debris_close");
		public final FeatureKey hearth = uni("hearth_crystal");
		public final FeatureKey debrisSmall = uni("debris_small");
		public final FeatureKey debrisLarge = uni("debris_large");

		public Ores(LLFeatureReg parent, String type) {
			super(parent, type);
		}

		@Override
		public void regFeatures(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
			var deepRack = DeepNether.BLOCKS.DEEP_NETHERRACK;
			var deep = new BlockMatchTest(deepRack.get());
			FeatureUtils.register(ctx, gold.cf, Feature.ORE, new OreConfiguration(deep,
					DeepNether.BLOCKS.BURIED_GOLD_DEBRIS.get().defaultBlockState(), 10));
			FeatureUtils.register(ctx, goldClose.cf, Feature.ORE, new OreConfiguration(deep,
					DeepNether.BLOCKS.BURIED_GOLD_DEBRIS.get().defaultBlockState(), 10, 0.8f));
			FeatureUtils.register(ctx, hearth.cf, Feature.ORE, new OreConfiguration(deep,
					DeepNether.BLOCKS.HEARTH_ORE.get().defaultBlockState(), 6));
			FeatureUtils.register(ctx, debrisSmall.cf, Feature.ORE, new OreConfiguration(deep,
					Blocks.ANCIENT_DEBRIS.defaultBlockState(), 3, 0.8f));
			FeatureUtils.register(ctx, debrisLarge.cf, Feature.ORE, new OreConfiguration(deep,
					Blocks.ANCIENT_DEBRIS.defaultBlockState(), 5, 1));
		}

		@Override
		public void regPlacements(BootstrapContext<PlacedFeature> ctx, HolderGetter<ConfiguredFeature<?, ?>> cf) {
			gold.place(ctx, cf, orePlace(20, uniform(10, 250)));
			goldClose.place(ctx, cf, orePlace(20, uniform(10, 250)));
			hearth.place(ctx, cf, orePlace(8, uniform(5, 30)));
			debrisSmall.place(ctx, cf, orePlace(15, uniform(10, 250)));
			debrisLarge.place(ctx, cf, orePlace(4, uniform(8, 70)));
		}
	}


	public static class Blobs extends FeatureGroup {

		public final FeatureKey blackstone = uni("blackstone");
		public final FeatureKey rackSmall = uni("netherrack_small");
		public final FeatureKey rackLarge = uni("netherrack_large");

		public Blobs(LLFeatureReg parent, String type) {
			super(parent, type);
		}

		@Override
		public void regFeatures(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
			var deepRack = DeepNether.BLOCKS.DEEP_NETHERRACK;
			var deep = new BlockMatchTest(deepRack.get());
			FeatureUtils.register(ctx, blackstone.cf, Feature.ORE, new OreConfiguration(deep,
					Blocks.BLACKSTONE.defaultBlockState(), 33));
			FeatureUtils.register(ctx, rackSmall.cf, Feature.ORE, new OreConfiguration(deep,
					Blocks.NETHERRACK.defaultBlockState(), 44));
			FeatureUtils.register(ctx, rackLarge.cf, Feature.ORE, new OreConfiguration(deep,
					Blocks.NETHERRACK.defaultBlockState(), 22));
		}

		@Override
		public void regPlacements(BootstrapContext<PlacedFeature> ctx, HolderGetter<ConfiguredFeature<?, ?>> reg) {
			blackstone.place(ctx, reg, orePlace(2, uniform(10, 250)));
			rackSmall.place(ctx, reg, orePlace(3, uniform(128, 250)));
			rackLarge.place(ctx, reg, orePlace(2, uniform(180, 250)));
		}
	}

	public static class Simple extends FeatureGroup {

		public final FeatureKey springOpen = uni("spring_open");
		public final FeatureKey springClose = uni("spring_close");
		public final FeatureKey springClose2 = springClose.variant("_double");

		public final FeatureKey firePatch = uni("fire_patch");
		public final FeatureKey soulfirePatch = uni("soul_fire_patch");

		public final FeatureKey ashBlossom = uni("ash_blossom");
		public final FeatureKey crimsonRoot = uni("crimson_root");

		public Simple(LLFeatureReg parent, String type) {
			super(parent, type);
		}

		@Override
		public void regFeatures(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
			var deepRack = DeepNether.BLOCKS.DEEP_NETHERRACK;
			var ashStone = DeepNether.BLOCKS.ASH_STONE;
			var blossom = DeepNether.BLOCKS.ASH_BLOSSOM;

			FeatureUtils.register(ctx, springOpen.cf, Feature.SPRING, new SpringConfiguration(
					Fluids.LAVA.defaultFluidState(), false, 4, 1, HolderSet.direct(deepRack)));
			FeatureUtils.register(ctx, springClose.cf, Feature.SPRING, new SpringConfiguration(
					Fluids.LAVA.defaultFluidState(), false, 5, 0, HolderSet.direct(deepRack)));

			FeatureUtils.register(ctx, firePatch.cf, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
					Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.FIRE)),
					List.of(Blocks.NETHERRACK, deepRack.get())));
			FeatureUtils.register(ctx, soulfirePatch.cf, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
					Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.SOUL_FIRE)),
					List.of(Blocks.SOUL_SOIL, Blocks.SOUL_SAND)));

			FeatureUtils.register(ctx, ashBlossom.cf, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
					Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(blossom.get())),
					List.of(ashStone.get())));
			FeatureUtils.register(ctx, crimsonRoot.cf, Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(
					Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.CRIMSON_ROOTS)),
					List.of(Blocks.SOUL_SOIL, Blocks.SOUL_SAND)));
		}

		@Override
		public void regPlacements(BootstrapContext<PlacedFeature> ctx, HolderGetter<ConfiguredFeature<?, ?>> cf) {
			springOpen.place(ctx, cf, orePlace(8, PlacementUtils.RANGE_4_4));
			springClose.place(ctx, cf, orePlace(16, PlacementUtils.RANGE_10_10));
			springClose2.place(ctx, cf, orePlace(32, PlacementUtils.RANGE_10_10));

			firePatch.place(ctx, cf, orePlace(0, 5, PlacementUtils.RANGE_4_4));
			soulfirePatch.place(ctx, cf, orePlace(0, 5, PlacementUtils.RANGE_4_4));

			ashBlossom.place(ctx, cf, orePlace(0, 3, PlacementUtils.RANGE_10_10));
			crimsonRoot.place(ctx, cf, orePlace(0, 3, PlacementUtils.RANGE_10_10));
		}

	}

	public static class Structs extends FeatureGroup {

		public final FeatureKey darkPile = uni("darkstone_pile");

		public Structs(LLFeatureReg parent, String type) {
			super(parent, type);
		}

		@Override
		public void regFeatures(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
			FeatureUtils.register(ctx, darkPile.cf, DeepNether.WG.F_PILE.get(), new StonePile.Data(
					3, 1.5f, 0.5f, Blocks.BLACKSTONE.defaultBlockState(),
					DeepNether.BLOCKS.DARK_STONE.getDefaultState()));
		}

		@Override
		public void regPlacements(BootstrapContext<PlacedFeature> ctx, HolderGetter<ConfiguredFeature<?, ?>> cf) {
			darkPile.place(ctx, cf, orePlace(1, PlacementUtils.RANGE_10_10));
		}

	}

}
