package dev.xkmc.lostlegends.modules.deepnether.entity.slime.piglin;

import dev.xkmc.l2core.init.reg.ench.EnchHelper;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.entity.slime.base.BaseSlime;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.List;

public class PigSlime extends BaseSlime {

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.2F);
	}

	public PigSlime(EntityType<? extends BaseSlime> type, Level level) {
		super(type, level);
	}

	@Override
	protected int healthOfSize(int size) {
		return size * size * size;
	}

	@Override
	protected int damageOfSize(int size) {
		return size;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
	}

	@Override
	protected boolean isValidTarget(LivingEntity e) {
		return false;
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.is(ItemTags.PIG_FOOD)) {
			if (getHealth() < getMaxHealth()) {
				heal(1);
			} else if (getSize() < 8) {
				setSize(getSize() + 1, false);
				return InteractionResult.SUCCESS;
			} else return super.mobInteract(player, hand);
			if (level() instanceof ServerLevel sl) {
				stack.shrink(1);
				float w = this.getDimensions(this.getPose()).width();
				float h = this.getDimensions(this.getPose()).height();
				float n = w * 2;
				for (int i = 0; i < n * 8; i++) {
					float a = this.random.nextFloat() * (float) (Math.PI * 2);
					float r = this.random.nextFloat() * 0.25F + 0.5F;
					double x = getX() + Mth.sin(a) * w * r;
					double y = getY() + this.random.nextFloat() * h;
					double z = getZ() + Mth.cos(a) * w * r;
					sl.sendParticles(ParticleTypes.COMPOSTER, x, y, z, 0, 0.0, 0.0, 0.0, 0);
				}
				level().playSound(null, blockPosition(), SoundEvents.COMPOSTER_FILL, SoundSource.NEUTRAL, 1, 1);
			}
			return InteractionResult.SUCCESS;
		}
		return super.mobInteract(player, hand);
	}

	@Override
	protected ParticleOptions getParticleType() {
		return new ItemParticleOption(ParticleTypes.ITEM, Items.PORKCHOP.getDefaultInstance());
	}

	private static final ResourceLocation TEX = LostLegends.loc("textures/entity/deepnether/pigslime.png");

	@Override
	public ResourceLocation getTexture() {
		return TEX;
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return false;
	}

	@Override
	public void onDeathSplit(List<Mob> children) {
		int factor = 0;
		if (children.size() > 1) {
			children.remove(random.nextInt(children.size()));
			factor++;
		}
		if (children.size() > 2) {
			children.remove(random.nextInt(children.size()));
			factor++;
		}
		if (factor == 0) return;
		var box = getBoundingBox();
		int ench = 0;
		if (getLastDamageSource() != null) {
			var killer = getLastDamageSource().getDirectEntity();
			if (killer instanceof LivingEntity le) {
				ItemStack weapon = le.getMainHandItem();
				if (!weapon.isEmpty()) {
					ench = EnchHelper.getLv(le.getMainHandItem(), Enchantments.LOOTING);
				}
			}
		}
		double rate = 0.05 + 0.02 * random.nextFloat() * ench;
		double vol = box.getXsize() * box.getYsize() * box.getZsize() * rate * factor;
		int count = (int) vol;
		if (random.nextFloat() < vol - count) {
			count++;
		}
		if (count <= 0) return;
		ItemStack drop = new ItemStack(isOnFire() ? Items.COOKED_PORKCHOP : Items.PORKCHOP, count);
		spawnAtLocation(drop);
	}

}
