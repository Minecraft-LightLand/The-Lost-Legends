package dev.xkmc.lostlegends.modules.spell.mob.beholder;

import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RandomDirModifier;
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
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import dev.xkmc.l2magic.content.particle.engine.SimpleParticleData;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.spell.engine.ModelRenderData;
import dev.xkmc.lostlegends.modules.spell.init.LLSpellGenEntry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.List;

public class FlameBeholderSpell extends LLSpellGenEntry {

	public static final ResourceKey<SpellAction> SPELL = spell("flame_beholder_magic");
	public static final DataGenCachedHolder<ProjectileConfig> PROJ = projectile("flame_beholder_projectile");
	public static final ResourceLocation MODEL = LostLegends.loc("spell/flame_beholder_projectile");

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(SPELL.location()), "Flame Beholder Magic");
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				spell(new DataGenContext(ctx)),
				Items.FIRE_CHARGE, 400,
				SpellCastType.INSTANT,
				SpellTriggerType.FACING_FRONT
		).verifyOnBuild(ctx, SPELL);
	}

	@Override
	public void registerProjectile(BootstrapContext<ProjectileConfig> ctx) {
		proj(new DataGenContext(ctx))
				.verifyOnBuild(ctx, PROJ);
	}

	private ProjectileConfig proj(DataGenContext ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.motion(SimpleMotion.ZERO)
				.tick(new SimpleParticleInstance(ParticleTypes.END_ROD, DoubleVariable.ZERO).move(ForwardOffsetModifier.of("-1")))
				.land(land(ctx))
				.hit(new DamageProcessor(ctx.damage(DamageTypes.INDIRECT_MAGIC), DoubleVariable.of("6"), true, true))
				.hit(new EffectProcessor(MobEffects.BLINDNESS, IntVariable.of("60"), IntVariable.of("0"), false, true))
				.hit(new CastAtProcessor(CastAtProcessor.PosType.ORIGINAL, CastAtProcessor.DirType.ORIGINAL, land(ctx)))
				.renderer(new ModelRenderData(MODEL, DoubleVariable.of("1")))
				.build();
	}

	@Override
	public List<ResourceLocation> additionalModels() {
		return List.of(MODEL);
	}

	@Override
	public void genModel(RegistrateItemModelProvider pvd) {
		pvd.getBuilder(MODEL.getPath())
				.parent(new ModelFile.UncheckedModelFile(LostLegends.loc("custom/beholder_projectile")))
				.texture("all", tex("beholder/projectile"))
				.renderType("cutout");
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

	private static ConfiguredEngine<?> spell(DataGenContext ctx) {
		return BeholderUtils.spell(ctx, 20, -1, 20, PROJ,
				new SimpleParticleData(RenderTypePreset.LIT, ParticleTypes.SOUL_FIRE_FLAME));
	}

}