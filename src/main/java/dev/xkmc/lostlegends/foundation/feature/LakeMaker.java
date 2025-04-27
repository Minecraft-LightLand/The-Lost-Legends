package dev.xkmc.lostlegends.foundation.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public class LakeMaker {

	private final ILakeFeature parent;
	private final boolean island;
	private final int maxHor;
	private final int maxVer;
	private int[] valid, edge;

	public LakeMaker(ILakeFeature parent, boolean island, int maxHor, int maxVer) {
		this.parent = parent;
		this.island = island;
		this.maxHor = maxHor;
		this.maxVer = maxVer;
		this.valid = new int[maxHor * maxHor];
		this.edge = new int[maxHor * maxHor];
	}

	protected boolean isValid(int x, int y, int z) {
		return (valid[x * maxHor + z] & (1 << y)) != 0;
	}

	protected void setValid(int x, int y, int z) {
		valid[x * maxHor + z] |= 1 << y;
	}

	protected boolean isEdge(int x, int y, int z) {
		return (edge[x * maxHor + z] & (1 << y)) != 0;
	}

	protected void setEdge(int x, int y, int z) {
		edge[x * maxHor + z] |= 1 << y;
	}

	public void pre(RandomSource rand, ILakeFeature.Data data, int size) {
		int offset = data.margin();
		int bottom = Math.max(2, offset);
		int maxThick = Math.min(data.depth(), maxVer - bottom - 2);
		for (int trial = 0; trial < size; trial++) {
			double rx = (rand.nextDouble() + 0.5) * data.radius();
			double ry = (rand.nextDouble() + 0.5) * maxThick;
			double rz = (rand.nextDouble() + 0.5) * data.radius();
			double dx = rand.nextDouble() * (maxHor - rx - 2 * offset) + offset + rx / 2;
			double dy = rand.nextDouble() * (maxVer - ry - bottom - 2) + bottom + ry / 2;
			double dz = rand.nextDouble() * (maxHor - rz - 2 * offset) + offset + rz / 2;
			for (int x = offset; x <= maxHor - offset - 1; x++) {
				for (int z = offset; z <= maxHor - offset - 1; z++) {
					for (int y = offset; y <= maxVer - offset - 1; y++) {
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
		for (int i = 1; i < offset; i++) {
			int[] copy = edge.clone();
			for (int dx = 0; dx < maxHor; dx++) {
				for (int dz = 0; dz < maxHor; dz++) {
					for (int dy = 0; dy < maxVer; dy++) {
						if (!isValid(dx, dy, dz) && !isEdge(dx, dy, dz)
								&& (dx < maxHor - 1 && isEdge(dx + 1, dy, dz)
								|| dx > 0 && isEdge(dx - 1, dy, dz)
								|| dz < maxHor - 1 && isEdge(dx, dy, dz + 1)
								|| dz > 0 && isEdge(dx, dy, dz - 1)
								|| dy < maxVer - 1 && isEdge(dx, dy + 1, dz)
								|| dy > 0 && isEdge(dx, dy - 1, dz)
						)) {
							copy[dx * maxHor + dz] |= 1 << dy;
						}
					}
				}
			}
			edge = copy;
		}
	}

	public boolean test(WorldGenLevel level, BlockPos origin, ILakeFeature.Data data) {
		BlockState fluid = data.fluid();
		var pos = new BlockPos.MutableBlockPos();
		for (int dx = 0; dx < maxHor; dx++) {
			for (int dz = 0; dz < maxHor; dz++) {
				for (int dy = 0; dy < maxVer; dy++) {
					if (isEdge(dz, dy, dz)) {
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
					} else if (isEdge(dx, dy, dz)) {
						pos.setWithOffset(origin, dx - maxHor / 2, dy, dz - maxHor / 2);
						BlockState state = level.getBlockState(pos);
						if ((dy < data.depth() || state.isSolid() && rand.nextInt(2) != 0) &&
								!state.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
							boolean surface = dy >= data.depth() - 1;
							if (surface) {
								pos.move(Direction.UP);
								surface = level.getBlockState(pos).isAir();
								pos.move(Direction.DOWN);
							}
							parent.setBarrier(level, pos, surface ? data.surface() : data.barrier());
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
