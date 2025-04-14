package dev.xkmc.lostlegends.foundation.event;

import dev.xkmc.lostlegends.foundation.fogblock.IFogBlock;
import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
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

	@SubscribeEvent
	public static void onFogColor(ViewportEvent.ComputeFogColor event) {
		var cam = event.getCamera();
		var block = getFog(cam, cam.getEntity().level());
		if (block == null) return;
		var config = block.getFogConfig();
		event.setRed(config.red());
		event.setGreen(config.green());
		event.setBlue(config.blue());
	}

	@SubscribeEvent
	public static void onFogSetup(ViewportEvent.RenderFog event) {
		var cam = event.getCamera();
		var block = getFog(cam, cam.getEntity().level());
		if (block == null) return;
		if (block.isClear(event.getCamera().getEntity())) {
			event.setNearPlaneDistance(-8f);
		} else {
			event.setNearPlaneDistance(0);
			event.setFarPlaneDistance(2);
		}
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
			if (blockstate.getBlock() instanceof IFogBlock fog)
				return fog;
		}
		return null;
	}

}
