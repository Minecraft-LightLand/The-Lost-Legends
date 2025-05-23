package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import com.mojang.serialization.MapCodec;
import dev.xkmc.lostlegends.foundation.block.FluidVineHead;
import dev.xkmc.lostlegends.foundation.block.LLFlowingFluid;
import dev.xkmc.lostlegends.modules.deepnether.block.fluid.SimpleSoulLoggedBlock;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SoulVineHead extends FluidVineHead implements SimpleSoulLoggedBlock {

	public static final MapCodec<SoulVineHead> CODEC = simpleCodec(SoulVineHead::new);

	public SoulVineHead(Properties prop) {
		super(prop, 3, 3, Direction.UP);
	}

	@Override
	public MapCodec<SoulVineHead> codec() {
		return CODEC;
	}

	@Override
	public Block getBodyBlock() {
		return DeepNether.VEGE.SCREAMING_SOUL_VINE_PLANT.get();
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {
		if (fluid() instanceof LLFlowingFluid ins && e.isInFluidType(fluid().getFluidType())) {
			ins.entityInside(e);
		}
	}

}
