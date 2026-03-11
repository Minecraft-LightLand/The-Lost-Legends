package dev.xkmc.lostlegends.modules.deepnether.entity.slime.animal;

import com.tterrag.registrate.providers.loot.RegistrateEntityLootTables;
import dev.xkmc.lostlegends.foundation.entity.slime.BaseSlime;
import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class BeeSlime extends AnimalSlime {

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.2F);
	}

	public static LootTable.Builder buildLoot(RegistrateEntityLootTables pvd) {
		return LootTable.lootTable().withPool(LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(Items.HONEYCOMB)
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(pvd.getRegistries(), UniformGenerator.between(0.0F, 1.0F)))
				));
	}

	public BeeSlime(EntityType<? extends BaseSlime> type, Level level) {
		super(type, level);
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getMainHandItem();
		if (stack.is(Items.GLASS_BOTTLE)) {
			if (getHealth() >= 4) {
				if (!level().isClientSide()) {
					setHealth(getHealth() - 2);
					if (!player.isCreative()) {
						stack.shrink(1);
					}
					player.getInventory().placeItemBackInInventory(Items.HONEY_BOTTLE.getDefaultInstance());
				}
				return InteractionResult.SUCCESS;
			}
		}
		if (stack.is(Items.BUCKET)) {
			if (getHealth() >= 12) {
				if (!level().isClientSide()) {
					setHealth(getHealth() - 8);
					player.getInventory().placeItemBackInInventory(Items.HONEY_BLOCK.getDefaultInstance());
				}
				return InteractionResult.SUCCESS;
			}
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public Ingredient getFoodIngredient() {
		return Ingredient.of(ItemTags.BEE_FOOD);
	}

	@Override
	public ItemStack getParticleStack() {
		return Items.HONEY_BLOCK.getDefaultInstance();
	}

	private static final ResourceLocation TEX = LostLegends.loc("textures/entity/deepnether/bee_slime.png");

	@Override
	public ResourceLocation getTexture() {
		return TEX;
	}

}
