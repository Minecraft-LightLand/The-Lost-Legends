package dev.xkmc.lostlegends.modules.deepnether.block.portal;

import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.status.ChunkStatus;

import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class LavaPortalForcer {
	protected final ServerLevel level;

	public LavaPortalForcer(ServerLevel p_77650_) {
		this.level = p_77650_;
	}

	public Optional<BlockPos> findClosestPortalPosition(BlockPos pos, int range, WorldBorder border) {
		PoiManager poimanager = this.level.getPoiManager();
		ensureFeatureLoaded(level, pos, range);
		poimanager.ensureLoadedAndValid(this.level, pos, range);
		return poimanager.getInSquare(p -> p.is(DeepNether.BLOCKS.PORTAL_POI.key()), pos, range, PoiManager.Occupancy.ANY)
				.map(PoiRecord::getPos)
				.filter(border::isWithinBounds)
				.min(Comparator.<BlockPos>comparingDouble(p -> p.distSqr(pos)).thenComparingInt(Vec3i::getY));
	}

	public void ensureFeatureLoaded(ServerLevel level, BlockPos pos, int range) {
		var list = ChunkPos.rangeClosed(new ChunkPos(pos), Math.floorDiv(range, 16)).toList();
		CompletableFuture.runAsync(() -> list.forEach(cpos -> level.getChunk(cpos.x, cpos.z, ChunkStatus.FEATURES, true)),
				level.getServer());
	}

}
