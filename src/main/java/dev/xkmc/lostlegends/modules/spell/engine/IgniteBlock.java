package dev.xkmc.lostlegends.modules.spell.engine;

import com.mojang.serialization.MapCodec;
import dev.xkmc.l2magic.content.engine.block.IBlockProcessor;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.lostlegends.modules.spell.init.LLSpellRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.BaseFireBlock;
import net.neoforged.neoforge.event.EventHooks;

public record IgniteBlock() implements IBlockProcessor<IgniteBlock> {

	public static final MapCodec<IgniteBlock> CODEC = MapCodec.unit(IgniteBlock::new);

	@Override
	public void execute(EngineContext ctx) {
		var user = ctx.user().user();
		if (!(user instanceof Mob) || EventHooks.canEntityGrief(user.level(), user)) {
			var dir = ctx.loc().dir().normalize();
			BlockPos hit = BlockPos.containing(ctx.loc().pos().add(dir));
			BlockPos pos = BlockPos.containing(ctx.loc().pos());
			if (hit.equals(pos)) return;
			if (!user.level().isEmptyBlock(hit) && user.level().isEmptyBlock(pos)) {
				user.level().setBlockAndUpdate(pos, BaseFireBlock.getState(user.level(), pos));
			}
		}
	}

	@Override
	public EngineType<IgniteBlock> type() {
		return LLSpellRegistry.MT_FIRE.get();
	}
}
