package dev.xkmc.lostlegends.foundation.dimension;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.ArrayList;

public class BiomeBuilder<P extends SurfaceHolder> extends SurfaceHolder {

	private final ClimateBuilder.RangeBuilder<P> parent;
	private final ResourceKey<Biome> biome;

	private SurfaceRuleBuilder rules;

	public BiomeBuilder(ClimateBuilder.RangeBuilder<P> parent, ResourceKey<Biome> biome) {
		super(new ArrayList<>());
		this.parent = parent;
		this.biome = biome;
	}

	public BiomeBuilder<P> addRule(SurfaceRules.RuleSource rule) {
		if (rules == null) {
			rules = new SurfaceRuleBuilder(SurfaceRules.isBiome(biome), this);
			parent.surfaceList.add(rules);
		}
		rules.add(rule);
		return this;
	}

	public BiomeBuilder<P> addRule(SurfaceRules.ConditionSource cond, SurfaceRules.RuleSource... rules) {
		if (rules.length == 0) return this;
		if (rules.length == 1) {
			return addRule(SurfaceRules.ifTrue(cond, rules[0]));
		}
		return addRule(SurfaceRules.ifTrue(cond, SurfaceRules.sequence(rules)));
	}

	public ClimateBuilder.RangeBuilder<P> end() {
		return parent;
	}

}
