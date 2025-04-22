package dev.xkmc.lostlegends.modules.deepnether.block.portal;

import dev.xkmc.lostlegends.modules.deepnether.data.DNDimensionGen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Optional;

public class LavaPortalHelper {

	@Nullable
	public static DimensionTransition getExitPortal(
			ServerLevel level, Entity e, BlockPos target, WorldBorder border
	) {
		int range = level.dimension().equals(DNDimensionGen.DEEP_NETHER) ? 64 : 128;
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


	public static Vec3 findCollisionFreePosition(Vec3 p_260315_, ServerLevel p_259704_, Entity p_259626_, EntityDimensions p_259816_) {
		if (!(p_259816_.width() > 4.0F) && !(p_259816_.height() > 4.0F)) {
			double d0 = (double) p_259816_.height() / 2.0;
			Vec3 vec3 = p_260315_.add(0.0, d0, 0.0);
			VoxelShape voxelshape = Shapes.create(
					AABB.ofSize(vec3, (double) p_259816_.width(), 0.0, (double) p_259816_.width()).expandTowards(0.0, 1.0, 0.0).inflate(1.0E-6)
			);
			Optional<Vec3> optional = p_259704_.findFreePosition(
					p_259626_, voxelshape, vec3, (double) p_259816_.width(), (double) p_259816_.height(), (double) p_259816_.width()
			);
			Optional<Vec3> optional1 = optional.map(p_259019_ -> p_259019_.subtract(0.0, d0, 0.0));
			return optional1.orElse(p_260315_);
		} else {
			return p_260315_;
		}
	}
}
