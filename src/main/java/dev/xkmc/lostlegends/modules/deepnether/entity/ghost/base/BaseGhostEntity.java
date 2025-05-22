package dev.xkmc.lostlegends.modules.deepnether.entity.ghost.base;

import dev.xkmc.lostlegends.foundation.entity.LavaSwimEntity;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;

public class BaseGhostEntity extends Monster implements LavaSwimEntity {

	protected BaseGhostEntity(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		navigation = new GhostNavigation(this, level);
		setPathfindingMalus(PathType.LAVA, 0);
		setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
		setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(3, new GhostSwimGoal(this));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.addBehaviourGoals();
	}

	protected void addBehaviourGoals() {
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return super.isInvulnerableTo(source) || source.is(DamageTypeTags.IS_FALL);
	}

	@Override
	public boolean canStandOnFluid(FluidState state) {
		var type = state.getFluidType();
		return type == NeoForgeMod.LAVA_TYPE.value() || type == DeepNether.BLOCKS.LIQUID_SOUL.getType();
	}

	@Override
	public boolean canSwimInFluidType(FluidType type) {
		return type == NeoForgeMod.LAVA_TYPE.value() || type == DeepNether.BLOCKS.LIQUID_SOUL.getType() ||
				super.canSwimInFluidType(type);
	}

	@Override
	public float getLavaSwimFactor() {
		return 3;
	}

	@Override
	public boolean canDrownInFluidType(FluidType type) {
		return false;
	}

}
