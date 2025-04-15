
package dev.xkmc.lostlegends.modules.deepnether.worldgen;

import dev.xkmc.lostlegends.foundation.dimension.ClimateBuilder;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public class DNSurfaceRuleData {
	private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
	private static final SurfaceRules.RuleSource GRAVEL = makeStateRule(Blocks.GRAVEL);
	private static final SurfaceRules.RuleSource LAVA = makeStateRule(Blocks.LAVA);
	private static final SurfaceRules.RuleSource NETHERRACK = makeStateRule(Blocks.NETHERRACK);
	private static final SurfaceRules.RuleSource SOUL_SAND = makeStateRule(Blocks.SOUL_SAND);
	private static final SurfaceRules.RuleSource SOUL_SOIL = makeStateRule(Blocks.SOUL_SOIL);
	private static final SurfaceRules.RuleSource BASALT = makeStateRule(Blocks.BASALT);
	private static final SurfaceRules.RuleSource BLACKSTONE = makeStateRule(Blocks.BLACKSTONE);
	private static final SurfaceRules.RuleSource WARPED_WART_BLOCK = makeStateRule(Blocks.WARPED_WART_BLOCK);
	private static final SurfaceRules.RuleSource WARPED_NYLIUM = makeStateRule(Blocks.WARPED_NYLIUM);
	private static final SurfaceRules.RuleSource NETHER_WART_BLOCK = makeStateRule(Blocks.NETHER_WART_BLOCK);
	private static final SurfaceRules.RuleSource CRIMSON_NYLIUM = makeStateRule(Blocks.CRIMSON_NYLIUM);

	private static SurfaceRules.RuleSource makeStateRule(Block block) {
		return SurfaceRules.state(block.defaultBlockState());
	}

	public static SurfaceRules.RuleSource nether(ClimateBuilder builder) {
		SurfaceRules.RuleSource ashBlock = makeStateRule(DeepNether.BLOCKS.ASH_BLOCK.get());

		SurfaceRules.ConditionSource above30 = SurfaceRules.yStartCheck(VerticalAnchor.absolute(30), 0);
		SurfaceRules.ConditionSource above31 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(31), 0);
		SurfaceRules.ConditionSource above32 = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(32), 0);
		SurfaceRules.ConditionSource below35 = SurfaceRules.not(SurfaceRules.yStartCheck(VerticalAnchor.absolute(35), 0));
		SurfaceRules.ConditionSource hole = SurfaceRules.hole();
		SurfaceRules.ConditionSource soulNoise = SurfaceRules.noiseCondition(Noises.SOUL_SAND_LAYER, -0.012);
		SurfaceRules.ConditionSource gravelNoise = SurfaceRules.noiseCondition(Noises.GRAVEL_LAYER, -0.012);
		SurfaceRules.ConditionSource patchNoise = SurfaceRules.noiseCondition(Noises.PATCH, -0.012);
		SurfaceRules.ConditionSource rackNoise = SurfaceRules.noiseCondition(Noises.NETHERRACK, 0.54);
		SurfaceRules.ConditionSource wartNoise = SurfaceRules.noiseCondition(Noises.NETHER_WART, 1.17);
		SurfaceRules.ConditionSource selNoise = SurfaceRules.noiseCondition(Noises.NETHER_STATE_SELECTOR, 0.0);
		SurfaceRules.RuleSource gravelLayer = SurfaceRules.ifTrue(
				patchNoise, SurfaceRules.ifTrue(above30, SurfaceRules.ifTrue(below35, GRAVEL))
		);

		SurfaceRules.RuleSource ash = SurfaceRules.ifTrue(
				SurfaceRules.noiseCondition(Noises.POWDER_SNOW, 0.45, 0.58), ashBlock
		);


		// bedrock floor
		builder.conditional(SurfaceRules.verticalGradient("bedrock_floor",
				VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK);
		// bedrock roof
		builder.conditional(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof",
				VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK);
		// fill netherrack ceiling for 5 blocks, covering bedrock roof
		builder.conditional(SurfaceRules.yBlockCheck(VerticalAnchor.belowTop(5), 0), NETHERRACK);
		
		var root = builder.start();
		root.temp(e -> e.get(1)).vege(e -> e.get(0)).biome(Biomes.BASALT_DELTAS, 0.1f)
				// basalt ceiling
				.addRule(SurfaceRules.UNDER_CEILING, BASALT)
				// gravel / basalt / blackstone floor
				.addRule(SurfaceRules.UNDER_FLOOR, gravelLayer, SurfaceRules.ifTrue(selNoise, BASALT), BLACKSTONE);
		root.temp(e -> e.get(0)).vege(e -> e.get(-1)).biome(Biomes.SOUL_SAND_VALLEY, 0f)
				// soul sand / soul soil ceiling
				.addRule(SurfaceRules.UNDER_CEILING, SurfaceRules.ifTrue(selNoise, SOUL_SAND), SOUL_SOIL)
				// gravel / soul sand / soul soil floor
				.addRule(SurfaceRules.UNDER_FLOOR, gravelLayer, SurfaceRules.ifTrue(selNoise, SOUL_SAND), SOUL_SOIL);
		var r0 = builder.start();
		r0.startRule(SurfaceRules.ON_FLOOR);
		// fill holes with lava <y32
		r0.addRule(SurfaceRules.not(above32), SurfaceRules.ifTrue(hole, LAVA));
		r0.temp(e -> e.get(0)).vege(e -> e.get(1)).biome(Biomes.WARPED_FOREST, 0.375f)
				// rack noise or <y31 -> rack, wart noise -> wart, otherwise nylium
				.addRule(SurfaceRules.not(rackNoise), SurfaceRules.ifTrue(above31,
						SurfaceRules.sequence(SurfaceRules.ifTrue(wartNoise, WARPED_WART_BLOCK), WARPED_NYLIUM)));
		r0.temp(e -> e.get(-1)).vege(e -> e.get(0)).biome(Biomes.CRIMSON_FOREST, 0f)
				// rack noise or <y31 -> rack, wart noise -> wart, otherwise nylium
				.addRule(SurfaceRules.not(rackNoise), SurfaceRules.ifTrue(above31,
						SurfaceRules.sequence(SurfaceRules.ifTrue(wartNoise, NETHER_WART_BLOCK), CRIMSON_NYLIUM)));

		root.temp(e -> e.get(0)).vege(e -> e.get(0)).biome(Biomes.NETHER_WASTES, 0f)
				// y30-35: fill soul sand with noise
				.addRule(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(soulNoise, SurfaceRules.sequence(
						SurfaceRules.ifTrue(SurfaceRules.not(hole), SurfaceRules.ifTrue(above30, SurfaceRules.ifTrue(below35, SOUL_SAND))),
						NETHERRACK
				)))
				// y31-35: fill gravel with noise
				// exempt for holes
				.addRule(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(above31, SurfaceRules.ifTrue(below35, SurfaceRules.ifTrue(gravelNoise,
						SurfaceRules.sequence(
								SurfaceRules.ifTrue(above32, GRAVEL),
								SurfaceRules.ifTrue(SurfaceRules.not(hole), GRAVEL))))));
		// fill everything else with netherrack
		builder.standalone(NETHERRACK);
		return root.buildRules();
	}

}
