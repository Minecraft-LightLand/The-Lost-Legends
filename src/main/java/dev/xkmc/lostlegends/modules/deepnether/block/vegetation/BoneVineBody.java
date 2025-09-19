package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import com.mojang.serialization.MapCodec;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.lostlegends.foundation.block.AttachingFluidVineBody;
import dev.xkmc.lostlegends.foundation.block.FluidVineBody;
import dev.xkmc.lostlegends.foundation.block.SimpleLavaloggedBlock;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

public class BoneVineBody extends AttachingFluidVineBody implements SimpleLavaloggedBlock {

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
