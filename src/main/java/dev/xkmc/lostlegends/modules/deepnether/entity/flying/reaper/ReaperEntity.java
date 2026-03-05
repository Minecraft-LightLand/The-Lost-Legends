package dev.xkmc.lostlegends.modules.deepnether.entity.flying.reaper;

import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.lostlegends.foundation.entity.api.INoFriendlyFireEntity;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.entity.flying.beholder.BeholderEntity;
import dev.xkmc.lostlegends.modules.spell.mob.reaper.ReaperBombSpell;
import dev.xkmc.lostlegends.modules.spell.mob.reaper.ReaperBurstSpell;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

public class ReaperEntity extends BeholderEntity implements INoFriendlyFireEntity {

	private ResourceKey<SpellAction> spell = ReaperBurstSpell.SPELL;
	private int delay = 40;

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 100)
				.add(Attributes.FOLLOW_RANGE, 35)
				.add(Attributes.MOVEMENT_SPEED, 0.10F)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}

	public ReaperEntity(EntityType<? extends BeholderEntity> type, Level level) {
		super(type, level);
	}

	@Override
	protected void afterInvokeSpell() {
		if (random.nextBoolean()) {
			spell = ReaperBurstSpell.SPELL;
			delay = 40;
		} else {
			spell = ReaperBombSpell.SPELL;
			delay = 30;
		}
	}

	@Override
	public ResourceKey<SpellAction> getSpell() {
		return spell;
	}

	@Override
	public int spellDuration() {
		return delay;
	}

	@Override
	public boolean noFriendlyFireTo(LivingEntity target) {
		if (target instanceof Mob mob && mob.getTarget() == this) return false;
		if (getTarget() == target) return false;
		return target.isAlliedTo(this) || target instanceof BeholderEntity || target instanceof Slime;
	}

	public static final ResourceLocation TEX = LostLegends.loc("textures/entity/deepnether/reaper/reaper.png");

	@Override
	public ResourceLocation getTexture() {
		return TEX;
	}

}
