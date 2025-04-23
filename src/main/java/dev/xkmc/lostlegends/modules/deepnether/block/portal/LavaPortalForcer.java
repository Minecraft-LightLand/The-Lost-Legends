package dev.xkmc.lostlegends.modules.deepnether.block.portal;

import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.border.WorldBorder;

import java.util.Comparator;
import java.util.Optional;

public class LavaPortalForcer {

	protected final ServerLevel level;

	public LavaPortalForcer(ServerLevel level) {
		this.level = level;
	}

	public Optional<BlockPos> findClosestPortalPosition(BlockPos pos, int range, WorldBorder border) {
		PoiManager poimanager = this.level.getPoiManager();
		poimanager.ensureLoadedAndValid(this.level, pos, range);
		return poimanager.getInSquare(p -> p.is(DeepNether.BLOCKS.PORTAL_POI.key()), pos, range, PoiManager.Occupancy.ANY)
				.map(PoiRecord::getPos)
				.filter(border::isWithinBounds)
				.min(Comparator.<BlockPos>comparingDouble(p -> p.distSqr(pos)).thenComparingInt(Vec3i::getY));
	}

	public void ensureFeatureLoaded(ServerLevel level, BlockPos pos, int range) {
		level.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(pos), Math.floorDiv(range, 16) + 1, pos);
	}

	public boolean isFeatureLoaded(ServerLevel level, BlockPos pos, int range) {
		var list = ChunkPos.rangeClosed(new ChunkPos(pos), Math.floorDiv(range, 16)).toList();
		for (var cpos : list) {
			if (!level.getChunkSource().hasChunk(cpos.x, cpos.z))
				return false;
		}
		return true;
	}

}
