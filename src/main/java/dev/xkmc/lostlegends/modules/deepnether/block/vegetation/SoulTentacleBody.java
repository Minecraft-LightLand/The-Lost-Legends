package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.lostlegends.foundation.block.AttachingFluidVineBody;
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

public class SoulTentacleBody extends AttachingFluidVineBody implements SimpleSoulLoggedBlock {

	public static final MapCodec<SoulTentacleBody> CODEC = simpleCodec(SoulTentacleBody::new);

	@Override
	public MapCodec<SoulTentacleBody> codec() {
		return CODEC;
	}

	public SoulTentacleBody(Properties prop) {
		this(prop, Direction.DOWN);
	}

	public SoulTentacleBody(Properties prop, Direction dir) {
		super(prop, 1, dir);
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

	public Direction getDirection() {
		return growthDirection;
	}

}
