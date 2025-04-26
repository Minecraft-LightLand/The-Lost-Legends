package dev.xkmc.lostlegends.modules.deepnether.data;

import com.tterrag.registrate.providers.DataProviderInitializer;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import javax.annotation.Nullable;

import static net.minecraft.world.level.levelgen.GenerationStep.Decoration.UNDERGROUND_STRUCTURES;

public class DNBiomeGen {

	public static final ResourceKey<Biome> BIOME_DELTA = biome("delta");
	public static final ResourceKey<Biome> BIOME_SOUL = biome("soul");
	public static final ResourceKey<Biome> BIOME_SOUL_HEART = biome("soul_heart");
	public static final ResourceKey<Biome> BIOME_ASH = biome("ash");
	public static final ResourceKey<Biome> BIOME_WASTE = biome("waste");
	public static final ResourceKey<Biome> BIOME_GOLDEN_PLAINS = biome("golden_plains");
	public static final ResourceKey<Biome> BIOME_GOLDEN_FOREST = biome("golden_forest");
	public static final ResourceKey<Biome> BIOME_GOLDEN_HEART = biome("golden_heart");
	public static final ResourceKey<Biome> BIOME_CRIMSON_PLAINS = biome("crimson_plains");
	public static final ResourceKey<Biome> BIOME_CRIMSON_FOREST = biome("crimson_forest");

	public static final ResourceKey<ConfiguredWorldCarver<?>> DEEP_CARVER = carver("deep_nether_carver");

	public static void init(DataProviderInitializer init) {

		init.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ctx -> {
			DNFeatures.INS.struct.netherPortal.asModifier(ctx, BiomeTags.IS_NETHER, UNDERGROUND_STRUCTURES);
		});

