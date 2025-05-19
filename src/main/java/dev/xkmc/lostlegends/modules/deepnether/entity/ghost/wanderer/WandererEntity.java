package dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class WandererEntity extends Monster {

	public final AnimationState idle = new AnimationState();
	public final AnimationState breath = new AnimationState();
	public final AnimationState attack = new AnimationState();
	public final AnimationState jump = new AnimationState();
	public final AnimationState hug = new AnimationState();

	public final WandererStateMachine state = new WandererStateMachine();

	public WandererEntity(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		idle.start(tickCount);
		breath.start(tickCount);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.addBehaviourGoals();
	}

	protected void addBehaviourGoals() {
		this.goalSelector.addGoal(2, new WandererAttackGoal(this, 1.0, false));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(WandererEntity.class));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
	}

	public float jumpAttackChance(LivingEntity target) {
		return 1 - getHealth() / getMaxHealth();
	}

	@Override
	protected void customServerAiStep() {
		state.tick(this);
		super.customServerAiStep();
	}

	@Override
	public void handleEntityEvent(byte id) {
		switch (id) {
			case WandererIds.IDLE:
				jump.stop();
				hug.stop();
				attack.stop();
				return;
			case WandererIds.ATTACK:
				jump.stop();
				hug.stop();
				attack.startIfStopped(tickCount);
				return;
			case WandererIds.JUMP:
				attack.stop();
				hug.stop();
				jump.startIfStopped(tickCount);
				return;
			case WandererIds.HUG:
				attack.stop();
				jump.stop();
				hug.startIfStopped(tickCount);
				return;
		}
		super.handleEntityEvent(id);
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance ins, MobSpawnType spawnType, @Nullable SpawnGroupData data) {
		int diff = ins.getDifficulty().getId();
		getAttribute(Attributes.ARMOR).setBaseValue(level.getRandom().nextInt(2 + diff * 2, 2 + diff * 4));
		return super.finalizeSpawn(level, ins, spawnType, data);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 25)
				.add(Attributes.FOLLOW_RANGE, 35)
				.add(Attributes.MOVEMENT_SPEED, 0.23F)
				.add(Attributes.ATTACK_DAMAGE, 6)
				.add(Attributes.ARMOR, 4)
				.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
	}


}
