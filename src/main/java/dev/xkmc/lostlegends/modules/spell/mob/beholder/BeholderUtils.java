package dev.xkmc.lostlegends.modules.spell.mob.beholder;

import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.LinearIterator;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.Dir2NormalModifier;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RandomDirModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.selector.BoxSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.content.particle.engine.CustomParticleInstance;
import dev.xkmc.l2magic.content.particle.engine.DustParticleData;
import dev.xkmc.l2magic.content.particle.engine.ParticleRenderData;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import dev.xkmc.lostlegends.modules.spell.engine.StopMotion;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;

import java.util.List;
import java.util.Map;

public class BeholderUtils {

	public static ConfiguredEngine<?> warn(DataGenContext ctx, int dist, int col) {
		return new LinearIterator(
				DoubleVariable.of("0.5"),
				IntVariable.of("" + dist * 2), false,
				new CustomParticleInstance(
						DoubleVariable.of("0"),
						DoubleVariable.of("0.07"),
						IntVariable.of("20"),
						false,
						SimpleMotion.ZERO,
						new DustParticleData(
								RenderTypePreset.LIT,
								ColorVariable.Static.of(col)
						)
				)
		);
	}

	public static ConfiguredEngine<?> charge(DataGenContext ctx, ParticleRenderData<?> charge, int dur) {
		return new DelayedIterator(IntVariable.of("" + dur), IntVariable.of("1"),
				new CustomParticleInstance(
						DoubleVariable.of("-0.1"),
						DoubleVariable.of("0.05"),
						IntVariable.of((10 + dur) + "-i"),
						false,
						new StopMotion(BooleanVariable.of("TickCount>=9")),
						charge
				).move(
						new Dir2NormalModifier(),
						RotationModifier.of("rand(0,360)"),
						ForwardOffsetModifier.of("1")
				), "i"
		);
	}

	public static ConfiguredEngine<?> spell(
			DataGenContext ctx, int dist, int col, int time,
			Holder<ProjectileConfig> proj, ParticleRenderData<?> charge) {
		return new ListLogic(List.of(
				warn(ctx, dist, col),
				charge(ctx, charge, 10),
				new ListLogic(List.of(
						new SoundInstance(
								SoundEvents.FIRECHARGE_USE,
								DoubleVariable.of("2"),
								DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
						),
						new CustomProjectileShoot(
								DoubleVariable.of("" + (1d * dist / time)), proj,
								IntVariable.of("" + time),
								false, false,
								Map.of()
						)
				)).delay(IntVariable.of("20"))
		));
	}

	public static ConfiguredEngine<?> explode(int col, int count, String speed) {
		return new LoopIterator(
				IntVariable.of("" + count),
				new DustParticleInstance(
						ColorVariable.Static.of(col),
						DoubleVariable.of("0.5"),
						DoubleVariable.of(speed),
						IntVariable.of("rand(18,26)")
				).move(
						new RandomDirModifier()
				), null
		);
	}

	public static ConfiguredEngine<?> affect(String speed, EntityProcessor<?> processor) {
		return new DelayedIterator(IntVariable.of("10"), IntVariable.of("1"),
				new ProcessorEngine(SelectionType.ENEMY_NO_FAMILY,
						new BoxSelector(
								DoubleVariable.of("(t+2)*" + speed),
								DoubleVariable.of("(t+2)*" + speed),
								true
						), List.of(processor)), "t"
		);
	}

}
