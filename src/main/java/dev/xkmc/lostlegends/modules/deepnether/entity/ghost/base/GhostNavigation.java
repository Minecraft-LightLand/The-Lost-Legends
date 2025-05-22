package dev.xkmc.lostlegends.modules.deepnether.entity.ghost.base;

import dev.xkmc.lostlegends.modules.deepnether.util.SoulEffectsHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;

public class GhostNavigation extends GroundPathNavigation {

	public GhostNavigation(Mob mob, Level level) {
		super(mob, level);
	}

	@Override
	protected PathFinder createPathFinder(int max) {
		nodeEvaluator = new GhostNodeEvaluator();
		nodeEvaluator.setCanPassDoors(true);
		nodeEvaluator.setCanFloat(true);
		return new PathFinder(this.nodeEvaluator, max);
	}

	@Override
	protected boolean hasValidPathType(PathType type) {
		return type == PathType.LAVA || type == PathType.DAMAGE_FIRE || type == PathType.DANGER_FIRE ||
				super.hasValidPathType(type);
	}

	@Override
	public boolean isStableDestination(BlockPos pos) {
		return level.getBlockState(pos).is(Blocks.LAVA) ||
				level.getBlockState(pos).is(SoulEffectsHelper.getFluidBlock().getBlock()) ||
				super.isStableDestination(pos);
	}

}
