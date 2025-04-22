package dev.xkmc.lostlegends.modules.deepnether.block;

import com.mojang.serialization.MapCodec;
import dev.xkmc.lostlegends.foundation.fogblock.FogConfig;
import dev.xkmc.lostlegends.foundation.fogblock.IFogBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;

public class DarkStoneBlock extends Block implements IFogBlock {

	public static final MapCodec<DarkStoneBlock> CODEC = simpleCodec(DarkStoneBlock::new);
	private static final FogConfig FOG = new FogConfig(FogConfig.Type.SURROUND,
			0, 0, 0,
			0f, 8f, false);

	public DarkStoneBlock(Properties p) {
		super(p);
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return CODEC;
	}

	@Override
	public FogConfig getFogConfig() {
		return FOG;
	}

	@Override
	public boolean isClear(Entity entity) {
		return entity.isSpectator();
	}

}
