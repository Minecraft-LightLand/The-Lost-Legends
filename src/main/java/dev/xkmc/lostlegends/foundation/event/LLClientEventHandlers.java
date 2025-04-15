package dev.xkmc.lostlegends.foundation.event;

import dev.xkmc.lostlegends.foundation.fogblock.FogConfig;
import dev.xkmc.lostlegends.foundation.fogblock.IFogBlock;
import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@EventBusSubscriber(value = Dist.CLIENT, modid = LostLegends.MODID, bus = EventBusSubscriber.Bus.GAME)
public class LLClientEventHandlers {

	private static final int MAX_EASING = 20;
	private static float easingPrevTick = 0;
	private static double easing = 0;
	private static IFogBlock easingCache = null;

	private static int searchPrevTick = 0;
	private static IFogBlock searchCache = null;

	@SubscribeEvent
	public static void onFogColor(ViewportEvent.ComputeFogColor event) {
		var block = easingCache;
		if (block == null) return;
		var config = block.getFogConfig();
		float p = (float) (easing / MAX_EASING);
		event.setRed(Mth.lerp(p, event.getRed(), config.red()));
		event.setGreen(Mth.lerp(p, event.getGreen(), config.green()));
		event.setBlue(Mth.lerp(p, event.getBlue(), config.blue()));
	}

	@SubscribeEvent
	public static void onFogSetup(ViewportEvent.RenderFog event) {
		var cam = event.getCamera();
		float easingTick = cam.getEntity().tickCount + cam.getPartialTickTime();
		float diffTick = easingTick - easingPrevTick;
		easingPrevTick = easingTick;
		if (diffTick < 0) {
			easingCache = null;
			easing = 0;
			return;
		}
		var block = getFog(cam, cam.getEntity().level());
		FogConfig config;
		if (block == null) {
			if (easingCache == null)
				return;
			block = easingCache;
			config = block.getFogConfig();
			if (config.immediate()) {
				easing = 0;
				easingCache = null;
				return;
			} else {
				if (easing > 0) easing -= diffTick;
				if (easing <= 0) {
					easing = 0;
					easingCache = null;
					return;
				}
			}
		} else {
			config = block.getFogConfig();
			easingCache = block;
			if (config.immediate()) {
				easing = MAX_EASING;
			} else {
				if (easing < MAX_EASING)
					easing += diffTick;
				if (easing > MAX_EASING)
					easing = MAX_EASING;
			}
		}
		var n0 = event.getNearPlaneDistance();
		var f0 = event.getFarPlaneDistance();
		float n1, f1;
		if (block.isClear(event.getCamera().getEntity())) {
			n1 = -8f;
			f1 = f0 * 0.5f;
		} else {
			n1 = config.nearPlane();
			f1 = config.farPlane();
		}
		float p = (float) (easing / MAX_EASING);
		p = (float) ((1 - Math.exp(-p * 5)) / (1 - Math.exp(-5)));
		event.setNearPlaneDistance(Mth.lerp(p, n0, n1));
		event.setFarPlaneDistance(Mth.lerp(p, f0, f1));
		event.setCanceled(true);
	}

	@Nullable
	public static IFogBlock getFog(Camera self, Level level) {
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
		var origin = BlockPos.containing(self.getEntity().getEyePosition());
		for (int x = -2; x <= 2; x++) {
			for (int y = -2; y <= 2; y++) {
				for (int z = -2; z <= 2; z++) {
					pos.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
					var state = level.getBlockState(pos);
					if (state.getBlock() instanceof IFogBlock fog && fog.getFogConfig().type() == FogConfig.Type.SURROUND) {
						searchCache = fog;
						return fog;
					}
				}
			}
		}
		return null;
	}

}
