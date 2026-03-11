package dev.xkmc.lostlegends.modules.spell.engine;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.fastprojectileapi.render.ProjTypeHolder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderData;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderType;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderer;
import dev.xkmc.lostlegends.modules.spell.init.LLSpellRegistry;
import net.minecraft.resources.ResourceLocation;

public record OrientedCrossRenderData(
		ResourceLocation texture
) implements ProjectileRenderData<OrientedCrossRenderData> {

	public static final MapCodec<OrientedCrossRenderData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ResourceLocation.CODEC.fieldOf("texture").forGetter(OrientedCrossRenderData::texture)
	).apply(i, OrientedCrossRenderData::new));

	@Override
	public ProjectileRenderType<OrientedCrossRenderData> type() {
		return LLSpellRegistry.PR_CROSS.get();
	}

	@Override
	public ProjectileRenderer resolve(EngineContext ctx) {
		return new OrientedCrossTextureRenderer(texture);
	}

	public void buildRenderer() {
		ProjTypeHolder.wrap(new OrientedCrossSpriteType(texture));
	}

}
