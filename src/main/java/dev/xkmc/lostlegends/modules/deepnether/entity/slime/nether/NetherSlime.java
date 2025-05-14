package dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseFireBlock;
import net.neoforged.neoforge.event.EventHooks;

public class NetherSlime extends BaseNetherSlime {

	private static int MAX_SIZE = 6;

	public NetherSlime(EntityType<? extends NetherSlime> type, Level level) {
		super(type, level);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(
				this, Slime.class, 10, true, false,
				this::mayAbsorb));
	}

	protected boolean mayAbsorb(LivingEntity e) {
		return e instanceof Slime n && getSize() * 0.6 > n.getSize() && (getHealth() < getMaxHealth() || getSize() < MAX_SIZE);
	}

	protected boolean isValidTarget(LivingEntity e) {
		return e instanceof IronGolem || e instanceof Player;
	}

	@Override
	protected void dealDamage(LivingEntity le) {
		if (le instanceof Slime e) {
			float health = e.getHealth();
			e.discard();
			if (getMaxHealth() - getHealth() < health && getSize() < MAX_SIZE) {
				setSize(getSize() + 1, false);
			}
			heal(health);
			return;
		}
		super.dealDamage(le);
	}

	protected void postHurt(LivingEntity le) {
		int size = getSize();
		double flameRate = size * 0.25f;
		double explosionRate = (size - 3) * 0.25f;
		if (getRandom().nextFloat() < flameRate && !le.fireImmune()) {
			le.igniteForTicks(size * 20);
		}
		if (getRandom().nextFloat() < explosionRate) {
			level().explode(this, le.getX(), le.getY(), le.getZ(), 1, Level.ExplosionInteraction.MOB);
		}
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return super.isInvulnerableTo(source) || source.is(DamageTypeTags.IS_EXPLOSION);
	}

	@Override
	public void setSize(int size, boolean heal) {
		super.setSize(size, heal);
		int i = Mth.clamp(size, 1, 127);
		this.reapplyPosition();
		this.refreshDimensions();
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(i * Math.max(i, 4));
		this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2F + 0.1F * (float) i);
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(i * 2);
		this.getAttribute(Attributes.ARMOR).setBaseValue(size * 3);
		if (heal) {
			this.setHealth(this.getMaxHealth());
		}
		this.xpReward = i * 4;
	}

	@Override
	public boolean causeFallDamage(float dist, float factor, DamageSource source) {
		setFire();
		return super.causeFallDamage(dist, factor, source);
	}

	protected void setFire() {
		if (!EventHooks.canEntityGrief(level(), this)) return;
		float r = (getSize() - 1) * 0.4f;
		int ir = (int) Math.floor(r);
		for (int x = -ir; x <= ir; x++) {
			for (int z = -ir; z <= ir; z++) {
				if (x * x + z * z < r * r) {
					var pos = blockPosition().offset(x, 0, z);
					var below = pos.below();
					if (level().isEmptyBlock(pos) && level().getBlockState(below).isFaceSturdy(level(), below, Direction.UP)) {
						level().setBlockAndUpdate(pos, BaseFireBlock.getState(this.level(), pos));
					}
				}
			}
		}
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
	}

	@Override
	protected void decreaseSquish() {
		this.targetSquish *= 0.9F;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.2F);
	}

	public static boolean checkMagmaCubeSpawnRules(
			EntityType<? extends Slime> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource rand
	) {
		return level.getDifficulty() != Difficulty.PEACEFUL;
	}

}
