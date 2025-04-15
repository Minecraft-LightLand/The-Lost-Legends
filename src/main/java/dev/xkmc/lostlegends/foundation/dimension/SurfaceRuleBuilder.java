package dev.xkmc.lostlegends.foundation.dimension;

import net.minecraft.world.level.levelgen.SurfaceRules;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SurfaceRuleBuilder {

	@Nullable
	private final SurfaceRules.ConditionSource condition;
	@Nullable
	private final SurfaceHolder self;
	private final List<SurfaceRules.RuleSource> list;

	SurfaceRuleBuilder(@Nullable SurfaceRules.ConditionSource condition, @Nullable SurfaceHolder self) {
		this.condition = condition;
		this.self = self;
		this.list = new ArrayList<>();
	}

	public void add(SurfaceRules.RuleSource rule) {
		list.add(rule);
	}

	public SurfaceRules.RuleSource buildRules() {
		var seq = buildRulesImpl();
		return condition == null ? seq : SurfaceRules.ifTrue(condition, seq);
	}

	private SurfaceRules.RuleSource buildRulesImpl() {
		int selfSize = self == null ? 0 : self.surfaceList.size();
		int total = selfSize + list.size();
		if (total == 0) throw new IllegalStateException("No rules to build");
		if (total == 1) {
			if (selfSize > 0)
				return self.surfaceList.getFirst().buildRules();
			else return list.getFirst();
		}
		List<SurfaceRules.RuleSource> list = new ArrayList<>();
		if (self != null) {
			for (var e : self.surfaceList) {
				list.add(e.buildRules());
			}
		}
		list.addAll(this.list);
		return SurfaceRules.sequence(list.toArray(SurfaceRules.RuleSource[]::new));
	}

}
