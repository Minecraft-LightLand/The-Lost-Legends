package dev.xkmc.lostlegends.modules.maze.block;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.mult.*;
import dev.xkmc.l2modularblock.one.MirrorRotateBlockMethod;
import dev.xkmc.l2modularblock.one.SpecialDropBlockMethod;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import dev.xkmc.lostlegends.modules.maze.init.MazeModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.List;
import java.util.Map;

public class MazeWallBlock {

	public static final Neighbor NEIGHBOR = new Neighbor();
	public static final AllDireState ALL_DIRE_STATE = new AllDireState();
	public static final Spawner SPAWNER = new Spawner();

	public static final int DELAY = 4;

	public static class Neighbor implements NeighborUpdateBlockMethod {

		@Override
		public void neighborChanged(Block self, BlockState state, Level level, BlockPos pos, Block nei_block, BlockPos nei_pos, boolean moving) {
			if (level.isClientSide())
				return;
			level.scheduleTick(pos, self, DELAY);
		}
	}

	public static class Spawner implements NeighborUpdateBlockMethod, DefaultStateBlockMethod, ScheduleTickBlockMethod, CreateBlockStateBlockMethod {

		@Override
		public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			builder.add(BlockStateProperties.POWERED);
		}

		@Override
		public BlockState getDefaultState(BlockState state) {
			return state.setValue(BlockStateProperties.POWERED, false);
		}

		@Override
		public void neighborChanged(Block self, BlockState state, Level level, BlockPos pos, Block nei_block, BlockPos nei_pos, boolean moving) {
			if (level.isClientSide())
				return;
			boolean flag = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
			if (state.getValue(BlockStateProperties.POWERED) != flag)
				level.scheduleTick(pos, state.getBlock(), 2);
		}