		init.add(Registries.CONFIGURED_CARVER, (ctx) -> {
			HolderGetter<Block> blocks = ctx.lookup(Registries.BLOCK);
			ctx.register(DEEP_CARVER, DeepNether.WG.DEEP_CARVER.get().configured(
					new CaveCarverConfiguration(
							0.2F,
							UniformHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.belowTop(1)),
							ConstantFloat.of(0.5F),
							VerticalAnchor.aboveBottom(10),
							blocks.getOrThrow(BlockTags.NETHER_CARVER_REPLACEABLES),
							ConstantFloat.of(1.0F),
							ConstantFloat.of(1.0F),
							ConstantFloat.of(-0.7F)
					)
			));
		});


		init.add(Registries.BIOME, (ctx) -> {
			var pf = ctx.lookup(Registries.PLACED_FEATURE);
			var wc = ctx.lookup(Registries.CONFIGURED_CARVER);

			ctx.register(BIOME_DELTA, biome(6840176,
					new MobSpawnSettings.Builder(),
					new DNBiomeDecoBuilder(pf, wc)
							.delta().pillar()
							.blackstoneBolb().magmaBolb()
							.ores().mushrooms()
							.build(),
					Musics.createGameMusic(SoundEvents.MUSIC_BIOME_BASALT_DELTAS)
			));

			ctx.register(BIOME_SOUL, biome(1787717,
					new MobSpawnSettings.Builder(),
					new DNBiomeDecoBuilder(pf, wc)
							.firePatch().soulland()
							.pillar().darkstonePile()
							.blackstoneBolb().warpedBlob().ecto()
							.ores().mushrooms().crimsonRoot()
							.build(),
					Musics.createGameMusic(SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY)
			));

			ctx.register(BIOME_SOUL_HEART, biome(1787717,
					new MobSpawnSettings.Builder(),
					new DNBiomeDecoBuilder(pf, wc)
							.firePatch().soulland()
							.pillar().darkstonePile()
							.blackstoneBolb().warpedBlob().ecto()
							.ores().mushrooms().crimsonRoot()
							.build(),
					Musics.createGameMusic(SoundEvents.MUSIC_BIOME_SOUL_SAND_VALLEY)
			));

			ctx.register(BIOME_ASH, biome(6840176,
					new MobSpawnSettings.Builder(),
					new DNBiomeDecoBuilder(pf, wc)
							.lavaSprings().firePatch()
							.pillar().darkstonePile()
							.blackstoneBolb().magmaBolb()
							.ores().mushrooms().ashBlossom()
							.build(),
					Musics.createGameMusic(SoundEvents.MUSIC_BIOME_BASALT_DELTAS)
			));

			ctx.register(BIOME_WASTE, biome(3344392,
					new MobSpawnSettings.Builder(),
					new DNBiomeDecoBuilder(pf, wc)
							.lavaSprings().firePatch()
							.darkstonePile()
							.blackstoneBolb().magmaBolb().amber()
							.ores().mushrooms()
							.build(),
					Musics.createGameMusic(SoundEvents.MUSIC_BIOME_NETHER_WASTES)
			));

			ctx.register(BIOME_GOLDEN_PLAINS, biome(1705242,
					new MobSpawnSettings.Builder(),
					new DNBiomeDecoBuilder(pf, wc)
							.lavaSprings().firePatch()
							.blackstoneBolb().magmaBolb()
							.ores().mushrooms()
							.build(),
					Musics.createGameMusic(SoundEvents.MUSIC_BIOME_WARPED_FOREST)
			));

			ctx.register(BIOME_GOLDEN_FOREST, biome(1705242,
					new MobSpawnSettings.Builder(),
					new DNBiomeDecoBuilder(pf, wc)
							.lavaSprings().firePatch()
							.blackstoneBolb().magmaBolb()
							.ores().mushrooms()
							.build(),
					Musics.createGameMusic(SoundEvents.MUSIC_BIOME_WARPED_FOREST)
			));

			ctx.register(BIOME_GOLDEN_HEART, biome(1705242,
					new MobSpawnSettings.Builder(),
					new DNBiomeDecoBuilder(pf, wc)
							.lavaSprings().firePatch()
							.blackstoneBolb().magmaBolb()
							.ores().mushrooms()
							.build(),
					Musics.createGameMusic(SoundEvents.MUSIC_BIOME_WARPED_FOREST)
			));

			ctx.register(BIOME_CRIMSON_PLAINS, biome(3343107,
					new MobSpawnSettings.Builder(),
					new DNBiomeDecoBuilder(pf, wc)
							.lavaSprings().firePatch()
							.blackstoneBolb().magmaBolb()
							.ores().mushrooms().crimsonPlains()
							.build(),
					Musics.createGameMusic(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST)
			));

			ctx.register(BIOME_CRIMSON_FOREST, biome(3343107,
					new MobSpawnSettings.Builder(),
					new DNBiomeDecoBuilder(pf, wc)
							.lavaSprings().firePatch()
							.blackstoneBolb().magmaBolb()
							.ores().mushrooms().crimsonForest()
							.build(),
					Musics.createGameMusic(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST)
			));


		});
	}

	private static ResourceKey<Biome> biome(String id) {
		return ResourceKey.create(Registries.BIOME, loc(id));
	}

	private static ResourceKey<ConfiguredWorldCarver<?>> carver(String id) {
		return ResourceKey.create(Registries.CONFIGURED_CARVER, loc(id));
	}

	private static Biome biome(int fogColor,
							   MobSpawnSettings.Builder spawns,
							   BiomeGenerationSettings.PlainBuilder gen,
							   @Nullable Music bgm
	) {
		return biome(false, 2, 0, fogColor, spawns, gen, bgm);
	}

	private static Biome biome(
			boolean hasPercipitation, float temperature, float downfall, int fogColor,
			MobSpawnSettings.Builder spawns,
			BiomeGenerationSettings.PlainBuilder gen,
			@Nullable Music bgm
	) {
		return biome(hasPercipitation, temperature, downfall, 4159204, 329011, fogColor, null, null, spawns, gen, bgm);
	}

	private static Biome biome(
			boolean hasPrecipitation, float temperature, float downfall,
			int waterColor, int waterFogColor, int fogColor,
			@Nullable Integer grassCol, @Nullable Integer foliageCol,
			MobSpawnSettings.Builder spawns,
			BiomeGenerationSettings.PlainBuilder gen,
			@Nullable Music bgm
	) {
		BiomeSpecialEffects.Builder biomespecialeffects$builder = new BiomeSpecialEffects.Builder()
				.waterColor(waterColor)
				.waterFogColor(waterFogColor)
				.fogColor(fogColor)
				.skyColor(calculateSkyColor(temperature))
				.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
				.backgroundMusic(bgm);
		if (grassCol != null) {
			biomespecialeffects$builder.grassColorOverride(grassCol);
		}
		if (foliageCol != null) {
			biomespecialeffects$builder.foliageColorOverride(foliageCol);
		}
		return new Biome.BiomeBuilder()
				.hasPrecipitation(hasPrecipitation)
				.temperature(temperature)
				.downfall(downfall)
				.specialEffects(biomespecialeffects$builder.build())
				.mobSpawnSettings(spawns.build())
				.generationSettings(gen.build())
				.build();
	}

	protected static int calculateSkyColor(float temperature) {
		float f = Mth.clamp(temperature / 3.0F, -1.0F, 1.0F);
		return Mth.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
	}

	private static ResourceLocation loc(String id) {
		return LostLegends.loc(id);
	}

}
