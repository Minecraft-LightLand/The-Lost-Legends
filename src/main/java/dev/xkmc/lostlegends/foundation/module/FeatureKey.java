package dev.xkmc.lostlegends.foundation.module;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class FeatureKey {

	private final FeatureGroup group;
	private final String id;
	public final ResourceKey<ConfiguredFeature<?, ?>> cf;
	public final ResourceKey<PlacedFeature> pf;

	FeatureKey(FeatureGroup group, String id) {
		this.group = group;
		this.id = id;
		cf = group.configured(id);
		pf = group.place(id);
	}

	private FeatureKey(FeatureGroup group, ResourceKey<ConfiguredFeature<?, ?>> cf, String id) {
		this.group = group;
		this.cf = cf;
		this.id = id;
		pf = group.place(id);
	}

	public void place(BootstrapContext<PlacedFeature> ctx, HolderGetter<ConfiguredFeature<?, ?>> reg, List<PlacementModifier> mods) {
		PlacementUtils.register(ctx, pf, reg.getOrThrow(cf), mods);
	}

	public void place(BootstrapContext<PlacedFeature> ctx, HolderGetter<ConfiguredFeature<?, ?>> reg, PlacementModifier... mods) {
		PlacementUtils.register(ctx, pf, reg.getOrThrow(cf), List.of(mods));
	}

	public void addTo(BiomeGenerationSettings.Builder builder, GenerationStep.Decoration decoration) {
		builder.addFeature(decoration, pf);
	}

	public FeatureKey variant(String suffix) {
		return new FeatureKey(group, cf, id + suffix);
	}

}
