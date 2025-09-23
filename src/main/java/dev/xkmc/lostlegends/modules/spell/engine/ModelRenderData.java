package dev.xkmc.lostlegends.modules.spell.engine;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderData;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderType;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderer;
import dev.xkmc.lostlegends.modules.spell.init.LLSpellRegistry;
import net.minecraft.resources.ResourceLocation;

public record ModelRenderData(
		ResourceLocation model, DoubleVariable scale
) implements ProjectileRenderData<ModelRenderData> {

	public static final MapCodec<ModelRenderData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ResourceLocation.CODEC.fieldOf("model").forGetter(ModelRenderData::model),
			DoubleVariable.codec("initial", ModelRenderData::scale)
	).apply(i, ModelRenderData::new));

	@Override
	public ProjectileRenderType<ModelRenderData> type() {
		return LLSpellRegistry.PR_MODEL.get();
	}

	@Override
	public ProjectileRenderer resolve(EngineContext ctx) {
		return new ModelRenderer(model, (float) scale.eval(ctx));
	}

}
