package dev.xkmc.lostlegends.modules.deepnether.block.portal;

import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

public class LavaPortalHelper {

	@Nullable
	public static DimensionTransition getExitPortal(
			ServerLevel level, Entity e, BlockPos target, WorldBorder border, int range
	) {
		Optional<BlockPos> optional = new LavaPortalForcer(level).findClosestPortalPosition(target, range, border);
		DimensionTransition.PostDimensionTransition transition;
		if (optional.isEmpty()) return null;
		BlockPos blockpos = optional.get();
		transition = DimensionTransition.PLAY_PORTAL_SOUND.then(x -> x.placePortalTicket(blockpos));
		EntityDimensions dim = e.getDimensions(e.getPose());
		Vec3 newPos = PortalShape.findCollisionFreePosition(blockpos.getBottomCenter(), level, e, dim);
		return new DimensionTransition(level, newPos, e.getDeltaMovement(), e.getYRot(), e.getXRot(), transition);
	}

	public static DimensionTransition createExitPortal(ServerLevel sl, Entity e, BlockPos target) {
		double w = e.getBbWidth();
		double h = e.getBbHeight();
		var vec = target.getBottomCenter();
		var vecLow = BlockPos.containing(vec.add(-w / 2, 0, -w / 2));
		var vecHigh = BlockPos.containing(vec.add(w / 2, h, w / 2));
		if (vecHigh.getX() == vecLow.getX()) vecHigh = vecHigh.offset(1, 0, 0);
		if (vecHigh.getZ() == vecLow.getZ()) vecHigh = vecHigh.offset(0, 0, 1);
		for (var pos : BlockPos.betweenClosed(vecLow, vecHigh)) {
			var state = sl.getBlockState(pos);
			if (state.isSolid()) {
				sl.setBlockAndUpdate(pos, pos.getY() == vecHigh.getY() ? DeepNether.BLOCKS.PORTAL.getDefaultState() : Blocks.LAVA.defaultBlockState());
			}
		}
		int x0 = vecLow.getX(), x1 = vecHigh.getX(), z0 = vecLow.getZ(), z1 = vecHigh.getZ(), y = vecLow.getY();
		var mpos = new BlockPos.MutableBlockPos();
		int max = -1, min = 16;
		for (int x = x0; x <= x1; x++) {
			for (int z = z0; z <= z1; z++) {
				int height = -1;
				for (int dy = 1; dy <= 16; dy++) {
					mpos.set(x, y - dy, z);
					var state = sl.getBlockState(mpos);
					if (!state.isSolid()) {
						height = dy - 1;
						break;
					}
				}
				max = Math.max(max, height);
				min = Math.min(min, height);
			}
		}
		var falling = Blocks.LAVA.defaultBlockState().setValue(LiquidBlock.LEVEL, 8);
		if (max > 0) {
			for (int x = x0; x <= x1; x++) {
				for (int z = z0; z <= z1; z++) {
					for (int dy = 1; dy <= max + 3; dy++) {
						mpos.set(x, y - dy, z);
						var state = sl.getBlockState(mpos);
						if (state.isSolid()) {
							sl.setBlockAndUpdate(mpos, falling);
						} else break;
					}
				}
			}
		} else {
			for (int x = x0; x <= x1; x++) {
				for (int z = z0; z <= z1; z++) {
					for (int dy = 1; dy <= 7; dy++) {
						mpos.set(x, y - dy, z);
						sl.setBlockAndUpdate(mpos, falling);
					}
				}
			}
			for (int x = x0 - 3; x <= x1 + 3; x++) {
				for (int z = z0 - 3; z <= z1 + 3; z++) {
					if (x >= x0 && x <= x1 && z >= z0 && z <= z1) continue;
					for (int dy = 4; dy <= 6; dy++) {
						mpos.set(x, y - dy, z);
						sl.setBlockAndUpdate(mpos, Blocks.AIR.defaultBlockState());
					}
				}
			}
		}

		return new DimensionTransition(sl, vec, e.getDeltaMovement(), e.getYRot(), e.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND);
	}

}
