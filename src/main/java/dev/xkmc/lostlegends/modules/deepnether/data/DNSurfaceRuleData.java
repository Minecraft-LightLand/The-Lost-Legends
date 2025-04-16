
package dev.xkmc.lostlegends.modules.deepnether.data;

import dev.xkmc.lostlegends.foundation.dimension.ClimateBuilder;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public class DNSurfaceRuleData {
	private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
	private static final SurfaceRules.RuleSource BASALT = makeStateRule(Blocks.BASALT);
	private static final SurfaceRules.RuleSource BLACKSTONE = makeStateRule(Blocks.BLACKSTONE);
	private static final SurfaceRules.RuleSource NETHERRACK = makeStateRule(Blocks.NETHERRACK);
	private static final SurfaceRules.RuleSource CRIMSON_WART = makeStateRule(Blocks.NETHER_WART_BLOCK);
	private static final SurfaceRules.RuleSource CRIMSON_NYLIUM = makeStateRule(Blocks.CRIMSON_NYLIUM);

	private static SurfaceRules.RuleSource makeStateRule(Block block) {
		return SurfaceRules.state(block.defaultBlockState());
	}

	public static SurfaceRules.RuleSource nether(ClimateBuilder builder) {
		var deepRack = makeStateRule(DeepNether.BLOCKS.DEEP_NETHERRACK.get());
		var ashBlock = makeStateRule(DeepNether.BLOCKS.ASH_BLOCK.get());
		var ashStone = makeStateRule(DeepNether.BLOCKS.ASH_STONE.get());
		var soil = makeStateRule(DeepNether.BLOCKS.NETHER_SOIL.get());
		var nylium = makeStateRule(DeepNether.BLOCKS.NETHER_NYLIUM.get());
		var soilSand = makeStateRule(DeepNether.BLOCKS.WEEPING_SAND.get());
		var soulSoil = makeStateRule(DeepNether.BLOCKS.DEMENTING_SOIL.get());

		var gold = makeStateRule(Blocks.GOLD_BLOCK);

		SurfaceRules.ConditionSource above30 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(30), 0);
		SurfaceRules.ConditionSource above31 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(31), 0);
		SurfaceRules.ConditionSource selNoise = SurfaceRules.noiseCondition(Noises.NETHER_STATE_SELECTOR, 0.0);
		SurfaceRules.ConditionSource lowSelNoise = SurfaceRules.noiseCondition(Noises.NETHER_STATE_SELECTOR, 0.2);
		SurfaceRules.ConditionSource wartNoise = SurfaceRules.noiseCondition(Noises.NETHER_WART, 0.2);

		SurfaceRules.RuleSource ash = SurfaceRules.ifTrue(above31, SurfaceRules.ifTrue(
				SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.45, 0.58), ashBlock
		));

		builder.conditional(SurfaceRules.verticalGradient("bedrock_floor",
				VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK);
		builder.conditional(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof",
				VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK);
		builder.conditional(SurfaceRules.yBlockCheck(VerticalAnchor.belowTop(5), 0), deepRack);

		var dead = builder.start().vege(e -> e.tip(-0.45f));
		dead.temp(e -> e.get(1)).biome(DNBiomeGen.BIOME_DELTA, 0.1f)
				.addRule(SurfaceRules.UNDER_CEILING, BASALT)
				.addRule(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(selNoise, BASALT), BLACKSTONE);
		dead.temp(e -> e.get(-1)).biome(DNBiomeGen.BIOME_SOUL, 0.1f)
				.addRule(SurfaceRules.UNDER_CEILING, SurfaceRules.ifTrue(selNoise, soilSand), soulSoil)
				.addRule(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(selNoise, soilSand), soulSoil);

		builder.start().vege(e -> e.tip(-0.3f)).temp(e -> e.tip(-0.3f)).biome(DNBiomeGen.BIOME_SOUL_HEART, 0f)
				.addRule(SurfaceRules.UNDER_CEILING, SurfaceRules.ifTrue(selNoise, soilSand), soulSoil)
				.addRule(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(selNoise, soilSand), soulSoil);

		var forest = builder.start().vege(e -> e.tip(0.45f));
		forest.temp(e -> e.get(-1)).biome(DNBiomeGen.BIOME_GOLDEN_FOREST, 0.1f)
				.addRule(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(above31, gold))
				.addRule(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(above30, soil));
		forest.temp(e -> e.get(1)).biome(DNBiomeGen.BIOME_CRIMSON_FOREST, 0.1f)
				.addRule(SurfaceRules.UNDER_CEILING, SurfaceRules.ifTrue(above31, SurfaceRules.ifTrue(wartNoise, CRIMSON_WART)))
				.addRule(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(above31, CRIMSON_NYLIUM))
				.addRule(SurfaceRules.UNDER_FLOOR, NETHERRACK);

		var plains = builder.start().vege(e -> e.get(1));
		plains.temp(e -> e.get(-1)).biome(DNBiomeGen.BIOME_GOLDEN_PLAINS, 0.15f)
				.addRule(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(above31, gold))
				.addRule(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(above30, soil));
		plains.temp(e -> e.get(1)).biome(DNBiomeGen.BIOME_CRIMSON_PLAINS, 0.15f)
				.addRule(SurfaceRules.UNDER_CEILING, SurfaceRules.ifTrue(above31, SurfaceRules.ifTrue(wartNoise, CRIMSON_WART)))
				.addRule(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(above31, SurfaceRules.sequence(SurfaceRules.ifTrue(lowSelNoise, CRIMSON_NYLIUM), nylium)))
				.addRule(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(above30, SurfaceRules.sequence(SurfaceRules.ifTrue(lowSelNoise, NETHERRACK), soil)));

		var waste = builder.start().vege(e -> e.get(-1));
		waste.temp(e -> e.get(-1)).biome(DNBiomeGen.BIOME_ASH, 0.15f)
				.addRule(SurfaceRules.UNDER_CEILING, ashStone)
				.addRule(SurfaceRules.UNDER_FLOOR, ash, ashStone);
		waste.temp(e -> e.get(1)).biome(DNBiomeGen.BIOME_WASTE, 0.15f);

		builder.standalone(deepRack);
		return builder.buildRules();
	}

}
