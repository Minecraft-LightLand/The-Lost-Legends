package main;

import java.util.Arrays;
import java.util.Random;

public class Test {

	public static void main(String[] args) {
		boolean pass = true;
		for (int i = 0; i < 10000; i++) {
			var a = new LakeMakerA(20, 10);
			a.pre(new Random(i+12345), 3, 6, 6, 10);
			var c = new LakeMakerC(20, 10);
			c.pre(new Random(i+12345), 3, 6, 6, 10);
			pass &= Arrays.equals(a.valid, c.valid);
			pass &= Arrays.equals(a.edge, c.edge);
		}
		System.out.println(pass);

		long t0 = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			var a = new LakeMakerA(20, 10);
			a.pre(new Random(i), 3, 6, 6, 10);
		}
		long t1 = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			var b = new LakeMakerC(20, 10);
			b.pre(new Random(i), 3, 6, 6, 10);
		}
		long t2 = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			var c = new LakeMakerB(20, 10);
			c.pre(new Random(i), 3, 6, 6, 10);
		}
		long t3 = System.nanoTime();
		System.out.println("A: " + (t1 - t0) / 10000000 + "us");
		System.out.println("B: " + (t2 - t1) / 10000000 + "us");
		System.out.println("C: " + (t3 - t2) / 10000000 + "us");
	}


	static class LakeMakerA {

		private final int maxHor;
		private final int maxVer;
		private int[] valid, edge;

		public LakeMakerA(int maxHor, int maxVer) {
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

		public void pre(Random rand, int offset, int depth, int radius, int size) {
			int bottom = Math.max(2, offset);
			int maxThick = Math.min(depth, maxVer - bottom - 2);
			for (int trial = 0; trial < size; trial++) {
				double rx = (rand.nextDouble() + 0.5) * radius;
				double ry = (rand.nextDouble() + 0.5) * maxThick;
				double rz = (rand.nextDouble() + 0.5) * radius;
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


	}

	static class LakeMakerB {

		private final int maxHor;
		private final int maxVer;
		private int[] valid, edge;

		public LakeMakerB(int maxHor, int maxVer) {
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

		public void pre(Random rand, int offset, int depth, int radius, int size) {
			int bottom = Math.max(2, offset);
			int maxThick = Math.min(depth, maxVer - bottom - 2);
			for (int trial = 0; trial < size; trial++) {
				double rx = (rand.nextDouble() + 0.5) * radius;
				double ry = (rand.nextDouble() + 0.5) * maxThick;
				double rz = (rand.nextDouble() + 0.5) * radius;
				double dx = rand.nextDouble() * (maxHor - rx - 2 * offset) + offset + rx / 2;
				double dy = rand.nextDouble() * (maxVer - ry - bottom - 2) + bottom + ry / 2;
				double dz = rand.nextDouble() * (maxHor - rz - 2 * offset) + offset + rz / 2;

				int minX = Math.max(offset, (int) (dx - rx / 2));
				int maxX = Math.min(maxHor - offset - 1, (int) (dx + rx / 2));
				int minZ = Math.max(offset, (int) (dz - rz / 2));
				int maxZ = Math.min(maxHor - offset - 1, (int) (dz + rz / 2));

				for (int x = minX; x <= maxX; x++) {
					for (int z = minZ; z <= maxZ; z++) {
						double x0 = ((double) x - dx) / (rx / 2);
						double z0 = ((double) z - dz) / (rz / 2);
						double y2 = 1 - x0 * x0 - z0 * z0;
						if (y2 < 0) continue;
						for (int y = offset; y <= maxVer - offset - 1; y++) {
							double y0 = ((double) y - dy) / (ry / 2);
							if (y0 * y0 < y2) {
								setValid(x, y, z);
							}
						}
					}
				}
			}
			for (int dx = 0; dx < maxHor; dx++) {
				for (int dz = 0; dz < maxHor; dz++) {
					int index = dx * maxHor + dz;
					int mask = valid[index] << 1 | valid[index] >> 1;
					if (dx > 0) mask |= valid[index - maxHor];
					if (dx < maxHor - 1) mask |= valid[index + maxHor];
					if (dz > 0) mask |= valid[index - 1];
					if (dz < maxHor - 1) mask |= valid[index + 1];
					edge[dx * maxHor + dz] = ~valid[dx * maxHor + dz] & mask;
				}
			}
			for (int i = 1; i < offset; i++) {
				int[] copy = edge.clone();
				for (int dx = 0; dx < maxHor; dx++) {
					for (int dz = 0; dz < maxHor; dz++) {
						int index = dx * maxHor + dz;
						int mask = edge[index] << 1 | edge[index] >> 1;
						if (dx > 0) mask |= edge[index - maxHor];
						if (dx < maxHor - 1) mask |= edge[index + maxHor];
						if (dz > 0) mask |= edge[index - 1];
						if (dz < maxHor - 1) mask |= edge[index + 1];
						copy[index] |= ~valid[index] & ~edge[index] & mask;
					}
				}
				edge = copy;
			}
		}


	}

	static class LakeMakerC {

		private final int maxHor;
		private final int maxVer;
		private int[] valid, edge;

		public LakeMakerC(int maxHor, int maxVer) {
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

		public void pre(Random rand, int offset, int depth, int radius, int size) {
			int bottom = Math.max(2, offset);
			int maxThick = Math.min(depth, maxVer - bottom - 2);
			for (int trial = 0; trial < size; trial++) {
				double rx = (rand.nextDouble() + 0.5) * radius;
				double ry = (rand.nextDouble() + 0.5) * maxThick;
				double rz = (rand.nextDouble() + 0.5) * radius;
				double dx = rand.nextDouble() * (maxHor - rx - 2 * offset) + offset + rx / 2;
				double dy = rand.nextDouble() * (maxVer - ry - bottom - 2) + bottom + ry / 2;
				double dz = rand.nextDouble() * (maxHor - rz - 2 * offset) + offset + rz / 2;

				int minX = Math.max(offset, (int) (dx - rx / 2));
				int maxX = Math.min(maxHor - offset - 1, (int) (dx + rx / 2));
				int minZ = Math.max(offset, (int) (dz - rz / 2));
				int maxZ = Math.min(maxHor - offset - 1, (int) (dz + rz / 2));

				for (int x = minX; x <= maxX; x++) {
					for (int z = minZ; z <= maxZ; z++) {
						double x0 = ((double) x - dx) / (rx / 2);
						double z0 = ((double) z - dz) / (rz / 2);
						double y2 = 1 - x0 * x0 - z0 * z0;
						if (y2 < 0) continue;
						double vy = Math.sqrt(y2) * (ry / 2);
						int minY = Math.max(offset, (int) (dy - vy));
						int maxY = Math.min(maxVer - offset - 1, (int) (dy + vy));
						for (int y = minY; y <= maxY; y++) {
							double y0 = ((double) y - dy) / (ry / 2);
							if (y0 * y0 < y2) {
								setValid(x, y, z);
							}
						}
					}
				}
			}
			for (int dx = 0; dx < maxHor; dx++) {
				for (int dz = 0; dz < maxHor; dz++) {
					int index = dx * maxHor + dz;
					int mask = valid[index] << 1 | valid[index] >> 1;
					if (dx > 0) mask |= valid[index - maxHor];
					if (dx < maxHor - 1) mask |= valid[index + maxHor];
					if (dz > 0) mask |= valid[index - 1];
					if (dz < maxHor - 1) mask |= valid[index + 1];
					edge[dx * maxHor + dz] = ~valid[dx * maxHor + dz] & mask;
				}
			}
			for (int i = 1; i < offset; i++) {
				int[] copy = edge.clone();
				for (int dx = 0; dx < maxHor; dx++) {
					for (int dz = 0; dz < maxHor; dz++) {
						int index = dx * maxHor + dz;
						int mask = edge[index] << 1 | edge[index] >> 1;
						if (dx > 0) mask |= edge[index - maxHor];
						if (dx < maxHor - 1) mask |= edge[index + maxHor];
						if (dz > 0) mask |= edge[index - 1];
						if (dz < maxHor - 1) mask |= edge[index + 1];
						copy[index] |= ~valid[index] & ~edge[index] & mask;
					}
				}
				edge = copy;
			}
		}


	}

}
