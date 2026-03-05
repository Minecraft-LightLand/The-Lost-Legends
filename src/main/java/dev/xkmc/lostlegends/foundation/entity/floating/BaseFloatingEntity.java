package dev.xkmc.lostlegends.foundation.entity.floating;

import dev.xkmc.lostlegends.foundation.entity.flying.FlyerLookAtEntityGoal;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class BaseFloatingEntity extends FlyingMob {

	protected List<MotionBlockGoals> motionGoals = new ArrayList<>();

	protected BaseFloatingEntity(EntityType<? extends BaseFloatingEntity> type, Level level) {
		super(type, level);
		this.moveControl = new FloaterMoveControl(this);
	}

	@Override
	protected void registerGoals() {
		motionGoals = new ArrayList<>();
		this.goalSelector.addGoal(7, new RandomFloatAroundGoal(this));
		this.goalSelector.addGoal(9, new FlyerLookAtEntityGoal(this, Player.class, 16));
		this.goalSelector.addGoal(12, new RandomLookAroundGoal(this));
	}

	@Override
	public SoundSource getSoundSource() {
		return SoundSource.HOSTILE;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource p_32750_) {
		return SoundEvents.HOSTILE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.HOSTILE_DEATH;
	}

	@Override
	public void travel(Vec3 vec3) {
		for (var e : motionGoals) {
			if (e.isImmobile()) {
				vec3 = Vec3.ZERO;
				break;
			}
		}
		super.travel(vec3);
	}

}
