package dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder;

import dev.xkmc.lostlegends.foundation.entity.floating.BaseFloatingEntity;
import dev.xkmc.lostlegends.foundation.entity.flying.FlyerAttackGoal;
import dev.xkmc.lostlegends.foundation.entity.flying.FlyerHurtByTargetGoal;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.spell.ai.MobSpellEntry;
import dev.xkmc.lostlegends.modules.spell.mob.beholder.BeholderSpell;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

public class BeholderEntity extends BaseFloatingEntity {

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 20)
				.add(Attributes.FOLLOW_RANGE, 35)
				.add(Attributes.MOVEMENT_SPEED, 0.10F)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}

	public static boolean checkSpawnRules(
			EntityType<? extends BeholderEntity> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource r
	) {
		return level.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(type, level, spawnType, pos, r);
	}

	public final BeholderStates states = new BeholderStates(this);

	public BeholderEntity(EntityType<? extends BeholderEntity> type, Level level) {
		super(type, level);
	}

	@Override
	public void aiStep() {
		states.tick();
		super.aiStep();
	}

	protected BeholderAttackGoal createAttackGoal() {
		return new BeholderAttackGoal(this, 1, 20, 16);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		var atk = createAttackGoal();
		motionGoals.add(atk);
		this.goalSelector.addGoal(3, atk);
		this.targetSelector.addGoal(1, new FlyerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new FlyerAttackGoal<>(
				this, Player.class, 10, true, false,
				e -> Math.abs(e.getY() - this.getY()) <= 4.0));
		this.targetSelector.addGoal(3, new FlyerAttackGoal<>(
				this, IronGolem.class, true));
	}

	private static final MobSpellEntry<BeholderEntity> SPELL = new MobSpellEntry<>(BeholderSpell.SPELL, e -> true, 100, 20);

	@Nullable
	public MobSpellEntry<? extends BeholderEntity> getSpell() {
		return SPELL;
	}

	public static final ResourceLocation TEX = LostLegends.loc("textures/entity/deepnether/beholder/beholder.png");

	public ResourceLocation getTexture() {
		return TEX;
	}

}
