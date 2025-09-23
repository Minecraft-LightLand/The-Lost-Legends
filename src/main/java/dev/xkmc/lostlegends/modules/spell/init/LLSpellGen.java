package dev.xkmc.lostlegends.modules.spell.init;

import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.spell.mob.beholder.BeholderSpell;
import dev.xkmc.lostlegends.modules.spell.mob.beholder.FlameBeholderSpell;
import dev.xkmc.lostlegends.modules.spell.mob.beholder.PoisonBeholderSpell;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;

public class LLSpellGen {

	public static final List<LLSpellGenEntry> LIST = List.of(
			new BeholderSpell(),
			new PoisonBeholderSpell(),
			new FlameBeholderSpell()
	);

	public static void gatherData(GatherDataEvent event) {
		var init = LostLegends.REGISTRATE.getDataGenInitializer();

		LostLegends.REGISTRATE.addDataGenerator(ProviderType.LANG, pvd ->
				LIST.forEach(e -> e.genLang(pvd)));

		LostLegends.REGISTRATE.addDataGenerator(ProviderType.ITEM_MODEL, pvd ->
				LIST.forEach(e -> e.genModel(pvd)));

		init.add(EngineRegistry.PROJECTILE, ctx ->
				LIST.forEach(e -> e.registerProjectile(ctx)));

		init.add(EngineRegistry.SPELL, ctx ->
				LIST.forEach(e -> e.register(ctx)));
	}

}
