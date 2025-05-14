package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class BonemealableFlowerBlock extends FlowerBlock implements BonemealableBlock {

	private final ResourceKey<ConfiguredFeature<?, ?>> feature;

	public BonemealableFlowerBlock(Holder<MobEffect> eff, float time, Properties prop, ResourceKey<ConfiguredFeature<?, ?>> feature) {
		super(eff, time, prop);
		this.feature = feature;
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
		BlockPos up = pos.above();
		ChunkGenerator cgen = level.getChunkSource().getGenerator();
		Registry<ConfiguredFeature<?, ?>> registry = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
		registry.getHolder(feature).ifPresent((holder) -> holder.value().place(level, cgen, rand, pos));
	}

}
