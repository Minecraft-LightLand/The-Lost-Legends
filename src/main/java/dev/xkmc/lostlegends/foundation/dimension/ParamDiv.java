package dev.xkmc.lostlegends.foundation.dimension;

import net.minecraft.world.level.biome.Climate;

public class ParamDiv {

	public static ParamDiv positive(float threshold) {
		return new ParamDiv(true, false, false, threshold);
	}

	public static ParamDiv trinary(float threshold) {
		return new ParamDiv(false, true, false, threshold);
	}

	public static ParamDiv polar() {
		return new ParamDiv(false, false, false, 0);
	}

	public static ParamDiv point() {
		return new ParamDiv(false, true, true, 0);
	}

	private final boolean positive;
	private final boolean trinary;
	private final boolean point;
	private final float threshold;

	private ParamDiv(boolean positive, boolean trinary, boolean point, float threshold) {
		this.positive = positive;
		this.trinary = trinary;
		this.point = point;
		this.threshold = threshold;
	}

	public Climate.Parameter get(int sign) {
		if (point) {
			float v = sign == 0 ? 0 : sign > 0 ? 0.5f : -0.5f;
			return span(v, v);
		}
		if (positive) {
			return sign == 0 ? span(0, threshold - 0.05f) : span(threshold + 0.05f, 1);
		}
		if (!trinary) {
			return sign < 0 ? span(-1, -0.05f) : span(0.05f, 1);
		}
		if (sign == 0) {
			return span(-threshold + 0.05f, threshold - 0.05f);
		}
		return sign < 0 ? span(-1, -threshold - 0.05f) : span(threshold + 0.05f, 1);
	}

	public Climate.Parameter not(int sign) {
		return sign > 0 ? span(-1, threshold - 0.05f) : span(-threshold + 0.05f, 1);
	}

	public Climate.Parameter all() {
		return positive ? span(0, 1) : span(-1, 1);
	}

	public Climate.Parameter tip(float tip) {
		if (positive) {
			return tip > 0.5 ? span(tip, 1) : span(0, tip);
		}
		return tip > 0 ? span(tip, 1) : span(-1, tip);
	}

	public static Climate.Parameter span(float min, float max) {
		if (min > max) {
			return new Climate.Parameter(Climate.quantizeCoord(max), Climate.quantizeCoord(min));
		} else {
			return new Climate.Parameter(Climate.quantizeCoord(min), Climate.quantizeCoord(max));
		}
	}

}
