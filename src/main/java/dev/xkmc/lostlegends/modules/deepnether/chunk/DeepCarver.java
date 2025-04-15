package dev.xkmc.lostlegends.modules.deepnether.chunk;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CaveWorldCarver;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.function.Function;

public class DeepCarver extends CaveWorldCarver {

	public DeepCarver(Codec<CaveCarverConfiguration> codec) {
		super(codec);
		this.liquids = ImmutableSet.of(Fluids.LAVA, Fluids.WATER);
	}

	@Override
	protected int getCaveBound() {
		return 10;
	}

	@Override
	protected float getThickness(RandomSource p_224907_) {
		return (p_224907_.nextFloat() * 2.0F + p_224907_.nextFloat()) * 2.0F;
	}

	@Override
	protected double getYScale() {
		return 5.0;
	}

	protected boolean carveBlock(
			CarvingContext ctx,
			CaveCarverConfiguration config,
			ChunkAccess chunk,
			Function<BlockPos, Holder<Biome>> biome,
			CarvingMask mask,
			BlockPos.MutableBlockPos pos,
			BlockPos.MutableBlockPos origin,
			Aquifer aquifer,
			MutableBoolean flag
	) {
		if (this.canReplaceBlock(config, chunk.getBlockState(pos))) {
			BlockState state = null;
			if (pos.getY() <= ctx.getMinGenY() + 31) {
				state = aquifer.computeSubstance(new DensityFunction.SinglePointContext(pos.getX(), pos.getY(), pos.getZ()), 0.0);
			}
			if (state == null) {
				state = CAVE_AIR;
			}
			chunk.setBlockState(pos, state, false);
			return true;
		} else {
			return false;
		}
	}
}
