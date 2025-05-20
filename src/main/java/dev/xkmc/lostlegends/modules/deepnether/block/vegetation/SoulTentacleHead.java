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

public class SoulTentacleHead extends FluidVineHead implements SimpleSoulLoggedBlock {

	public static final MapCodec<SoulTentacleHead> CODEC = simpleCodec(SoulTentacleHead::new);

	public SoulTentacleHead(Properties prop) {
		this(prop, Direction.DOWN);
	}

	public SoulTentacleHead(Properties prop, Direction dir) {
		super(prop, dir);
	}

	@Override
	public MapCodec<SoulTentacleHead> codec() {
		return CODEC;
	}

	@Override
	public Block getBodyBlock() {
		return DeepNether.VEGE.SOUL_TENTACLE_DOWN_BODY.get();
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {
		if (fluid() instanceof LLFlowingFluid ins && e.isInFluidType(fluid().getFluidType())) {
			ins.entityInside(e);
		}
	}

}
