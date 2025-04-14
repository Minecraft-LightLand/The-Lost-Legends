package dev.xkmc.lostlegends.foundation.module;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.lostlegends.init.LostLegends;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class LLRegBase {

	private final String path;
	private final L2Registrate reg;

	public LLRegBase(L2Registrate reg, String path) {
		this.path = path;
		this.reg = reg;
	}

	public <T extends Block> LLBlockBuilder<T> block(String id, NonNullFunction<BlockBehaviour.Properties, T> factory) {
		return new LLBlockBuilder<>(LostLegends.REGISTRATE.block(id, factory), path);
	}

	public ResourceLocation blockLoc(String id) {
		return reg.loc("block/" + path + "/" + id);
	}

	public ResourceLocation itemLoc(String id) {
		return reg.loc("item/" + path + "/" + id);
	}

	public void flatItem(DataGenContext<Item, Item> ctx, RegistrateItemModelProvider pvd) {
		pvd.generated(ctx, itemLoc(ctx.getName()));
	}

}
