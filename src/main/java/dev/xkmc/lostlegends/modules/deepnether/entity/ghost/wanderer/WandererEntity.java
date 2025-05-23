package dev.xkmc.lostlegends.modules.deepnether.entity.ghost.wanderer;

import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeWrapper;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.lostlegends.foundation.entity.DamageModifierEntity;
import dev.xkmc.lostlegends.modules.deepnether.entity.ghost.base.BaseGhostEntity;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class WandererEntity extends BaseGhostEntity implements DamageModifierEntity {

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

	protected void addBehaviourGoals() {
		super.addBehaviourGoals();
		this.goalSelector.addGoal(2, new WandererAttackGoal(this, 1.0, true));
	}

	public float jumpAttackChance(LivingEntity target) {
		return (1 - getHealth() / getMaxHealth()) * WandererConstants.jumpMaxChance();
	}

	@Override
	protected void customServerAiStep() {
		state.serverTick(this);
		super.customServerAiStep();
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (level().isClientSide()) {
			state.clientTick(this);
		}
	}

	@Override
	protected float getDamageAfterMagicAbsorb(DamageSource source, float amount) {
		amount = WandererConstants.modifyDamage(source, amount);
		damageContainers.peek().setNewDamage(amount);
		return super.getDamageAfterMagicAbsorb(source, amount);
	}

	@Override
	protected void actuallyHurt(DamageSource source, float amount) {
		super.actuallyHurt(source, amount);
		state.resetAttackMode();
	}

	@Override
	public void modify(CreateSourceEvent event, DamageTypeWrapper res) {
		if (res.validState(DefaultDamageState.BYPASS_ARMOR)) {
			event.enable(DefaultDamageState.BYPASS_ARMOR);
		}
	}

	protected AABB getAttackBoundingBox() {
		var ans = super.getAttackBoundingBox();
		if (onGround()) return ans;
		return ans.inflate(0, 0.8f, 0);
	}

	public boolean isWithinMeleeAttackRange(LivingEntity e, float buffer) {
		return this.getAttackBoundingBox().intersects(e.getHitbox().inflate(buffer));
	}

	@Override
	public void handleEntityEvent(byte id) {
		state.onEntityEvent(this, id);
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
				.add(Attributes.MAX_HEALTH, 26)
				.add(Attributes.FOLLOW_RANGE, 35)
				.add(Attributes.MOVEMENT_SPEED, 0.26F)
				.add(Attributes.ATTACK_DAMAGE, 6)
				.add(Attributes.ARMOR, 4)
				.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
	}

}
