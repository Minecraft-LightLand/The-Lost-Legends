package dev.xkmc.lostlegends.foundation.module;

import com.tterrag.registrate.providers.DataProviderInitializer;
import net.minecraft.core.registries.Registries;

import java.util.ArrayList;
import java.util.List;

public class LLFeatureReg {

	public final String path;

	List<FeatureGroup> groups = new ArrayList<>();

	public LLFeatureReg(String path) {
		this.path = path;
	}

	public void init(DataProviderInitializer init) {
		init.add(Registries.CONFIGURED_FEATURE, (ctx) -> {
			for (var e : groups)
				e.regFeatures(ctx);
		});

		init.add(Registries.PLACED_FEATURE, (ctx) -> {
			var reg = ctx.lookup(Registries.CONFIGURED_FEATURE);
			for (var e : groups)
				e.regPlacements(ctx, reg);
		});
	}

}
