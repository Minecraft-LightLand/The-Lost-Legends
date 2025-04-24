package dev.xkmc.lostlegends.modules.deepnether.item;

import dev.xkmc.lostlegends.foundation.item.TooltipItem;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;

public class PortalDissonator extends TooltipItem {

	public PortalDissonator(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		var level = ctx.getLevel();
		var pos = ctx.getClickedPos();
		var state = level.getBlockState(pos);
		if (state.is(Blocks.NETHER_PORTAL) || state.is(DeepNether.BLOCKS.PORTAL)) {
			if (!level.isClientSide()) {
				level.removeBlock(pos, false);
				if (ctx.getPlayer() != null && !ctx.getPlayer().getAbilities().instabuild) {
					ctx.getItemInHand().shrink(1);
				}
			}
			return InteractionResult.SUCCESS;
		}
		return super.useOn(ctx);
	}


}