		@Override
		public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
			boolean flag = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
			if (state.getValue(BlockStateProperties.POWERED) != flag)
				level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, flag));
		}

	}

	public static class AllDireState implements UseItemOnBlockMethod,
			CreateBlockStateBlockMethod, DefaultStateBlockMethod, PlacementBlockMethod,
			ScheduleTickBlockMethod, MirrorRotateBlockMethod, SpecialDropBlockMethod {

		public static final BooleanProperty[] PROPS = {BlockStateProperties.DOWN, BlockStateProperties.UP,
				BlockStateProperties.NORTH, BlockStateProperties.SOUTH,
				BlockStateProperties.WEST, BlockStateProperties.EAST};

		private static final Map<Direction, BooleanProperty> MAP = Map.of(
				Direction.DOWN, BlockStateProperties.DOWN,
				Direction.UP, BlockStateProperties.UP,
				Direction.NORTH, BlockStateProperties.NORTH,
				Direction.SOUTH, BlockStateProperties.SOUTH,
				Direction.WEST, BlockStateProperties.WEST,
				Direction.EAST, BlockStateProperties.EAST);

		public static void buildModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
			var tin = MazeModule.BLOCKS.blockLoc("in");
			var tout = MazeModule.BLOCKS.blockLoc("out");
			var min = pvd.models().getBuilder(ctx.getName() + "_in")
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/face")))
					.texture("texture", tin).texture("particle", tin);
			var mout = pvd.models().getBuilder(ctx.getName() + "_out")
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/face")))
					.texture("texture", tout).texture("particle", tout);
			pvd.getMultipartBuilder(ctx.get())
					.part().modelFile(min)
					.addModel().condition(BlockStateProperties.NORTH, true).end()
					.part().modelFile(min).rotationY(90).uvLock(true)
					.addModel().condition(BlockStateProperties.EAST, true).end()
					.part().modelFile(min).rotationY(180).uvLock(true)
					.addModel().condition(BlockStateProperties.SOUTH, true).end()
					.part().modelFile(min).rotationY(270).uvLock(true)
					.addModel().condition(BlockStateProperties.WEST, true).end()
					.part().modelFile(min).rotationX(270).uvLock(true)
					.addModel().condition(BlockStateProperties.UP, true).end()
					.part().modelFile(min).rotationX(90).uvLock(true)
					.addModel().condition(BlockStateProperties.DOWN, true).end()
					.part().modelFile(mout)
					.addModel().condition(BlockStateProperties.NORTH, false).end()
					.part().modelFile(mout).rotationY(90).uvLock(true)
					.addModel().condition(BlockStateProperties.EAST, false).end()
					.part().modelFile(mout).rotationY(180).uvLock(true)
					.addModel().condition(BlockStateProperties.SOUTH, false).end()
					.part().modelFile(mout).rotationY(270).uvLock(true)
					.addModel().condition(BlockStateProperties.WEST, false).end()
					.part().modelFile(mout).rotationX(270).uvLock(true)
					.addModel().condition(BlockStateProperties.UP, false).end()
					.part().modelFile(mout).rotationX(90).uvLock(true)
					.addModel().condition(BlockStateProperties.DOWN, false).end();
			pvd.models().cubeAll(ctx.getName(), tout);
		}

		@Override
		public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
			builder.add(PROPS);
		}

		@Override
		public BlockState getDefaultState(BlockState state) {
			for (BooleanProperty bp : PROPS)
				state = state.setValue(bp, false);
			return state;
		}

		@Override
		public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
			BlockState rep = getDefaultState(state);
			BlockState self = state;
			for (Direction dire : Direction.values()) {
				BlockPos next = pos.relative(dire);
				if (level.isOutsideBuildHeight(next))
					continue;
				BlockState nei = level.getBlockState(next);
				if (self.getValue(MAP.get(dire))) {
					if (nei.getBlock() != self.getBlock()) {
						nei = getStateForPlacement(rep, level, next);
						level.setBlockAndUpdate(next, nei);
					}
				} else if (nei.getBlock() == self.getBlock()) {
					self = self.setValue(MAP.get(dire), true);
				}
			}
			if (self != state)
				level.setBlockAndUpdate(pos, self);
		}

		@Override
		public BlockState mirror(BlockState state, Mirror mirrorIn) {
			BlockState ans = state;
			for (int i = 2; i < 6; i++) {
				Direction d0 = Direction.values()[i];
				Direction d1 = mirrorIn.mirror(d0);
				ans = ans.setValue(PROPS[d1.ordinal()], state.getValue(PROPS[d0.ordinal()]));
			}
			return ans;
		}

		@Override
		public BlockState rotate(BlockState state, Rotation rot) {
			BlockState ans = state;
			for (int i = 2; i < 6; i++) {
				Direction d0 = Direction.values()[i];
				Direction d1 = rot.rotate(d0);
				ans = ans.setValue(PROPS[d1.ordinal()], state.getValue(PROPS[d0.ordinal()]));
			}
			return ans;
		}

		@Override
		public BlockState getStateForPlacement(BlockState def, BlockPlaceContext context) {
			Level level = context.getLevel();
			BlockPos pos = context.getClickedPos();
			return getStateForPlacement(def, level, pos);
		}

		private BlockState getStateForPlacement(BlockState def, Level level, BlockPos pos) {
			for (Direction dire : Direction.values()) {
				BlockState nei = level.getBlockState(pos.relative(dire));
				if (nei.getBlock() == def.getBlock()) {
					def = def.setValue(MAP.get(dire), true);
				}
			}
			return def;
		}

		@Override
		public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player pl, InteractionHand hand, BlockHitResult result) {
			if (stack.is(DeepNether.ITEMS.PORTAL_DISSONATOR.get())) {
				if (!level.isClientSide()) {
					for (Direction dire : Direction.values()) {
						BlockPos next = pos.relative(dire);
						if (level.isOutsideBuildHeight(next))
							continue;
						BlockState nei = level.getBlockState(next);
						if (nei.getBlock() == state.getBlock()) {
							level.setBlockAndUpdate(next, nei.setValue(MAP.get(dire.getOpposite()), false));
						}
					}
					level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
					if (!pl.getAbilities().instabuild)
						stack.shrink(1);
				}
				return ItemInteractionResult.SUCCESS;
			}
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		@Override
		public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
			for (Direction dire : Direction.values()) {
				if (state.getValue(MAP.get(dire))) {
					return List.of();
				}
			}
			return List.of(new ItemStack(state.getBlock()));
		}
	}

}
