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

	public List<PlacementModifier> orePlace(PlacementModifier count, PlacementModifier range) {
		return List.of(count, InSquarePlacement.spread(), range, BiomeFilter.biome());
	}

	public List<PlacementModifier> orePlace(int count, PlacementModifier m) {
		return orePlace(CountPlacement.of(count), m);
	}

	public List<PlacementModifier> orePlace(int min, int max, PlacementModifier m) {
		return orePlace(CountPlacement.of(UniformInt.of(min, max)), m);
	}

	public FeatureKey uni(String id) {
		return new FeatureKey(this, id);
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
