package dev.xkmc.lostlegends.foundation.fogblock;

import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ClientFogBlockHandler {

	private static int searchPrevTick = 0;
	private static IFogBlock searchCache = null;

	private static final int MAX_EASING = 20;
	private static float easingPrevTick = 0;
	private static double easing = 0, altEasing;
	private static IFogBlock easingCache = null, altEasingCache = null;

	public static void onFogColor(ViewportEvent.ComputeFogColor event) {
		if (easingCache == null) return;
		if (altEasingCache != null) {
			altEasingCache.getFogConfig().lerpColor(event, (float) (altEasing / MAX_EASING));
		}
		easingCache.getFogConfig().lerpColor(event, (float) (easing / MAX_EASING));
	}

	private static void checkEasing(Camera cam) {
		float easingTick = cam.getEntity().tickCount + cam.getPartialTickTime();
		float diffTick = easingTick - easingPrevTick;
		easingPrevTick = easingTick;
		if (diffTick < 0) {
			easingCache = null;
			easing = 0;
			altEasingCache = null;
			altEasing = 0;
			return;
		}

		var block = getFogBlock(cam, cam.getEntity().level());
		FogConfig config;
		if (block == null) {
			if (easingCache == null) {
				return;
			}
			block = easingCache;
			config = block.getFogConfig();
			if (config.immediate()) {
				easing = 0;
				easingCache = null;
				altEasing = 0;
				altEasingCache = null;
			} else {
				if (easing > 0) easing -= diffTick;
				if (easing <= 0) {
					easing = 0;
					easingCache = null;
				}
				if (altEasing > 0) altEasing -= diffTick;
				if (altEasing <= 0) {
					altEasing = 0;
					altEasingCache = null;
				}
				if (altEasing > 0 && easing == 0) {
					easingCache = altEasingCache;
					easing = altEasing;
					altEasingCache = null;
					altEasing = 0;
				}
			}
		} else {
			config = block.getFogConfig();
			if (config.immediate() || easingCache != null && easingCache.getFogConfig().immediate()) {
				easing = MAX_EASING;
				easingCache = block;
				altEasing = 0;
				altEasingCache = null;
			} else {
				if (easingCache != null && easingCache != block) {
					if (easingCache != altEasingCache && !easingCache.getFogConfig().immediate()) {
						altEasingCache = easingCache;
						altEasing = easing;
					}
					easing = 0;
				}
				easingCache = block;
				if (easing < MAX_EASING)
					easing += diffTick;
				if (easing > MAX_EASING)
					easing = MAX_EASING;
				if (altEasing > 0) altEasing -= diffTick;
				if (altEasing <= 0) {
					altEasing = 0;
					altEasingCache = null;
				}
			}
		}
	}

	public static void onFogSetup(ViewportEvent.RenderFog event) {
		checkEasing(event.getCamera());
		var e = event.getCamera().getEntity();
		if (easingCache == null) return;
		if (altEasingCache != null) {
			altEasingCache.getFogConfig().lerpPlane(event, (float) (altEasing / MAX_EASING), altEasingCache.isClear(e));
		}
		easingCache.getFogConfig().lerpPlane(event, (float) (easing / MAX_EASING), easingCache.isClear(e));
		event.setCanceled(true);
	}

	@Nullable
	public static IFogBlock getFogBlock(Camera self, Level level) {
		Camera.NearPlane camera$nearplane = self.getNearPlane();
		for (Vec3 vec3 : Arrays.asList(
				camera$nearplane.forward,
				camera$nearplane.getTopLeft(),
				camera$nearplane.getTopRight(),
				camera$nearplane.getBottomLeft(),
				camera$nearplane.getBottomRight()
		)) {
			Vec3 vec31 = self.getPosition().add(vec3);
			BlockPos blockpos = BlockPos.containing(vec31);
			BlockState blockstate = level.getBlockState(blockpos);
			if (blockstate.getBlock() instanceof IFogBlock fog && fog.getFogConfig().type() == FogConfig.Type.VIEWPORT)
				return fog;
		}
		if (searchPrevTick == self.getEntity().tickCount) {
			return searchCache;
		}
		searchCache = null;
		searchPrevTick = self.getEntity().tickCount;
		var pos = new BlockPos.MutableBlockPos();
		var cen = self.getEntity().getEyePosition();
		var origin = BlockPos.containing(cen);
		var minDist = Double.MAX_VALUE;
		int r = 2;
		for (int x = -r; x <= r; x++) {
			for (int y = -r; y <= r; y++) {
				for (int z = -r; z <= r; z++) {
					pos.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
					var state = level.getBlockState(pos);
					if (state.getBlock() instanceof IFogBlock fog && fog.getFogConfig().type() == FogConfig.Type.SURROUND) {
						double distSqr = pos.getCenter().distanceToSqr(cen);
						if (minDist < distSqr) continue;
						searchCache = fog;
						minDist = distSqr;
					}
				}
			}
		}
		return searchCache;
	}

}
