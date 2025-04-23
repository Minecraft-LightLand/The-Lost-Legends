package dev.xkmc.lostlegends.modules.deepnether.block.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.block.Blocks;
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
		var vecLow = vec.add(-w / 2, 0, -w / 2);
		var vecHigh = vec.add(w / 2, h, w / 2);
		for (var pos : BlockPos.betweenClosed(BlockPos.containing(vecLow), BlockPos.containing(vecHigh))) {
			var state = sl.getBlockState(pos);
			if (state.isSolid()) {
				sl.setBlockAndUpdate(pos, Blocks.LAVA.defaultBlockState());
			}
		}
		return new DimensionTransition(sl, vec, e.getDeltaMovement(), e.getYRot(), e.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND);
	}

}
