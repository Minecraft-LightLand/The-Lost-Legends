package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import com.mojang.serialization.MapCodec;
import dev.xkmc.lostlegends.foundation.block.FluidVineBody;
import dev.xkmc.lostlegends.foundation.block.SimpleLavaloggedBlock;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BoneVineBody extends FluidVineBody implements SimpleLavaloggedBlock {

	public static final MapCodec<BoneVineBody> CODEC = simpleCodec(BoneVineBody::new);

	@Override
	public MapCodec<BoneVineBody> codec() {
		return CODEC;
	}

	public BoneVineBody(BlockBehaviour.Properties prop) {
		super(prop, 1, Direction.UP);
	}

	@Override
	protected GrowingPlantHeadBlock getHeadBlock() {
		return DeepNether.VEGE.SCORCHED_BONE_VINE.get();
	}

}
