package dev.xkmc.lostlegends.foundation.block;

import dev.xkmc.lostlegends.foundation.fogblock.FogConfig;
import dev.xkmc.lostlegends.foundation.fogblock.IFogBlock;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.function.Consumer;

public class LLFluidType extends FluidType implements IFogBlock {

	private final ResourceLocation stillTexture, flowingTexture;
	private final FogConfig fog;

	public LLFluidType(Properties prop, ResourceLocation stillTexture, ResourceLocation flowingTexture, FogConfig fog) {
		super(prop);
		this.stillTexture = stillTexture;
		this.flowingTexture = flowingTexture;
		this.fog = fog;
	}

	@Override
	public FogConfig getFogConfig() {
		return fog;
	}

	public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
		consumer.accept(new IClientFluidTypeExtensions() {
			public ResourceLocation getStillTexture() {
				return stillTexture;
			}

			public ResourceLocation getFlowingTexture() {
				return flowingTexture;
			}
		});
	}
}
