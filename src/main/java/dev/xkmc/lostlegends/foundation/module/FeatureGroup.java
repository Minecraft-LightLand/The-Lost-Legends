package dev.xkmc.lostlegends.foundation.module;

import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public abstract class FeatureGroup {

	protected final LLFeatureReg parent;
	protected final String type;

	public FeatureGroup(LLFeatureReg parent, String type) {
		this.parent = parent;
		this.type = type;
		parent.groups.add(this);
	}

	public abstract void regFeatures(BootstrapContext<ConfiguredFeature<?, ?>> ctx);

	public abstract void regPlacements(BootstrapContext<PlacedFeature> ctx, HolderGetter<ConfiguredFeature<?, ?>> reg);

	public HeightRangePlacement uniform(int min, int max) {
		return HeightRangePlacement.uniform(VerticalAnchor.absolute(min), VerticalAnchor.absolute(max));
	}

	public List<PlacementModifier> spreadRare(int rarity) {
		return List.of(RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), BiomeFilter.biome());
	}

	public List<PlacementModifier> spreadRare(int rarity, PlacementModifier range) {
		return List.of(RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), range, BiomeFilter.biome());
	}

	public List<PlacementModifier> spread(PlacementModifier count, PlacementModifier range) {
		return List.of(count, InSquarePlacement.spread(), range, BiomeFilter.biome());
	}


	public List<PlacementModifier> rarity(int rarity) {
		return List.of(RarityFilter.onAverageOnceEvery(rarity), BiomeFilter.biome());
	}

	public List<PlacementModifier> spread(int count, PlacementModifier m) {
		return spread(CountPlacement.of(count), m);
	}

	public List<PlacementModifier> spread(int min, int max, PlacementModifier m) {
		return spread(CountPlacement.of(UniformInt.of(min, max)), m);
	}

	public List<PlacementModifier> layer(int count) {
		return List.of(CountOnEveryLayerPlacement.of(count), BiomeFilter.biome());
	}

	public List<PlacementModifier> layer(int min, int max) {
		return List.of(CountOnEveryLayerPlacement.of(UniformInt.of(min, max)), BiomeFilter.biome());
	}

	public FeatureKey uni(String id) {
		return new FeatureKey(this, id);
	}

	public FeatureKey wrap(ResourceKey<ConfiguredFeature<?, ?>> key, String id) {
		return new FeatureKey(this, key, id);
	}

	public ResourceKey<ConfiguredFeature<?, ?>> configured(String id) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, loc(id));
	}

	public ResourceKey<PlacedFeature> place(String id) {
		return ResourceKey.create(Registries.PLACED_FEATURE, loc(id));
	}

	public ResourceLocation loc(String id) {
		return LostLegends.loc(parent.path + "/" + type + "/" + id);
	}

}
