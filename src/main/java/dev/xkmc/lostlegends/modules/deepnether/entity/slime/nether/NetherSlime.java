package dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class NetherSlime extends BaseNetherSlime {

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

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.2F);
	}

	public static boolean checkMagmaCubeSpawnRules(
			EntityType<? extends Slime> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource rand
	) {
		return level.getDifficulty() != Difficulty.PEACEFUL;
	}

}
