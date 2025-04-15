package dev.xkmc.lostlegends.foundation.fogblock;

public record FogConfig(
		Type type,
		float red, float green, float blue,
		float nearPlane, float farPlane, boolean immediate
) {

	public enum Type {
		VIEWPORT,
		SURROUND
	}

}
