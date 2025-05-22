package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.common.CommonHooks;

import java.util.List;

public class GrowableShroomBlock extends BaseShroomBlock {

	public static final BooleanProperty GROWN = BooleanProperty.create("grown");

	protected final List<Holder<Block>> variants;

	public GrowableShroomBlock(Properties prop, int w, int h, List<Holder<Block>> variants) {
		super(prop, w, h);
		this.variants = variants;
		registerDefaultState(defaultBlockState().setValue(GROWN, true));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(GROWN);
		super.createBlockStateDefinition(builder);
	}

	@Override
	public BlockState getSmall() {
		return defaultBlockState().setValue(GROWN, false);
	}


	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		if (!state.getValue(GROWN)) {
			int chance = 5;
			if (CommonHooks.canCropGrow(level, pos, state, rand.nextInt(chance) == 0)) {
				BlockState next = state.setValue(GROWN, true);
				if (rand.nextInt(8) == 0 && !variants.isEmpty()) {
					next = variants.get(rand.nextInt(variants.size())).value().defaultBlockState();
				}
				level.setBlock(pos, next, 2);
				CommonHooks.fireCropGrowPost(level, pos, state);
			}
			return;
		}
		super.randomTick(state, level, pos, rand);
	}

}
