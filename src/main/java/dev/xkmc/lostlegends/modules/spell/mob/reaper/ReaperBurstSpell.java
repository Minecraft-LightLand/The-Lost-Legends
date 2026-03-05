package dev.xkmc.lostlegends.modules.spell.mob.reaper;

import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.processor.PropertyProcessor;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import dev.xkmc.l2magic.content.particle.engine.SimpleParticleData;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import dev.xkmc.lostlegends.modules.spell.engine.IgniteBlock;
import dev.xkmc.lostlegends.modules.spell.engine.ModelRenderData;
import dev.xkmc.lostlegends.modules.spell.init.LLSpellGenEntry;
import dev.xkmc.lostlegends.modules.spell.mob.beholder.BeholderUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.List;
import java.util.Map;

public class ReaperBurstSpell extends LLSpellGenEntry {

	public static final ResourceKey<SpellAction> SPELL = spell("reaper_burst_magic");
	public static final DataGenCachedHolder<ProjectileConfig> PROJ = projectile("reaper_burst_projectile");
	public static final ResourceLocation MODEL = LostLegends.loc("spell/reaper_burst_projectile");

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(SPELL.location()), "Reaper Burst Magic");
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				spell(new DataGenContext(ctx)),
				Items.FIRE_CHARGE, 402,
				SpellCastType.INSTANT,
				SpellTriggerType.FACING_FRONT
		).verifyOnBuild(ctx, SPELL);
	}

	@Override
	public void registerProjectile(BootstrapContext<ProjectileConfig> ctx) {
		proj(new DataGenContext(ctx)).verifyOnBuild(ctx, PROJ);
	}

	@Override
	public List<ResourceLocation> additionalModels() {
		return List.of(MODEL);
	}

	@Override
	public void genModel(RegistrateItemModelProvider pvd) {
		pvd.getBuilder(MODEL.getPath())
				.parent(new ModelFile.UncheckedModelFile(LostLegends.loc("custom/beholder_projectile")))
				.texture("all", tex("beholder/flaming_projectile"))
				.renderType("cutout");
	}

	private ProjectileConfig proj(DataGenContext ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.motion(SimpleMotion.ZERO)
				.tick(new SimpleParticleInstance(ParticleTypes.SMALL_FLAME, DoubleVariable.ZERO).move(ForwardOffsetModifier.of("-1")))
				.hit(new EffectProcessor(DeepNether.EFFECTS.FLAME_CURSE, IntVariable.of("200"), IntVariable.of("1"), false, true))
				.hit(new DamageProcessor(ctx.damage(DamageTypes.INDIRECT_MAGIC), DoubleVariable.of("6"), true, true))
				.hit(PropertyProcessor.Type.IGNITE.of("100"))
				.land(new IgniteBlock())
				.renderer(new ModelRenderData(MODEL, DoubleVariable.of("1")))
				.build();
	}

	private static ConfiguredEngine<?> spell(DataGenContext ctx) {
		return new ListLogic(List.of(
				BeholderUtils.warn(ctx, 30, 0xFFFF0000, 30),
				BeholderUtils.charge(ctx, new SimpleParticleData(RenderTypePreset.LIT, ParticleTypes.FLAME), 40),
				new DelayedIterator(IntVariable.of("20"), IntVariable.of("2"),
						new ListLogic(List.of(
								new SoundInstance(
										SoundEvents.FIRECHARGE_USE,
										DoubleVariable.of("1"),
										DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
								),
								new CustomProjectileShoot(
										DoubleVariable.of("0.5"), PROJ,
										IntVariable.of("60"),
										false, false,
										Map.of()
								).move(RotationModifier.of("rand(-6,6)", "rand(-6,6)"))
						)).delay(IntVariable.of("20"))
				)));
	}

}