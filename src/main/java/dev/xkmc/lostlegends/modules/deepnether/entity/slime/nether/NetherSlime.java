package dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether;

import dev.xkmc.lostlegends.modules.deepnether.entity.slime.base.BaseAbsorbingSlime;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseFireBlock;
import net.neoforged.neoforge.event.EventHooks;

public class NetherSlime extends BaseAbsorbingSlime {

	public NetherSlime(EntityType<? extends NetherSlime> type, Level level) {
		super(type, level);
	}

	protected boolean isValidTarget(LivingEntity e) {
		return e instanceof IronGolem || e instanceof Player;
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
	public boolean causeFallDamage(float dist, float factor, DamageSource source) {
		var target = getTarget();
		if (target != null && !(target instanceof Slime) && !target.fireImmune())
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
					int dist = Math.abs(x) + Math.abs(z);
					if (dist > 0 && getRandom().nextInt(dist) > 0) continue;
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
