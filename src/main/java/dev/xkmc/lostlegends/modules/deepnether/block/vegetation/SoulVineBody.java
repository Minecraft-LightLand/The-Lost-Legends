package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import com.mojang.serialization.MapCodec;
import dev.xkmc.lostlegends.foundation.block.FluidVineBody;
import dev.xkmc.lostlegends.foundation.block.LLFlowingFluid;
import dev.xkmc.lostlegends.modules.deepnether.block.fluid.SimpleSoulLoggedBlock;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;

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

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {
		if (fluid() instanceof LLFlowingFluid ins && e.isInFluidType(fluid().getFluidType())) {
			ins.entityInside(e);
		}
	}

}
