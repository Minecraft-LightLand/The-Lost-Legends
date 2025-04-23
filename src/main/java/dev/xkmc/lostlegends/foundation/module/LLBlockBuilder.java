package dev.xkmc.lostlegends.foundation.module;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class LLBlockBuilder<T extends Block> {

	private final BlockBuilder<T, L2Registrate> builder;
	private final String path;

	private ItemBuilder<BlockItem, ?> item;

	LLBlockBuilder(BlockBuilder<T, L2Registrate> builder, String path) {
		this.builder = builder;
		this.path = path;
	}

	public LLBlockBuilder<T> prop(MapColor col, SoundType sound) {
		builder.properties(p -> p.mapColor(col).sound(sound));
		return this;
	}

	public LLBlockBuilder<T> prop(NonNullUnaryOperator<BlockBehaviour.Properties> op) {
		builder.properties(op);
		return this;
	}

	public LLBlockBuilder<T> fullBlock() {
		builder.properties(p -> p
				.isValidSpawn((a, b, c, d) -> true)
				.isRedstoneConductor((a, b, c) -> true)
				.isViewBlocking((a, b, c) -> true)
				.isSuffocating((a, b, c) -> true));
		builder.tag(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON);
		return this;
	}

	public LLBlockBuilder<T> fakeSolid() {
		builder.properties(p -> p.noOcclusion()
				.isValidSpawn((a, b, c, d) -> true)
				.isRedstoneConductor((a, b, c) -> true)
				.isViewBlocking((a, b, c) -> false)
				.isSuffocating((a, b, c) -> false));
		builder.tag(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON);
		return this;
	}

	public LLBlockBuilder<T> nonSolid() {
		builder.properties(p -> p.noOcclusion()
				.isValidSpawn((a, b, c, d) -> false)
				.isRedstoneConductor((a, b, c) -> false)
				.isViewBlocking((a, b, c) -> false)
				.isSuffocating((a, b, c) -> false));
		builder.tag(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON);
		return this;
	}

	public LLBlockBuilder<T> strength(float v) {
		builder.properties(p -> p.strength(v));
		return this;
	}

	public LLBlockBuilder<T> strength(float v, float e) {
		builder.properties(p -> p.strength(v, e));
		return this;
	}

	public LLBlockBuilder<T> fragile() {
		builder.properties(p -> p.noCollission().instabreak().pushReaction(PushReaction.DESTROY));
		return this;
	}

	public LLBlockBuilder<T> foliage() {
		prop(MapColor.PLANT, SoundType.GRASS);
		fragile();
		builder.properties(p -> p.offsetType(BlockBehaviour.OffsetType.XZ));
		return this;
	}

	public LLBlockBuilder<T> cubeAll() {
		builder.blockstate((ctx, pvd) ->
				pvd.simpleBlock(ctx.get(), pvd.models().cubeAll(ctx.getName(),
						pvd.modLoc("block/" + path + "/" + ctx.getName()))));
		return this;
	}

	public LLBlockBuilder<T> cubeColumn() {
		builder.blockstate((ctx, pvd) ->
				pvd.simpleBlock(ctx.get(), pvd.models().cubeColumn(ctx.getName(),
						pvd.modLoc("block/" + path + "/" + ctx.getName() + "_side"),
						pvd.modLoc("block/" + path + "/" + ctx.getName() + "_top"))));
		return this;
	}

	public LLBlockBuilder<T> cross() {
		builder.blockstate((ctx, pvd) ->
				pvd.simpleBlock(ctx.get(), pvd.models().cross(ctx.getName(),
								pvd.modLoc("block/" + path + "/" + ctx.getName()))
						.renderType("cutout")));
		return this;
	}

	public LLBlockBuilder<T> noModel() {
		builder.blockstate((ctx, pvd) ->
				pvd.simpleBlock(ctx.get(), pvd.models().withExistingParent(ctx.getName(), pvd.mcLoc("block/air"))));
		return this;
	}

	public LLBlockBuilder<T> blockstate(NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> cons) {
		builder.blockstate(cons);
		return this;
	}

	@SafeVarargs
	public final LLBlockBuilder<T> tag(TagKey<Block>... tags) {
		builder.tag(tags);
		return this;
	}

	@SafeVarargs
	public final LLBlockBuilder<T> itemTag(TagKey<Item>... tags) {
		item.tag(tags);
		return this;
	}

	public LLBlockBuilder<T> shovel() {
		builder.tag(BlockTags.MINEABLE_WITH_SHOVEL);
		return this;
	}

	public LLBlockBuilder<T> pickaxe() {
		builder.properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
				.tag(BlockTags.MINEABLE_WITH_PICKAXE);
		return this;
	}

	public LLBlockBuilder<T> obsidian() {
		builder.properties(p -> p.requiresCorrectToolForDrops().pushReaction(PushReaction.BLOCK))
				.tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL);
		return this;
	}

	public LLBlockBuilder<T> simpleItem() {
		item = builder.item();
		return this;
	}

	public LLBlockBuilder<T> itemModel(NonNullBiConsumer<DataGenContext<Item, BlockItem>, RegistrateItemModelProvider> cons) {
		item.model(cons);
		return this;
	}

	public LLBlockBuilder<T> silkTouchOr(ItemLike other) {
		builder.loot((pvd, block) -> pvd.add(block, pvd.createSingleItemTableWithSilkTouch(block, other)));
		return this;
	}

	public LLBlockBuilder<T> oreLoot(ItemLike other) {
		builder.loot((pvd, block) -> pvd.add(block, pvd.createOreDrop(block, other.asItem())));
		return this;
	}

	public LLBlockBuilder<T> multiOreLoot(ItemLike other, int min, int max) {
		builder.loot((pvd, block) -> pvd.add(block, pvd.createSilkTouchDispatchTable(
				block, LootItem.lootTableItem(other)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
						.apply(ApplyBonusCount.addOreBonusCount(pvd.getRegistries().holderOrThrow(Enchantments.FORTUNE)))
		)));
		return this;
	}

	public LLBlockBuilder<T> shardLoot(ItemLike other, int min, int max) {
		builder.loot((pvd, block) -> pvd.add(block, pvd.createSilkTouchDispatchTable(
				block, LootItem.lootTableItem(other)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
						.apply(ApplyBonusCount.addUniformBonusCount(pvd.getRegistries().holderOrThrow(Enchantments.FORTUNE)))
						.apply(LimitCount.limitCount(IntRange.range(1, max))))));
		return this;
	}

	public LLBlockBuilder<T> lootChance(float min) {
		builder.loot((pvd, block) -> pvd.add(block, pvd.createSilkTouchDispatchTable(
				block, LootItem.lootTableItem(block)
						.when(BonusLevelTableCondition.bonusLevelFlatChance(
								pvd.getRegistries().holderOrThrow(Enchantments.FORTUNE),
								min, min * 1.4f, min * 1.7f, min * 2f)))));
		return this;
	}

	public BlockEntry<T> register() {
		if (item != null) item.build();
		return builder.register();
	}

}
