package dev.xkmc.lostlegends.modules.deepnether.entity.flying.floating;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class FloaterMoveControl extends MoveControl {

	private final BaseFloatingEntity entity;
	private int floatDuration;

	protected float strafeUp;

	public FloaterMoveControl(BaseFloatingEntity e) {
		super(e);
		entity = e;
	}

	@Override
	public void tick() {
		float attr = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
		float speed = (float) this.speedModifier * attr;
		if (operation == MoveControl.Operation.MOVE_TO) {
			double dx = wantedX - mob.getX();
			double dz = wantedZ - mob.getZ();
			double dy = wantedY - mob.getY();
			Vec3 dir = new Vec3(dx, dy, dz);
			double d0 = dir.length();
			dir = dir.normalize();

			float yrot = (float) (Mth.atan2(dz, dx) * 180 / Math.PI) - 90;
			mob.setYRot(rotlerp(mob.getYRot(), yrot, 30));
			mob.yBodyRot = mob.getYRot();

			if (floatDuration-- > 0) return;
			floatDuration = floatDuration + entity.getRandom().nextInt(5) + 2;
			if (canReach(dir, Mth.ceil(d0))) {
				entity.setDeltaMovement(entity.getDeltaMovement().add(dir.scale(speed)));
			} else {
				operation = MoveControl.Operation.WAIT;
			}
		} else if (operation == Operation.STRAFE) {
			this.mob.setSpeed(speed);
			this.mob.setZza(this.strafeForwards);
			this.mob.setXxa(this.strafeRight);
			this.mob.setYya(this.strafeUp);
			this.operation = MoveControl.Operation.WAIT;
		}
	}

	public void strafe(float x, float z, float y) {
		strafe(x, z);
		strafeUp = y;
	}

	private boolean canReach(Vec3 offset, int step) {
		AABB aabb = entity.getBoundingBox();

		for (int i = 1; i < step; i++) {
			aabb = aabb.move(offset);
			if (!entity.level().noCollision(entity, aabb)) {
				return false;
			}
		}
		return true;
	}

	public void stop() {
		operation = Operation.WAIT;
	}

}