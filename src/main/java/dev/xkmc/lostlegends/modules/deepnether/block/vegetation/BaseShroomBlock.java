package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.util.TriState;

public class BaseShroomBlock extends BushBlock implements BonemealableBlock {

	public static final MapCodec<BaseShroomBlock> CODEC = simpleCodec(BaseShroomBlock::new);
	protected static final VoxelShape SHAPE = Block.box(5, 0, 5, 11, 6, 11);

	protected BaseShroomBlock(Properties prop) {
		super(prop);
	}

	protected boolean isSame(BlockState state) {
		return state.getBlock() instanceof BaseShroomBlock;
	}

	public BlockState getSmall() {
		return defaultBlockState();
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		Vec3 vec3 = state.getOffset(level, pos);
		return SHAPE.move(vec3.x, vec3.y, vec3.z);
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
		randomTick(state, level, pos, rand);
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		if (rand.nextInt(25) == 0) {
			int repeat = 5;
			int r = 4;

			for (BlockPos ipos : BlockPos.betweenClosed(pos.offset(-r, -1, -r), pos.offset(r, 1, r))) {
				if (isSame(level.getBlockState(ipos))) {
					if (--repeat <= 0) {
						return;
					}
				}
			}
			BlockState small = getSmall();
			BlockPos ipos = null;
			for (int k = 0; k < 4; k++) {
				ipos = pos.offset(
						rand.nextInt(3) - 1,
						rand.nextInt(2) - rand.nextInt(2),
						rand.nextInt(3) - 1
				);
				if (level.isEmptyBlock(ipos) && small.canSurvive(level, ipos)) {
					pos = ipos;
				}
			}

			if (level.isEmptyBlock(ipos) && small.canSurvive(level, ipos)) {
				level.setBlock(ipos, small, 2);
			}
		}
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.isSolidRender(level, pos);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos down = pos.below();
		BlockState downState = level.getBlockState(down);
		TriState soil = downState.canSustainPlant(level, down, Direction.UP, state);
		return downState.is(BlockTags.MUSHROOM_GROW_BLOCK) || soil.isTrue() || soil.isDefault() &&
				this.mayPlaceOn(downState, level, down);
	}

	@Override
	protected MapCodec<? extends BaseShroomBlock> codec() {
		return CODEC;
	}

}
