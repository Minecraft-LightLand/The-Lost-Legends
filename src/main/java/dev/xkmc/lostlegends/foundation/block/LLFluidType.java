package dev.xkmc.lostlegends.foundation.block;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.function.Consumer;

public class LLFluidType extends FluidType {

	private final ResourceLocation stillTexture, flowingTexture;

	public LLFluidType(Properties prop, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
		super(prop);
		this.stillTexture = stillTexture;
		this.flowingTexture = flowingTexture;
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
