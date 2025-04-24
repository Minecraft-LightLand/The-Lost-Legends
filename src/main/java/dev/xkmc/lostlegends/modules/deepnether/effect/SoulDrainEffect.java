package dev.xkmc.lostlegends.modules.deepnether.effect;

import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.NeoForgeMod;

public class SoulDrainEffect extends MobEffect {

	public SoulDrainEffect(MobEffectCategory category, int color) {
		super(category, color);
		var id = LostLegends.loc("soul_drain");
		addAttributeModifier(Attributes.MOVEMENT_SPEED, id, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		addAttributeModifier(NeoForgeMod.SWIM_SPEED, id, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		addAttributeModifier(Attributes.ATTACK_SPEED, id, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		addAttributeModifier(Attributes.JUMP_STRENGTH, id, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, id, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		addAttributeModifier(Attributes.SUBMERGED_MINING_SPEED, id, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		addAttributeModifier(Attributes.BLOCK_INTERACTION_RANGE, id, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, id, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

}
