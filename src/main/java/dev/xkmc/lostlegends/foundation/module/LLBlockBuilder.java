package dev.xkmc.lostlegends.foundation.module;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class LLBlockBuilder<T extends Block> {

	private final BlockBuilder<T, L2Registrate> builder;
	private final String path;

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

	public LLBlockBuilder<T> cross() {
		builder.blockstate((ctx, pvd) ->
				pvd.simpleBlock(ctx.get(), pvd.models().cross(ctx.getName(),
								pvd.modLoc("block/" + path + "/" + ctx.getName()))
						.renderType("cutout")));
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
		builder.simpleItem();
		return this;
	}

	public LLBlockBuilder<T> silkTouchOr(ItemLike other) {
		builder.loot((pvd, block) -> pvd.add(block, pvd.createSingleItemTableWithSilkTouch(block, other)));
		return this;
	}

	public BlockEntry<T> register() {
		return builder.register();
	}

}
