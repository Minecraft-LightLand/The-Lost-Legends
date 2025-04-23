package dev.xkmc.lostlegends.modules.deepnether.block.portal;

import com.mojang.serialization.MapCodec;
import dev.xkmc.lostlegends.modules.deepnether.data.DNDimensionGen;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.portal.DimensionTransition;

import javax.annotation.Nullable;

public class LavaPortalBlock extends Block implements LiquidBlockContainer, Portal {

	public static final MapCodec<LavaPortalBlock> CODEC = simpleCodec(LavaPortalBlock::new);

	@Override
	public MapCodec<LavaPortalBlock> codec() {
		return CODEC;
	}

	public LavaPortalBlock(BlockBehaviour.Properties prop) {
		super(prop);
	}

	@Override
	public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluid) {
		return false;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
		return fluidstate.is(FluidTags.LAVA) && fluidstate.getAmount() == 8 ? super.getStateForPlacement(ctx) : null;
	}

	@Override
	protected FluidState getFluidState(BlockState p_54319_) {
		return Fluids.LAVA.getSource(false);
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {
		if (e.canUsePortal(false)) {
			e.setAsInsidePortal(this, pos);
		}
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
		return ItemStack.EMPTY;
	}

	@Override
	protected boolean canBeReplaced(BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public Portal.Transition getLocalTransition() {
		return Portal.Transition.CONFUSION;
	}

	@Override
	public int getPortalTransitionTime(ServerLevel sl, Entity e) {
		return e instanceof Player player ? Math.max(1, sl.getGameRules().getInt(
				player.getAbilities().invulnerable
						? GameRules.RULE_PLAYERS_NETHER_PORTAL_CREATIVE_DELAY
						: GameRules.RULE_PLAYERS_NETHER_PORTAL_DEFAULT_DELAY
		)) : 0;
	}

	@Nullable
	@Override
	public DimensionTransition getPortalDestination(ServerLevel level, Entity e, BlockPos pos) {
		boolean isNether = level.dimension() == Level.NETHER;
		ResourceKey<Level> key = isNether ? DNDimensionGen.DEEP_NETHER : Level.NETHER;
		ServerLevel sl = level.getServer().getLevel(key);
		if (sl == null)
			return null;
		WorldBorder border = sl.getWorldBorder();
		double scale = DimensionType.getTeleportationScale(level.dimensionType(), sl.dimensionType());
		double targetY = isNether ? sl.getMaxBuildHeight() - 8 : sl.getMinBuildHeight() + 8;
		BlockPos target = border.clampToBounds(e.getX() * scale, targetY, e.getZ() * scale);
		int range = isNether ? 64 : 128;
		long time = level.getGameTime();
		if (e instanceof Player) {
			long last = e.getPersistentData().getLong("LastLavaPortalFailTime");
			if (time - last < 10) {
				e.setPortalCooldown(0);
				return null;
			}
			if (time - last == 10) {
				var forcer = new LavaPortalForcer(sl);
				if (forcer.isFeatureLoaded(sl, target, range)) {
					long first = e.getPersistentData().getLong("FirstLavaPortalFailTime");
					if (time - first > 60) {
						e.getPersistentData().putLong("FirstLavaPortalFailTime", time);
					} else if (time - first >= 50) {
						e.getPersistentData().putLong("FirstLavaPortalFailTime", 0);
						return LavaPortalHelper.createExitPortal(sl, e, target);
					}
				}
			} else {
				e.getPersistentData().putLong("FirstLavaPortalFailTime", 0);
			}
		}
		var ans = LavaPortalHelper.getExitPortal(sl, e, target, border, range);
		if (ans == null && e instanceof Player) {
			new LavaPortalForcer(sl).ensureFeatureLoaded(sl, target, range);
			e.getPersistentData().putLong("LastLavaPortalFailTime", time);
			e.setPortalCooldown(0);
		}
		return ans;
	}


}
