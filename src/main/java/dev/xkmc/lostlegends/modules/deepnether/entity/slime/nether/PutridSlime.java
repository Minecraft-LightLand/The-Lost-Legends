package dev.xkmc.lostlegends.modules.deepnether.entity.slime.nether;

import dev.xkmc.lostlegends.init.LLDamageTypes;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.entity.slime.base.BaseAbsorbingSlime;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.List;

public class PutridSlime extends BaseAbsorbingSlime {

	public PutridSlime(EntityType<? extends PutridSlime> type, Level level) {
		super(type, level);
	}

	protected boolean isValidTarget(LivingEntity e) {
		return e instanceof IronGolem || e instanceof Player;
	}

	@Override
	protected void setAttributes(int size) {
		super.setAttributes(size);
	}

	protected boolean mayAbsorbSlime(Slime n) {
		return getSize() >= n.getSize() && getHealth() >= n.getHealth();
	}

	protected boolean mayHunt(LivingEntity e) {
		return super.mayHunt(e) || e instanceof PutridSlime slime && slime.wantToAbsorb() && slime.mayAbsorbSlime(this);
	}

	@Override
	protected float getDamageAfterMagicAbsorb(DamageSource source, float amount) {
		if (!source.is(LLDamageTypes.NO_SLIME_SPLIT) && getSize() > 1) {
			amount = Math.min(amount, getMaxHealth() / 2);
			damageContainers.peek().setNewDamage(amount);
		}
		return super.getDamageAfterMagicAbsorb(source, amount);
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		if ((tickCount + getId() * 13) % 20 == 0 && wantToAbsorb() && isAlive()) {
			var list = level().getEntities(this, getBoundingBox().inflate(3), e -> e.getType() == getType());
			for (var e : list) {
				if (e instanceof PutridSlime slime && slime.isAlive()) {
					if (getBoundingBox().inflate(0.5).intersects(slime.getBoundingBox().inflate(0.5))) {
						if (mayAbsorbSlime(slime)) {
							absorbSlime(slime);
						}
					}
				}
			}
		}
	}

	@Override
	public void onDeathSplit(List<Mob> children) {
		int size = getSize();
		if (size <= 0) return;
		int n = children.size();
		float min = 1f * healthOfSize(size - 1) / n;
		for (var e : children) {
			if (e instanceof PutridSlime slime) {
				int x = slime.getSize();
				while (healthOfSize(x + 1) <= min) x++;
				slime.setSize(x, true);
			}
		}
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

	private static final ResourceLocation TEX = LostLegends.loc("textures/entity/deepnether/putrid_slime.png");

	@Override
	public ResourceLocation getTexture() {
		return TEX;
	}

}
