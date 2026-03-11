package dev.xkmc.lostlegends.modules.deepnether.init;

import com.tterrag.registrate.util.entry.FluidEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.modules.deepnether.block.fluid.LiquidSoulFluid;
import dev.xkmc.lostlegends.modules.deepnether.block.fluid.LiquidSoulFluidType;
import dev.xkmc.lostlegends.modules.deepnether.block.fluid.MoltenGoldFluid;
import dev.xkmc.lostlegends.modules.deepnether.block.fluid.MoltenGoldFluidType;
import net.minecraft.world.level.pathfinder.PathType;

public class DNFluids extends LLRegBase {

	public final FluidEntry<LiquidSoulFluid.Flowing> LIQUID_SOUL;
	public final FluidEntry<MoltenGoldFluid.Flowing> MOLTEN_GOLD;

	DNFluids(L2Registrate reg, String path) {
		super(reg, path);

		LIQUID_SOUL = fluid("liquid_soul", LiquidSoulFluidType::new,
				LiquidSoulFluid.Flowing::new, LiquidSoulFluid.Source::new)
				.properties(p -> p.lightLevel(15).temperature(1500).pathType(PathType.LAVA))
				.fluidProperties(p -> p.explosionResistance(100).tickRate(10))
				.register();

		MOLTEN_GOLD = fluid("molten_gold", MoltenGoldFluidType::new,
				MoltenGoldFluid.Flowing::new, MoltenGoldFluid.Source::new)
				.properties(p -> p.lightLevel(15).temperature(1500).pathType(PathType.LAVA))
				.fluidProperties(p -> p.explosionResistance(100).tickRate(20))
				.register();

	}

}
