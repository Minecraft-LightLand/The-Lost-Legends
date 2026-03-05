package dev.xkmc.lostlegends.modules.deepnether.entity.flying.reaper;

import dev.xkmc.l2core.base.entity.SyncedData;
import dev.xkmc.lostlegends.foundation.entity.api.EntityUtils;
import dev.xkmc.lostlegends.foundation.entity.api.INoFriendlyFireEntity;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder.BeholderEntity;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import dev.xkmc.lostlegends.modules.spell.ai.MobSpellEntry;
import dev.xkmc.lostlegends.modules.spell.ai.MobSpellPool;
import dev.xkmc.lostlegends.modules.spell.mob.reaper.ReaperBombSpell;
import dev.xkmc.lostlegends.modules.spell.mob.reaper.ReaperBurstSpell;
import dev.xkmc.lostlegends.modules.spell.mob.reaper.ReaperShootSpell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReaperEntity extends BeholderEntity implements INoFriendlyFireEntity {

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 100)
				.add(Attributes.FOLLOW_RANGE, 35)
				.add(Attributes.MOVEMENT_SPEED, 0.10F)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}

	private static <T> EntityDataAccessor<T> define(EntityDataSerializer<T> e) {
		return SynchedEntityData.defineId(ReaperEntity.class, e);
	}

	protected static final SyncedData DATA = new SyncedData(ReaperEntity::define);

	protected static final EntityDataAccessor<Float> LEFT_HEAD = DATA.define(EntityUtils.FLOAT, 0f, "left_head");
	protected static final EntityDataAccessor<Float> RIGHT_HEAD = DATA.define(EntityUtils.FLOAT, 0f, "right_head");

	private static final MobSpellPool<ReaperEntity> POOL = new MobSpellPool<>(List.of(
			new MobSpellEntry<>(ReaperBurstSpell.SPELL, e -> true, 100, 40),
			new MobSpellEntry<>(ReaperBombSpell.SPELL, e -> true, 100, 30),
			new MobSpellEntry<>(ReaperShootSpell.SPELL, e -> true, 100, 20)
	));

	public ReaperEntity(EntityType<? extends BeholderEntity> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		DATA.register(builder);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		DATA.write(registryAccess(), tag, entityData);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		DATA.read(registryAccess(), tag, entityData);
	}

	@Nullable
	@Override
	public MobSpellEntry<? extends ReaperEntity> getSpell() {
		return POOL.poll(this);
	}

	@Override
	public boolean noFriendlyFireTo(LivingEntity target) {
		if (target instanceof Mob mob && mob.getTarget() == this) return false;
		if (getTarget() == target) return false;
		return target.isAlliedTo(this) || target instanceof BeholderEntity || target instanceof Slime;
	}

	@Override
	protected void actuallyHurt(DamageSource source, float amount) {
		float hp = getHealth();
		super.actuallyHurt(source, amount);
		float dhp = hp - getHealth();
		if (isAlive() && dhp > 1) {
			var src = source.getSourcePosition();
			if (src == null) {
				var e = source.getEntity();
				if (e == null) return;
				src = e.position();
			}
			var look = getLookAngle();
			var diff = src.subtract(position());
			var val = look.x * diff.z < look.z * diff.x ? LEFT_HEAD : RIGHT_HEAD;
			var progress = entityData.get(val) + amount / getMaxHealth() * 5;
			if (progress > 1) {
				var e = new BeholderEntity(DeepNether.ENTITY.BEHOLDER.get(), level());
				e.setPos(position().add(diff.normalize()));
				e.setTarget(getTarget());
				level().addFreshEntity(e);
				progress = 0;
			}
			entityData.set(val, progress);
		}
	}

	public float getLeftScale() {
		return entityData.get(LEFT_HEAD);
	}

	public float getRightScale() {
		return entityData.get(RIGHT_HEAD);
	}

	public static final ResourceLocation TEX = LostLegends.loc("textures/entity/deepnether/reaper/reaper.png");

	@Override
	public ResourceLocation getTexture() {
		return TEX;
	}

}
