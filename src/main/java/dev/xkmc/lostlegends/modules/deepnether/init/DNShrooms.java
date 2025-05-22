package dev.xkmc.lostlegends.modules.deepnether.init;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.lostlegends.foundation.module.LLRegBase;
import dev.xkmc.lostlegends.modules.deepnether.block.vegetation.GrowableShroomBlock;
import dev.xkmc.lostlegends.modules.deepnether.block.vegetation.VariantShroomBlock;
import net.minecraft.core.Holder;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.List;

public class DNShrooms extends LLRegBase {

	public final BlockEntry<GrowableShroomBlock> SCORCHROOM, HEARTHROOM, GHOSHROOM, OCEAN_PHANTOM;
	public final BlockEntry<VariantShroomBlock> BISCORCHROOM, LARGE_HEARTHROOM, HORNED_GHOSHROOM, DEEP_OCEAN_PHANTOM, BLAZING_OCEAN_PHANTOM;

	public DNShrooms(L2Registrate reg, String path) {
		super(reg, path);

		SCORCHROOM = block("scorchroom", p -> new GrowableShroomBlock(p, 3, 15, scorch()))
				.prop(MapColor.NETHER, SoundType.GRASS)
				.prop(BlockBehaviour.Properties::randomTicks)
				.light(9)
				.foliage().blockstate(this::growable)
				.simpleItem()
				.transformItem(this::makeItem)
				.register();

		BISCORCHROOM = block("biscorchroom", p -> new VariantShroomBlock(p, 3, 15, SCORCHROOM))
				.prop(MapColor.NETHER, SoundType.GRASS)
				.prop(BlockBehaviour.Properties::randomTicks)
				.light(11)
				.foliage().cross()
				.simpleItem()
				.transformItem(this::makeItem)
				.register();

		HEARTHROOM = block("hearthroom", p -> new GrowableShroomBlock(p, 2, 12, hearth()))
				.prop(MapColor.NETHER, SoundType.GRASS)
				.prop(BlockBehaviour.Properties::randomTicks)
				.foliage().blockstate(this::growable)
				.simpleItem()
				.transformItem(this::makeItem)
				.register();

		LARGE_HEARTHROOM = block("large_hearthroom", p -> new VariantShroomBlock(p, 1, 15, HEARTHROOM))
				.prop(MapColor.NETHER, SoundType.GRASS)
				.prop(BlockBehaviour.Properties::randomTicks)
				.foliage().cross()
				.simpleItem()
				.transformItem(this::makeItem)
				.register();

		GHOSHROOM = block("ghoshroom", p -> new GrowableShroomBlock(p, 3, 13, ghost()))
				.prop(MapColor.NETHER, SoundType.GRASS)
				.prop(BlockBehaviour.Properties::randomTicks)
				.light(7)
				.foliage().blockstate(this::growable)
				.simpleItem()
				.transformItem(this::makeItem)
				.register();

		HORNED_GHOSHROOM = block("horned_ghoshroom", p -> new VariantShroomBlock(p, 1, 13, GHOSHROOM))
				.prop(MapColor.NETHER, SoundType.GRASS)
				.prop(BlockBehaviour.Properties::randomTicks)
				.light(9)
				.foliage().cross()
				.simpleItem()
				.transformItem(this::makeItem)
				.register();

		OCEAN_PHANTOM = block("ocean_phantom", p -> new GrowableShroomBlock(p, 2, 12, phantom()))
				.prop(MapColor.NETHER, SoundType.GRASS)
				.prop(BlockBehaviour.Properties::randomTicks)
				.light(11)
				.foliage().blockstate(this::growable)
				.simpleItem()
				.transformItem(this::makeItem)
				.register();

		DEEP_OCEAN_PHANTOM = block("deep_ocean_phantom", p -> new VariantShroomBlock(p, 1, 12, OCEAN_PHANTOM))
				.prop(MapColor.NETHER, SoundType.GRASS)
				.prop(BlockBehaviour.Properties::randomTicks)
				.light(11)
				.foliage().cross()
				.simpleItem()
				.transformItem(this::makeItem)
				.register();

		BLAZING_OCEAN_PHANTOM = block("blazing_ocean_phantom", p -> new VariantShroomBlock(p, 2, 15, OCEAN_PHANTOM))
				.prop(MapColor.NETHER, SoundType.GRASS)
				.prop(BlockBehaviour.Properties::randomTicks)
				.light(14)
				.foliage().cross()
				.simpleItem()
				.transformItem(this::makeItem)
				.register();

	}

	private List<Holder<Block>> scorch() {
		return List.of(BISCORCHROOM);
	}

	private List<Holder<Block>> hearth() {
		return List.of(LARGE_HEARTHROOM);
	}

	private List<Holder<Block>> ghost() {
		return List.of(HORNED_GHOSHROOM);
	}

	private List<Holder<Block>> phantom() {
		return List.of(DEEP_OCEAN_PHANTOM, BLAZING_OCEAN_PHANTOM);
	}

	private void makeItem(ItemBuilder<BlockItem, ?> builder) {
		builder.properties(p -> p
						.food(new FoodProperties.Builder().nutrition(4).saturationModifier(0.6f).build())
				).model(this::flatItem)
				.tag(Tags.Items.MUSHROOMS)
				.dataMap(NeoForgeDataMaps.COMPOSTABLES, new Compostable(0.65f));
	}

	public <T extends GrowableShroomBlock> void growable(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider pvd) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			boolean large = state.getValue(GrowableShroomBlock.GROWN);
			String id = large ? ctx.getName() : ctx.getName() + "_small";
			return ConfiguredModel.builder().modelFile(pvd.models().cross(id, blockLoc(id)).renderType("cutout")).build();
		});
	}

}
