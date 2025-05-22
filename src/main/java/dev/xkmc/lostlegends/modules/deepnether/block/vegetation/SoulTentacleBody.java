package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.lostlegends.foundation.block.FluidVineBody;
import dev.xkmc.lostlegends.foundation.block.LLFlowingFluid;
import dev.xkmc.lostlegends.modules.deepnether.block.fluid.SimpleSoulLoggedBlock;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

public class SoulTentacleBody extends FluidVineBody implements SimpleSoulLoggedBlock {

	public static final MapCodec<SoulTentacleBody> CODEC = simpleCodec(SoulTentacleBody::new);
	public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;

	@Override
	public MapCodec<SoulTentacleBody> codec() {
		return CODEC;
	}

	public SoulTentacleBody(Properties prop) {
		this(prop, Direction.DOWN);
	}

	public SoulTentacleBody(Properties prop, Direction dir) {
		super(prop, 1, dir);
		registerDefaultState(defaultBlockState().setValue(ATTACHED, false));
	}

	@Override
	protected GrowingPlantHeadBlock getHeadBlock() {
		return DeepNether.VEGE.SOUL_TENTACLE_DOWN.get();
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {
		if (fluid() instanceof LLFlowingFluid ins && e.isInFluidType(fluid().getFluidType())) {
			ins.entityInside(e);
		}
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return state.getValue(ATTACHED) ? Shapes.block() : super.getShape(state, level, pos, ctx);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(property(), ATTACHED);
	}

	@Override
	protected BlockState updateShape(
			BlockState state, Direction dir, BlockState nstate, LevelAccessor level, BlockPos pos, BlockPos npos
	) {
		var prev = super.updateShape(state, dir, nstate, level, pos, npos);
		if (prev.is(this)) {
			var from = level.getBlockState(pos.relative(growthDirection.getOpposite()));
			var to = level.getBlockState(pos.relative(growthDirection));
			prev = prev.setValue(ATTACHED, !from.is(this) && to.is(this));
		}
		return prev;
	}

	public Direction getDirection() {
		return growthDirection;
	}

	public static void buildBlockStates(DataGenContext<Block, SoulTentacleBody> ctx, RegistrateBlockstateProvider pvd) {
		pvd.getVariantBuilder(ctx.get()).forAllStatesExcept(
				state -> {
					var root = state.getValue(ATTACHED);
					var id = ctx.getName();
					if (root) id += "_attached";
					var ans = pvd.models().cross("block/" + id, DeepNether.VEGE.blockLoc(id))
							.renderType("cutout");
					return ConfiguredModel.builder().modelFile(ans).build();
				}, ctx.get().property());
	}
}
