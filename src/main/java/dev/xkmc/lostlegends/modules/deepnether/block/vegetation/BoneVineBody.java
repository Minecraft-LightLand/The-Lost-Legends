package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import com.mojang.serialization.MapCodec;
import dev.xkmc.lostlegends.foundation.block.SimpleLavaloggedBlock;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.Shapes;

public class BoneVineBody extends GrowingPlantBodyBlock implements SimpleLavaloggedBlock {

	public static final MapCodec<BoneVineBody> CODEC = simpleCodec(BoneVineBody::new);

	@Override
	public MapCodec<BoneVineBody> codec() {
		return CODEC;
	}

	public BoneVineBody(BlockBehaviour.Properties prop) {
		super(prop, Direction.UP, Shapes.block(), true);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LAVALOGGED);
	}

	@Override
	protected GrowingPlantHeadBlock getHeadBlock() {
		return DeepNether.BLOCKS.SCORCHED_BONE_VINE.get();
	}

	protected BlockState updateHeadAfterConvertedFromBody(BlockState body, BlockState head) {
		return head.setValue(LAVALOGGED, body.getValue(LAVALOGGED));
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.getValue(LAVALOGGED) ? Fluids.LAVA.getSource(false) : super.getFluidState(state);
	}

}
