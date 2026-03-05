package dev.xkmc.lostlegends.modules.spell.mob.reaper;

import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetPosModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.CastAtProcessor;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
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
import dev.xkmc.lostlegends.modules.spell.engine.ModelRenderData;
import dev.xkmc.lostlegends.modules.spell.init.LLSpellGenEntry;
import dev.xkmc.lostlegends.modules.spell.mob.beholder.BeholderUtils;
import net.minecraft.core.Holder;
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

public class ReaperBombSpell extends LLSpellGenEntry {

	public static final ResourceKey<SpellAction> SPELL = spell("reaper_bomb_magic");
	public static final DataGenCachedHolder<ProjectileConfig> PROJ = projectile("reaper_bomb_projectile");
	public static final ResourceLocation MODEL = LostLegends.loc("spell/reaper_bomb_projectile");

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(SPELL.location()), "Reaper Bomb Magic");
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				spell(new DataGenContext(ctx)),
				Items.GUNPOWDER, 400,
				SpellCastType.INSTANT,
				SpellTriggerType.TARGET_POS
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
				.texture("all", tex("beholder/projectile"))
				.renderType("cutout");
	}

	private static ConfiguredEngine<?> spell(DataGenContext ctx) {
		return new ListLogic(List.of(
				BeholderUtils.charge(ctx, new SimpleParticleData(RenderTypePreset.LIT, ParticleTypes.FLAME), 20, 3, 2, 10, 0.15),
				new ListLogic(List.of(
						shoot(ctx, 40, PROJ),
						new DelayedIterator(IntVariable.of("5"), IntVariable.of("2"),
								shoot(ctx, 40, PROJ).move(RotationModifier.of("rand(-20,20)", "rand(-20,20)")),
								null)
				)).delay(IntVariable.of("20"))
		)).move(
				SetPosModifier.of("CasterX", "CasterY+1", "CasterZ"),
				SetDirectionModifier.of("vx*x/x0", "vy", "vx*z/x0"),
				ForwardOffsetModifier.of("0.5")
		).withVariables(
				"v", "sqrt(vx*vx+vy*vy)"
		).withVariables(
				"x0", "sqrt(x*x+z*z)",
				"vx", "x0/t",
				"vy", "y/t+g*t/2"
		).withVariables(
				"x", "PosX - CasterX",
				"y", "PosY - CasterY",
				"z", "PosZ - CasterZ"
		).withVariables("t", "20", "g", "0.03");
	}

	public static ConfiguredEngine<?> shoot(DataGenContext ctx, int time, Holder<ProjectileConfig> proj) {

		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.FIRECHARGE_USE,
						DoubleVariable.of("2"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new CustomProjectileShoot(
						DoubleVariable.of("v"), proj,
						IntVariable.of("" + time),
						false, false,
						Map.of()
				)
		));
	}

	private ProjectileConfig proj(DataGenContext ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.motion(new SimpleMotion(DoubleVariable.of("0"), DoubleVariable.of("0.03")))
				.tick(new SimpleParticleInstance(ParticleTypes.END_ROD, DoubleVariable.ZERO).move(ForwardOffsetModifier.of("-1")))
				.land(land(ctx))
				.hit(new DamageProcessor(ctx.damage(DamageTypes.INDIRECT_MAGIC), DoubleVariable.of("6"), true, true))
				.hit(new CastAtProcessor(CastAtProcessor.PosType.ORIGINAL, CastAtProcessor.DirType.ORIGINAL, land(ctx)))
				.renderer(new ModelRenderData(MODEL, DoubleVariable.of("1")))
				.build();
	}

	private ConfiguredEngine<?> land(DataGenContext ctx) {
		return new ListLogic(List.of(
				BeholderUtils.explode(-1, 200, "0.2"),
				new SoundInstance(
						SoundEvents.DRAGON_FIREBALL_EXPLODE,
						DoubleVariable.of("2"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				BeholderUtils.affect("0.2",
						new DamageProcessor(
								ctx.damage(DamageTypes.EXPLOSION),
								DoubleVariable.of("6-t*0.5"),
								true, true)
				)
		));
	}


}