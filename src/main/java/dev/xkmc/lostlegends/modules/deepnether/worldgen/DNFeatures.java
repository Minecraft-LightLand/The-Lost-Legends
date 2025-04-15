package dev.xkmc.lostlegends.modules.deepnether.worldgen;

import com.tterrag.registrate.providers.DataProviderInitializer;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;

import java.util.List;

public class DNFeatures {

	public static final ResourceKey<ConfiguredFeature<?, ?>> CF_GOLD = configured("ore_gold_debris");
	public static final ResourceKey<PlacedFeature> PF_GOLD = place("ore_gold_debris");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CF_HEARTH = configured("ore_hearth_crystal");
	public static final ResourceKey<PlacedFeature> PF_HEARTH = place("ore_hearth_crystal");

	public static final ResourceKey<ConfiguredFeature<?, ?>> CF_DEBRIS_SMALL = configured("ore_debris_small");
	public static final ResourceKey<PlacedFeature> PF_DEBRIS_SMALL = place("ore_debris_small");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CF_DEBRIS_LARGE = configured("ore_debris_large");
	public static final ResourceKey<PlacedFeature> PF_DEBRIS_LARGE = place("ore_debris_large");

	public static final ResourceKey<ConfiguredFeature<?, ?>> CF_BLACKSTONE = configured("blob_blackstone");
	public static final ResourceKey<PlacedFeature> PF_BLACKSTONE = place("blob_blackstone");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CF_RACK_SMALL = configured("blob_netherrack_small");
	public static final ResourceKey<PlacedFeature> PF_RACK_SMALL = place("blob_netherrack_small");
	public static final ResourceKey<ConfiguredFeature<?, ?>> CF_RACK_LARGE = configured("blob_netherrack_large");
	public static final ResourceKey<PlacedFeature> PF_RACK_LARGE = place("blob_netherrack_large");

	public static void init(DataProviderInitializer init) {

		init.add(Registries.CONFIGURED_FEATURE, (ctx) -> {
			var deep = new BlockMatchTest(DeepNether.BLOCKS.DEEP_NETHERRACK.get());
			FeatureUtils.register(ctx, CF_BLACKSTONE, Feature.ORE, new OreConfiguration(deep,
					Blocks.BLACKSTONE.defaultBlockState(), 33));
			FeatureUtils.register(ctx, CF_RACK_SMALL, Feature.ORE, new OreConfiguration(deep,
					Blocks.NETHERRACK.defaultBlockState(), 44));
			FeatureUtils.register(ctx, CF_RACK_LARGE, Feature.ORE, new OreConfiguration(deep,
					Blocks.NETHERRACK.defaultBlockState(), 22));
			FeatureUtils.register(ctx, CF_GOLD, Feature.ORE, new OreConfiguration(deep,
					DeepNether.BLOCKS.BURIED_GOLD_DEBRIS.get().defaultBlockState(), 10));
			FeatureUtils.register(ctx, CF_HEARTH, Feature.ORE, new OreConfiguration(deep,
					DeepNether.BLOCKS.HEARTH_ORE.get().defaultBlockState(), 6));
			FeatureUtils.register(ctx, CF_DEBRIS_SMALL, Feature.ORE, new OreConfiguration(deep,
					Blocks.ANCIENT_DEBRIS.defaultBlockState(), 3));
			FeatureUtils.register(ctx, CF_DEBRIS_LARGE, Feature.ORE, new OreConfiguration(deep,
					Blocks.ANCIENT_DEBRIS.defaultBlockState(), 5));
		});

		init.add(Registries.PLACED_FEATURE, (ctx) -> {
			var cf = ctx.lookup(Registries.CONFIGURED_FEATURE);
			PlacementUtils.register(ctx, PF_BLACKSTONE, cf.getOrThrow(CF_BLACKSTONE), commonOrePlacement(2, uniform(10, 250)));
			PlacementUtils.register(ctx, PF_RACK_SMALL, cf.getOrThrow(CF_RACK_SMALL), commonOrePlacement(3, uniform(128, 250)));
			PlacementUtils.register(ctx, PF_RACK_LARGE, cf.getOrThrow(CF_RACK_LARGE), commonOrePlacement(2, uniform(180, 250)));
			PlacementUtils.register(ctx, PF_GOLD, cf.getOrThrow(CF_GOLD), commonOrePlacement(20, uniform(10, 250)));
			PlacementUtils.register(ctx, PF_HEARTH, cf.getOrThrow(CF_HEARTH), commonOrePlacement(20, uniform(5, 30)));
			PlacementUtils.register(ctx, PF_DEBRIS_SMALL, cf.getOrThrow(CF_DEBRIS_SMALL), commonOrePlacement(3, uniform(10, 250)));
			PlacementUtils.register(ctx, PF_DEBRIS_LARGE, cf.getOrThrow(CF_DEBRIS_LARGE), commonOrePlacement(2, uniform(10, 128)));
		});
	}

	private static HeightRangePlacement uniform(int min, int max) {
		return HeightRangePlacement.uniform(VerticalAnchor.absolute(min), VerticalAnchor.absolute(max));
	}

	private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
		return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
	}

	private static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
		return orePlacement(CountPlacement.of(p_195344_), p_195345_);
	}


	private static ResourceKey<ConfiguredFeature<?, ?>> configured(String id) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, loc(id));
	}

	private static ResourceKey<PlacedFeature> place(String id) {
		return ResourceKey.create(Registries.PLACED_FEATURE, loc(id));
	}

	private static ResourceLocation loc(String id) {
		return LostLegends.loc(id);
	}

}
