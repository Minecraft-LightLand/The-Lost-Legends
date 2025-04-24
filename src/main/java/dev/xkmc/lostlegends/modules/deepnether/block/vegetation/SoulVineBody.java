package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import com.mojang.serialization.MapCodec;
import dev.xkmc.lostlegends.foundation.block.FluidVineBody;
import dev.xkmc.lostlegends.modules.deepnether.block.fluid.SimpleSoulLoggedBlock;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;

public class SoulVineBody extends FluidVineBody implements SimpleSoulLoggedBlock {

	public static final MapCodec<SoulVineBody> CODEC = simpleCodec(SoulVineBody::new);

	@Override
	public MapCodec<SoulVineBody> codec() {
		return CODEC;
	}

	public SoulVineBody(Properties prop) {
		super(prop);
	}

	@Override
	protected GrowingPlantHeadBlock getHeadBlock() {
		return DeepNether.BLOCKS.SCREAMING_SOUL_VINE.get();
	}

}
