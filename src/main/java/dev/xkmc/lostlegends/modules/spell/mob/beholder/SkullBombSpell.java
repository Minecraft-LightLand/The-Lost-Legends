package dev.xkmc.lostlegends.modules.spell.mob.beholder;

import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.CastAtProcessor;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
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
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import dev.xkmc.l2magic.content.particle.engine.SimpleParticleData;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.spell.engine.ModelRenderData;
import dev.xkmc.lostlegends.modules.spell.engine.OrientedCrossRenderData;
import dev.xkmc.lostlegends.modules.spell.init.LLSpellGenEntry;
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

public class SkullBombSpell extends LLSpellGenEntry {

	public static final ResourceKey<SpellAction> SPELL = spell("beholder/skull_bomb");
	public static final DataGenCachedHolder<ProjectileConfig> SKULL = projectile("beholder/skull");
	public static final DataGenCachedHolder<ProjectileConfig> BONE = projectile("beholder/bone");
	public static final ResourceLocation MODEL = LostLegends.loc("spell/beholder/skull");
	private static final ResourceLocation TEX = LostLegends.loc("textures/block/spell/beholder/bone.png");
	private static final DoubleVariable SKULL_DMG = DoubleVariable.of("8");
	private static final DoubleVariable BONE_DMG = DoubleVariable.of("4");

	@Override
	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(SPELL.location()), "Beholder Bomb Magic");
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
		bone(new DataGenContext(ctx)).verifyOnBuild(ctx, BONE);
		skull(new DataGenContext(ctx)).verifyOnBuild(ctx, SKULL);
	}

	@Override
	public List<ResourceLocation> additionalModels() {
		return List.of(MODEL);
	}

	@Override
	public void genModel(RegistrateItemModelProvider pvd) {
		pvd.getBuilder(MODEL.getPath())
				.parent(new ModelFile.UncheckedModelFile(LostLegends.loc("custom/skull_projectile")))
				.texture("all", tex("beholder/skull_projectile"))
				.renderType("cutout");
	}

	private static ConfiguredEngine<?> spell(DataGenContext ctx) {
		return BeholderUtils.shootProj(new ListLogic(List.of(
				BeholderUtils.charge(ctx, new SimpleParticleData(RenderTypePreset.LIT, ParticleTypes.END_ROD), 20),
				shoot(ctx, 40, SKULL).delay(IntVariable.of("20"))
		)), 20, 0.03);
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

	private ProjectileConfig skull(DataGenContext ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.motion(new SimpleMotion(DoubleVariable.of("0"), DoubleVariable.of("0.03")))
				.tick(new SimpleParticleInstance(ParticleTypes.END_ROD, DoubleVariable.ZERO).move(ForwardOffsetModifier.of("-1")))
				.land(land(ctx))
				.hit(new DamageProcessor(ctx.damage(DamageTypes.INDIRECT_MAGIC), SKULL_DMG, true, true))
				.hit(new CastAtProcessor(CastAtProcessor.PosType.ORIGINAL, CastAtProcessor.DirType.ORIGINAL, land(ctx)))
				.renderer(new ModelRenderData(MODEL, DoubleVariable.of("1"), DoubleVariable.of("72")))
				.build();
	}

	private ConfiguredEngine<?> land(DataGenContext ctx) {
		int phi = 3;
		int theta = 6;
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.BONE_BLOCK_BREAK,
						DoubleVariable.of("1"),
						DoubleVariable.of("0.8+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new LoopIterator(
						IntVariable.of("" + phi),
						new LoopIterator(
								IntVariable.of("" + theta),
								new CustomProjectileShoot(
										DoubleVariable.of("0.5"), BONE,
										IntVariable.of("100"),
										false, false,
										Map.of()
								).move(new RotationModifier(
										DoubleVariable.of(360 / theta + "*j"),
										DoubleVariable.of(90 / phi + "*(i+0.5)")
								)), "j"
						).move(RotationModifier.of("rand(0,360)")), "i"
				).move(
						OffsetModifier.of("0", "0.1", "0"),
						SetDirectionModifier.of("1", "0", "0")
				)
		));
	}

	public static ProjectileConfig bone(DataGenContext ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.tick(new DustParticleInstance(
						ColorVariable.Static.of(0xFCFBED),
						DoubleVariable.of("0.5"),
						DoubleVariable.ZERO,
						IntVariable.of("20")
				).move(ForwardOffsetModifier.of("-0.2")))
				.hit(new DamageProcessor(ctx.damage(DamageTypes.MOB_PROJECTILE), BONE_DMG, true, true
				)).size(DoubleVariable.of("0.25"))
				.motion(SimpleMotion.BREAKING)
				.renderer(new OrientedCrossRenderData(TEX))
				.build();
	}


}