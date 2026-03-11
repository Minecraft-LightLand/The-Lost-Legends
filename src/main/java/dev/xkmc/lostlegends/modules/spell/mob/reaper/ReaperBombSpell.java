package dev.xkmc.lostlegends.modules.spell.mob.reaper;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import dev.xkmc.l2magic.content.particle.engine.SimpleParticleData;
import dev.xkmc.lostlegends.modules.spell.init.LLSpellGenEntry;
import dev.xkmc.lostlegends.modules.spell.mob.beholder.BeholderUtils;
import dev.xkmc.lostlegends.modules.spell.mob.beholder.SkullBombSpell;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Map;

public class ReaperBombSpell extends LLSpellGenEntry {

	public static final ResourceKey<SpellAction> SPELL = spell("reaper_bomb_magic");

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

	private static ConfiguredEngine<?> spell(DataGenContext ctx) {
		return BeholderUtils.shootProj(new ListLogic(List.of(
				BeholderUtils.charge(ctx, new SimpleParticleData(RenderTypePreset.LIT, ParticleTypes.END_ROD),
						20, 3, 2, 10, 0.15),
				new ListLogic(List.of(
						shoot(ctx, 40, SkullBombSpell.SKULL),
						new DelayedIterator(IntVariable.of("5"), IntVariable.of("2"),
								shoot(ctx, 40, SkullBombSpell.SKULL).move(RotationModifier.of("rand(-20,20)", "rand(-20,20)")),
								null)
				)).delay(IntVariable.of("20"))
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

}