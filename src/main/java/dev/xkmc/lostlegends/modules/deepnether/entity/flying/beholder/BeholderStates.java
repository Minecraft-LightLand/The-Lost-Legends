package dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder;

import net.minecraft.world.entity.AnimationState;

public class BeholderStates {

	public final AnimationState idle = new AnimationState();

	private final BeholderEntity e;

	public BeholderStates(BeholderEntity e) {
		this.e = e;
	}

	public void tick() {
		idle.startIfStopped(e.tickCount);
	}

}
