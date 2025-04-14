
package dev.xkmc.lostlegends.modules.deepnether.block;

import com.mojang.serialization.MapCodec;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.lighting.LightEngine;

public class DNNyliumBlock extends Block implements BonemealableBlock {
	public static final MapCodec<DNNyliumBlock> CODEC = simpleCodec(DNNyliumBlock::new);

	public MapCodec<DNNyliumBlock> codec() {
		return CODEC;
	}

	public DNNyliumBlock(BlockBehaviour.Properties prop) {
		super(prop);
	}

	private static boolean canBeNylium(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos up = pos.above();
		BlockState upState = level.getBlockState(up);
		int light = LightEngine.getLightBlockInto(level, state, pos, upState, up, Direction.UP, upState.getLightBlock(level, up));
		return light < level.getMaxLightLevel();
	}

	protected void randomTick(BlockState state, ServerLevel sl, BlockPos pos, RandomSource rand) {
		if (!canBeNylium(state, sl, pos)) {
			sl.setBlockAndUpdate(pos, DeepNether.BLOCKS.NETHER_SOIL.get().defaultBlockState());
		}

	}

	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		return level.getBlockState(pos.above()).isAir();
	}

	public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
		return true;
	}

	public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
		BlockPos up = pos.above();
		ChunkGenerator cgen = level.getChunkSource().getGenerator();
		Registry<ConfiguredFeature<?, ?>> registry = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
		place(registry, NetherFeatures.CRIMSON_FOREST_VEGETATION_BONEMEAL, level, cgen, rand, up);
	}

	private void place(Registry<ConfiguredFeature<?, ?>> reg, ResourceKey<ConfiguredFeature<?, ?>> cf,
					   ServerLevel sl, ChunkGenerator cgen, RandomSource rand, BlockPos pos) {
		reg.getHolder(cf).ifPresent((holder) -> holder.value().place(sl, cgen, rand, pos));
	}

	public BonemealableBlock.Type getType() {
		return Type.NEIGHBOR_SPREADER;
	}

}
