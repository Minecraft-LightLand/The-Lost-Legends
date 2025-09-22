package dev.xkmc.lostlegends.modules.spell.mob.beholder;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.LinearIterator;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.Dir2NormalModifier;
import dev.xkmc.l2magic.content.engine.modifier.RandomDirModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.CastAtProcessor;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.selector.BoxSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.content.particle.engine.CustomParticleInstance;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import dev.xkmc.l2magic.content.particle.engine.SimpleParticleData;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import dev.xkmc.lostlegends.modules.spell.init.LLSpellGenEntry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Map;

public class BeholderSpell extends LLSpellGenEntry {

	public static final ResourceKey<SpellAction> SPELL = spell("beholder_magic");
	public static final DataGenCachedHolder<ProjectileConfig> PROJ = projectile("beholder_projectile");

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(SPELL.location()), "Beholder Magic");
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				spell(new DataGenContext(ctx), 20),
				Items.FIRE_CHARGE, 400,
				SpellCastType.INSTANT,
				SpellTriggerType.FACING_FRONT
		).verifyOnBuild(ctx, SPELL);
	}

	@Override
	public void registerProjectile(BootstrapContext<ProjectileConfig> ctx) {
		circularProjectile(new DataGenContext(ctx))
				.verifyOnBuild(ctx, PROJ);
	}

	private ProjectileConfig circularProjectile(DataGenContext ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.motion(SimpleMotion.ZERO)
				.tick(new SimpleParticleInstance(ParticleTypes.END_ROD, DoubleVariable.ZERO))
				.land(land(ctx))
				.hit(new DamageProcessor(ctx.damage(DamageTypes.INDIRECT_MAGIC), DoubleVariable.of("6"), true, true))
				.hit(new EffectProcessor(MobEffects.BLINDNESS, IntVariable.of("60"), IntVariable.of("0"), false, true))
				.hit(new CastAtProcessor(CastAtProcessor.PosType.ORIGINAL, CastAtProcessor.DirType.ORIGINAL, land(ctx)))
				.build();
	}

	private ConfiguredEngine<?> land(DataGenContext ctx) {
		return new ListLogic(List.of(
				new LoopIterator(
						IntVariable.of("200"),
						new DustParticleInstance(
								ColorVariable.Static.of(-1),
								DoubleVariable.of("0.5"),
								DoubleVariable.of("0.2"),
								IntVariable.of("20")
						).move(
								new RandomDirModifier()
						), null
				),
				new SoundInstance(
						SoundEvents.DRAGON_FIREBALL_EXPLODE,
						DoubleVariable.of("2"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new DelayedIterator(IntVariable.of("10"), IntVariable.of("1"),
						new ProcessorEngine(SelectionType.ENEMY_NO_FAMILY,
								new BoxSelector(
										DoubleVariable.of("(t+2)*0.2"),
										DoubleVariable.of("(t+2)*0.2"),
										true
								), List.of(
								new DamageProcessor(
										ctx.damage(DamageTypes.EXPLOSION),
										DoubleVariable.of("6-t*0.5"),
										true, true)
						)), "t"
				)
		));
	}

	private static ConfiguredEngine<?> spell(DataGenContext ctx, int dist) {
		return new ListLogic(List.of(
				new LinearIterator(
						DoubleVariable.of("0.5"),
						IntVariable.of("" + dist * 2), false,
						new DustParticleInstance(
								ColorVariable.Static.of(-1),
								DoubleVariable.of("0.5"),
								DoubleVariable.of("0"),
								IntVariable.of("20")
						)
				),
				new DelayedIterator(IntVariable.of("10"), IntVariable.of("1"),
						new CustomParticleInstance(
								DoubleVariable.of("-0.1"),
								DoubleVariable.of("0.05"),
								IntVariable.of("10"),
								false,
								SimpleMotion.ZERO,
								new SimpleParticleData(
										RenderTypePreset.LIT,
										ParticleTypes.SOUL_FIRE_FLAME
								)
						).move(
								new Dir2NormalModifier(),
								RotationModifier.of("rand(0,360)")
						)
				),
				new ListLogic(List.of(
						new SoundInstance(
								SoundEvents.FIRECHARGE_USE,
								DoubleVariable.of("2"),
								DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
						),
						new CustomProjectileShoot(
								DoubleVariable.of("1"),
								PROJ,
								IntVariable.of("20"),
								false, false,
								Map.of()
						)
				))
		));
	}

}