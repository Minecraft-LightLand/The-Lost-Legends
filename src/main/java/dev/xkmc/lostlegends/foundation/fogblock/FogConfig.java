package dev.xkmc.lostlegends.foundation.fogblock;

import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.event.ViewportEvent;

public record FogConfig(
		Type type,
		float red, float green, float blue,
		float nearPlane, float farPlane, boolean immediate
) {

	public void lerpColor(ViewportEvent.ComputeFogColor event, float p) {
		event.setRed(Mth.lerp(p, event.getRed(), red()));
		event.setGreen(Mth.lerp(p, event.getGreen(), green()));
		event.setBlue(Mth.lerp(p, event.getBlue(), blue()));
	}

	public void lerpPlane(ViewportEvent.RenderFog event, float easing, boolean clear) {
		float perc = (float) ((1 - Math.exp(-easing * 5)) / (1 - Math.exp(-5)));
		var n0 = event.getNearPlaneDistance();
		var f0 = event.getFarPlaneDistance();
		float n1, f1;
		if (clear) {
			n1 = -8f;
			f1 = f0 * 0.5f;
		} else {
			n1 = nearPlane();
			f1 = farPlane();
		}
		event.setNearPlaneDistance(Mth.lerp(perc, n0, n1));
		event.setFarPlaneDistance(Mth.lerp(perc, f0, f1));
	}

	public enum Type {
		VIEWPORT,
		SURROUND
	}

}
