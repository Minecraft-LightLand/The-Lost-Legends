package dev.xkmc.lostlegends.modules.deepnether.data;

import com.tterrag.registrate.providers.DataProviderInitializer;
import dev.xkmc.lostlegends.foundation.dimension.ClimateBuilder;
import dev.xkmc.lostlegends.foundation.dimension.ParamDiv;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import dev.xkmc.lostlegends.modules.deepnether.util.SoulEffectsHelper;
import dev.xkmc.lostlegends.modules.deepnether.worldgen.aquifer.DNAquifer;
import dev.xkmc.lostlegends.modules.deepnether.worldgen.aquifer.DNChunkGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;

import java.util.List;
import java.util.OptionalLong;

public class DNDimensionGen {

	public static final ResourceKey<DimensionType> DT_DEEP = ResourceKey.create(Registries.DIMENSION_TYPE, loc("deep_nether"));
	public static final ResourceKey<NoiseGeneratorSettings> NGS_DEEP = ResourceKey.create(Registries.NOISE_SETTINGS, loc("deep_nether"));
	public static final ResourceKey<LevelStem> LEVEL_DEEP = ResourceKey.create(Registries.LEVEL_STEM, loc("deep_nether"));
	public static final ResourceKey<Level> DEEP_NETHER = Registries.levelStemToLevel(LEVEL_DEEP);

	public static void init(DataProviderInitializer init) {
		var biomeSet = new ClimateBuilder(
				null, null,
				ParamDiv.polar(),
				ParamDiv.polar()
		);
		DNSurfaceRuleData.nether(biomeSet);

		init.add(Registries.DIMENSION_TYPE, (ctx) -> {
			var spawn = new DimensionType.MonsterSettings(true, false,
					UniformInt.of(0, 7), 0);
			ctx.register(DT_DEEP, new DimensionType(
					OptionalLong.of(18000L),
					false, false, false, false,
					16, false, false,
					0, 256, 256,
					BlockTags.INFINIBURN_NETHER,
					BuiltinDimensionTypes.NETHER_EFFECTS, 0, spawn
			));
		});

		init.add(Registries.NOISE, (ctx) -> {
		});

		init.add(Registries.NOISE_SETTINGS, (ctx) -> {
			var df = ctx.lookup(Registries.DENSITY_FUNCTION);
			var np = ctx.lookup(Registries.NOISE);
			ctx.register(NGS_DEEP, new NoiseGeneratorSettings(
					new NoiseSettings(0, 256, 2, 1),
					DeepNether.BLOCKS.DEEP_NETHERRACK.get().defaultBlockState(), Blocks.LAVA.defaultBlockState(),
					DNNoiseRouterData.nether(df, np, 0.25, 0.125, 256), biomeSet.buildRules(),
					List.of(), 32, false,
					false, false, true
			));
		});

		init.add(Registries.LEVEL_STEM, (ctx) -> {
			var dt = ctx.lookup(Registries.DIMENSION_TYPE);
			var biome = ctx.lookup(Registries.BIOME);
			var noise = ctx.lookup(Registries.NOISE_SETTINGS);

			ctx.register(LEVEL_DEEP, new LevelStem(dt.getOrThrow(DT_DEEP),
					new DNChunkGenerator(MultiNoiseBiomeSource.createFromList(biomeSet.climate(biome)),
							noise.getOrThrow(NGS_DEEP), List.of(
							new DNAquifer.Entry(-1, -0.1, -1, -0.4, 0.05, 0.01,
									SoulEffectsHelper.getFluidBlock(),
									Blocks.CRYING_OBSIDIAN.defaultBlockState(),
									DeepNether.BLOCKS.RAGING_OBSIDIAN.getDefaultState())
					))));
		});

	}

	private static ResourceLocation loc(String id) {
		return LostLegends.loc(id);
	}

}
