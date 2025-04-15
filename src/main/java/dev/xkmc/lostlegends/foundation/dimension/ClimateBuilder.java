package dev.xkmc.lostlegends.foundation.dimension;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ClimateBuilder extends SurfaceHolder {

	@Nullable
	protected final ParamDiv depth, cont, temp, vege;

	final List<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> climateList = new ArrayList<>();

	@ParametersAreNullableByDefault
	public ClimateBuilder(ParamDiv depth, ParamDiv cont, ParamDiv temp, ParamDiv vege) {
		super(new ArrayList<>());
		this.depth = depth;
		this.cont = cont;
		this.temp = temp;
		this.vege = vege;
	}

	public RangeBuilder<ClimateBuilder> start() {
		return new RangeBuilder<>(this,
				depth == null ? null : depth.all(),
				cont == null ? null : cont.all(),
				temp == null ? null : temp.all(),
				vege == null ? null : vege.all());
	}

	public Climate.ParameterList<Holder<Biome>> climate(HolderGetter<Biome> reg) {
		List<Pair<Climate.ParameterPoint, Holder<Biome>>> list = new ArrayList<>();
		for (var e : climateList) {
			list.add(Pair.of(e.getFirst(), reg.getOrThrow(e.getSecond())));
		}
		return new Climate.ParameterList<>(list);
	}


	public SurfaceHolder standalone(SurfaceRules.RuleSource rule) {
		var ins = new SurfaceRuleBuilder(null, null);
		ins.add(rule);
		surfaceList.add(ins);
		return this;
	}

	public SurfaceHolder conditional(SurfaceRules.ConditionSource cond, SurfaceRules.RuleSource... rules) {
		if (rules.length == 0) return this;
		if (rules.length == 1) {
			return standalone(SurfaceRules.ifTrue(cond, rules[0]));
		}
		return standalone(SurfaceRules.ifTrue(cond, SurfaceRules.sequence(rules)));
	}

	public final class RangeBuilder<P extends SurfaceHolder> extends SurfaceHolder {

		private final P parent;
		private final Climate.Parameter depthParam, contParam, tempParam, vegeParam;

		@Nullable
		private SurfaceRuleBuilder rules = null;

		@ParametersAreNullableByDefault
		public RangeBuilder(@Nonnull P parent, Climate.Parameter depthParam, Climate.Parameter contParam, Climate.Parameter tempParam, Climate.Parameter vegeParam) {
			super(parent.surfaceList);
			this.parent = parent;
			this.depthParam = depthParam;
			this.contParam = contParam;
			this.tempParam = tempParam;
			this.vegeParam = vegeParam;
		}

		public RangeBuilder<RangeBuilder<P>> depth(Function<ParamDiv, Climate.Parameter> func) {
			return new RangeBuilder<>(this, func.apply(depth), contParam, tempParam, vegeParam);
		}

		public RangeBuilder<RangeBuilder<P>> cont(Function<ParamDiv, Climate.Parameter> func) {
			return new RangeBuilder<>(this, depthParam, func.apply(cont), tempParam, vegeParam);
		}

		public RangeBuilder<RangeBuilder<P>> temp(Function<ParamDiv, Climate.Parameter> func) {
			return new RangeBuilder<>(this, depthParam, contParam, func.apply(temp), vegeParam);
		}

		public RangeBuilder<RangeBuilder<P>> vege(Function<ParamDiv, Climate.Parameter> func) {
			return new RangeBuilder<>(this, depthParam, contParam, tempParam, func.apply(vege));
		}

		public BiomeBuilder<P> biome(ResourceKey<Biome> biome, float offset) {
			climateList.add(Pair.of(Climate.parameters(
					tempParam == null ? point(0) : tempParam,
					vegeParam == null ? point(0) : vegeParam,
					contParam == null ? point(0) : contParam,
					point(0),
					depthParam == null ? point(0) : depthParam,
					point(0),
					offset
			), biome));
			return new BiomeBuilder<>(this, biome);
		}

		public RangeBuilder<P> startRule(SurfaceRules.ConditionSource condition) {
			this.surfaceList = new ArrayList<>();
			rules = new SurfaceRuleBuilder(condition, this);
			parent.surfaceList.add(rules);
			return this;
		}

		public RangeBuilder<P> endRule() {
			rules = null;
			return this;
		}

		public RangeBuilder<P> addRule(SurfaceRules.RuleSource rule) {
			if (rules == null) throw new IllegalStateException("No condition defined");
			rules.add(rule);
			return this;
		}

		public RangeBuilder<P> addRule(SurfaceRules.ConditionSource cond, SurfaceRules.RuleSource... rules) {
			if (rules.length == 0) return this;
			if (rules.length == 1) {
				return addRule(SurfaceRules.ifTrue(cond, rules[0]));
			}
			return addRule(SurfaceRules.ifTrue(cond, SurfaceRules.sequence(rules)));
		}

		public P end() {
			return parent;
		}

	}

	public static Climate.Parameter point(float value) {
		return ParamDiv.span(value, value);
	}

}

