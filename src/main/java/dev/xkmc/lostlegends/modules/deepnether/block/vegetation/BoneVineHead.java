package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import com.mojang.serialization.MapCodec;
import dev.xkmc.lostlegends.foundation.block.FluidVineHead;
import dev.xkmc.lostlegends.foundation.block.SimpleLavaloggedBlock;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BoneVineHead extends FluidVineHead implements SimpleLavaloggedBlock {

	public static final MapCodec<BoneVineHead> CODEC = simpleCodec(BoneVineHead::new);

	public BoneVineHead(BlockBehaviour.Properties prop) {
		super(prop, Direction.UP);
	}

	@Override
	public MapCodec<BoneVineHead> codec() {
		return CODEC;
	}

	@Override
	public Block getBodyBlock() {
		return DeepNether.VEGE.SCORCHED_BONE_VINE_PLANT.get();
	}


}
