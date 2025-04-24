package dev.xkmc.lostlegends.foundation.module;

import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.lostlegends.foundation.block.LLFluidBlock;
import dev.xkmc.lostlegends.foundation.block.LLFluidType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class LLRegBase {

	private final String path;
	private final L2Registrate reg;

	public LLRegBase(L2Registrate reg, String path) {
		this.path = path;
		this.reg = reg;
	}

	public <T extends Block> LLBlockBuilder<T> block(String id, NonNullFunction<BlockBehaviour.Properties, T> factory) {
		return new LLBlockBuilder<>(reg.block(id, factory), path);
	}

	public <T extends LLFluidType, F extends BaseFlowingFluid, S extends BaseFlowingFluid> FluidBuilder<F, L2Registrate> fluid(
			String id, FluidBuilder.FluidTypeFactory typeFactory,
			NonNullFunction<BaseFlowingFluid.Properties, F> flowFactory,
			NonNullFunction<BaseFlowingFluid.Properties, S> sourceFactory
	) {
		return reg.fluid(id,
						blockLoc(id + "_still"), blockLoc(id + "_flow"),
						typeFactory, flowFactory)
				.source(sourceFactory).block(LLFluidBlock::new).build()
				.bucket().model(this::flatItem).build();
	}

	public ResourceLocation blockLoc(String id) {
		return reg.loc("block/" + path + "/" + id);
	}

	public ResourceLocation itemLoc(String id) {
		return reg.loc("item/" + path + "/" + id);
	}

	public <T extends Item> void flatItem(DataGenContext<Item, T> ctx, RegistrateItemModelProvider pvd) {
		pvd.generated(ctx, itemLoc(ctx.getName()));
	}

	public <T extends Item> void flatBlockItem(DataGenContext<Item, T> ctx, RegistrateItemModelProvider pvd) {
		pvd.generated(ctx, blockLoc(ctx.getName()));
	}

}
