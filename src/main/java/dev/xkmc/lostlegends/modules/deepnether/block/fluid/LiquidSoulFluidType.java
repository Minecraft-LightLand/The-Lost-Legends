package dev.xkmc.lostlegends.modules.deepnether.block.fluid;

import dev.xkmc.lostlegends.foundation.block.LLFluidType;
import dev.xkmc.lostlegends.foundation.fogblock.FogConfig;
import dev.xkmc.lostlegends.foundation.fogblock.IFogBlock;
import dev.xkmc.lostlegends.modules.deepnether.util.SoulEffectsHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class LiquidSoulFluidType extends LLFluidType implements IFogBlock {

	private static final FogConfig FOG = new FogConfig(FogConfig.Type.VIEWPORT, 0, 0.25f, 0.5f,
			0f, 8f, 2f, 24f, true);

	public LiquidSoulFluidType(Properties prop, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
		super(prop, stillTexture, flowingTexture);
	}

	@Override
	public FogConfig getFogConfig() {
		return FOG;
	}

	@Override
	public boolean isClear(LivingEntity le) {
		return SoulEffectsHelper.isClear(le);
	}

}
