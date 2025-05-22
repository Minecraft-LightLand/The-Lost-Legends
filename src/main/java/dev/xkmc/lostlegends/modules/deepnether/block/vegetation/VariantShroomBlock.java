package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class VariantShroomBlock extends BaseShroomBlock {

	protected final Supplier<GrowableShroomBlock> base;

	public VariantShroomBlock(Properties prop, int w, int h, Supplier<GrowableShroomBlock> base) {
		super(prop, w, h);
		this.base = base;
	}

	@Override
	public BlockState getSmall() {
		return base.get().getSmall();
	}

}
