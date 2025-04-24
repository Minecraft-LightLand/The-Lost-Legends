package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import com.mojang.serialization.MapCodec;
import dev.xkmc.lostlegends.foundation.block.FluidVineHead;
import dev.xkmc.lostlegends.modules.deepnether.block.fluid.SimpleSoulLoggedBlock;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.world.level.block.Block;

public class SoulVineHead extends FluidVineHead implements SimpleSoulLoggedBlock {

	public static final MapCodec<SoulVineHead> CODEC = simpleCodec(SoulVineHead::new);

	public SoulVineHead(Properties prop) {
		super(prop);
	}

	@Override
	public MapCodec<SoulVineHead> codec() {
		return CODEC;
	}

	@Override
	public Block getBodyBlock() {
		return DeepNether.BLOCKS.SCREAMING_SOUL_VINE_PLANT.get();
	}


}
