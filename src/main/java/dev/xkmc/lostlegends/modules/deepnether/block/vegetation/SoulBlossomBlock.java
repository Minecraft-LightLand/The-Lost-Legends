package dev.xkmc.lostlegends.modules.deepnether.block.vegetation;

import dev.xkmc.lostlegends.foundation.fogblock.FogConfig;
import dev.xkmc.lostlegends.foundation.fogblock.IFogBlock;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SoulBlossomBlock extends FlowerBlock implements IFogBlock {

	protected static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 10, 14);
	private static final FogConfig FOG = new FogConfig(FogConfig.Type.SURROUND,
			0, 0.25f, 0.5f,
			0f, 8f, 2f, 24f, false);

	public SoulBlossomBlock(Holder<MobEffect> eff, float dur, Properties prop) {
		super(eff, dur, prop);
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {
		if (level.isClientSide || level.getDifficulty() == Difficulty.PEACEFUL) return;
		if (e instanceof LivingEntity le && !isClear(le)) {
			le.addEffect(new MobEffectInstance(DeepNether.EFFECTS.SOUL_DRAIN, 40));
			boolean hurt = e.hurt(e.damageSources().magic(), 4) || e.hurt(e.damageSources().lava(), 4);
			if (hurt) {
				e.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2 + e.getRandom().nextFloat() * 0.4F);
			}
		}
	}

	@Override
	public boolean isClear(LivingEntity le) {
		return le.hasEffect(DeepNether.EFFECTS.SOUL_SHELTER);
	}

	@Override
	public FogConfig getFogConfig() {
		return FOG;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(BlockTags.BASE_STONE_NETHER) || super.mayPlaceOn(state, level, pos);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		Vec3 vec3 = state.getOffset(level, pos);
		return SHAPE.move(vec3.x, vec3.y, vec3.z);
	}

}
