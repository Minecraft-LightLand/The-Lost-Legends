package dev.xkmc.lostlegends.foundation.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LakeMaker {

	private final ILakeFeature parent;
	private final boolean island;
	private final int maxHor;
	private final int maxVer;
	private final int[][] valid, edge;

	public LakeMaker(ILakeFeature parent, boolean island, int maxHor, int maxVer) {
		this.parent = parent;
		this.island = island;
		this.maxHor = maxHor;
		this.maxVer = maxVer;
		this.valid = new int[maxHor][maxHor];
		this.edge = new int[maxHor][maxHor];
	}

	protected boolean isValid(int x, int y, int z) {
		return (valid[x][z] & (1 << y)) != 0;
	}

	protected void setValid(int x, int y, int z) {
		valid[x][z] |= 1 << y;
	}

	protected boolean isHole(int x, int y, int z) {
		return (edge[x][z] & (1 << y)) != 0;
	}

	protected void setEdge(int x, int y, int z) {
		edge[x][z] |= 1 << y;
	}

	public void pre(RandomSource rand, ILakeFeature.Data data, int size) {
		for (int trial = 0; trial < size; trial++) {
			double rx = (rand.nextDouble() + 0.5) * data.radius();
			double ry = (rand.nextDouble() + 0.5) * data.depth();
			double rz = (rand.nextDouble() + 0.5) * data.radius();
			double dx = rand.nextDouble() * (maxHor - rx - 2) + 1 + rx / 2;
			double dy = rand.nextDouble() * (maxVer - ry - data.depth()) + 2 + ry / 2;
			double dz = rand.nextDouble() * (maxHor - rz - 2) + 1 + rz / 2;
			for (int x = 1; x <= maxHor - 2; x++) {
				for (int z = 1; z <= maxHor - 2; z++) {
					for (int y = 1; y <= maxVer - 2; y++) {
						double x0 = ((double) x - dx) / (rx / 2);
						double y0 = ((double) y - dy) / (ry / 2);
						double z0 = ((double) z - dz) / (rz / 2);
						double r2 = x0 * x0 + y0 * y0 + z0 * z0;
						if (r2 < 1) {
							setValid(x, y, z);
						}
					}
				}
			}
		}
		for (int dx = 0; dx < maxHor; dx++) {
			for (int dz = 0; dz < maxHor; dz++) {
				for (int dy = 0; dy < maxVer; dy++) {
					if (!isValid(dx, dy, dz)
							&& (dx < maxHor - 1 && isValid(dx + 1, dy, dz)
							|| dx > 0 && isValid(dx - 1, dy, dz)
							|| dz < maxHor - 1 && isValid(dx, dy, dz + 1)
							|| dz > 0 && isValid(dx, dy, dz - 1)
							|| dy < maxVer - 1 && isValid(dx, dy + 1, dz)
							|| dy > 0 && isValid(dx, dy - 1, dz)
					)) setEdge(dx, dy, dz);
				}
			}
		}
	}

	public boolean test(WorldGenLevel level, BlockPos origin, ILakeFeature.Data data) {
		BlockState fluid = data.fluid();
		var pos = new BlockPos.MutableBlockPos();
		for (int dx = 0; dx < maxHor; dx++) {
			for (int dz = 0; dz < maxHor; dz++) {
				for (int dy = 0; dy < maxVer; dy++) {
					if (isHole(dz, dy, dz)) {
						pos.setWithOffset(origin, dx - maxHor / 2, dy, dz - maxHor / 2);
						BlockState block = level.getBlockState(pos);
						if (island && !block.isAir())
							return false;
						if (island)
							continue;
						if (dy >= data.depth() && block.liquid())
							return false;
						if (dy < data.depth() && !block.isSolid() && level.getBlockState(pos) != fluid)
							return false;
					}
				}
			}
		}
		return true;
	}

	public void gen(WorldGenLevel level, BlockPos origin, RandomSource rand, ILakeFeature.Data data) {
		var pos = new BlockPos.MutableBlockPos();
		for (int dx = 0; dx < maxHor; dx++) {
			for (int dz = 0; dz < maxHor; dz++) {
				for (int dy = 0; dy < maxVer; dy++) {
					if (isValid(dx, dy, dz)) {
						pos.setWithOffset(origin, dx - maxHor / 2, dy, dz - maxHor / 2);
						if (canReplaceBlock(level.getBlockState(pos))) {
							if (dy >= data.depth())
								parent.setAir(level, pos);
							else parent.setFluid(level, pos, data.fluid());
						}
					} else if (isHole(dx, dy, dz)) {
						pos.setWithOffset(origin, dx - maxHor / 2, dy, dz - maxHor / 2);
						BlockState state = level.getBlockState(pos);
						if ((dy < data.depth() || state.isSolid() && rand.nextInt(2) != 0) &&
								!state.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
							parent.setBarrier(level, pos, data.barrier());
						}
					}
				}
			}
		}
	}

	private boolean canReplaceBlock(BlockState state) {
		return !state.is(BlockTags.FEATURES_CANNOT_REPLACE);
	}

}
