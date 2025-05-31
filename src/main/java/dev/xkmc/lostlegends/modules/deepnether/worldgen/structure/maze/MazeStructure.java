package dev.xkmc.lostlegends.modules.deepnether.worldgen.structure.maze;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.lostlegends.foundation.maze.generator.MazeConfig;
import dev.xkmc.lostlegends.foundation.maze.structure.MazeGenerator;
import dev.xkmc.lostlegends.init.LostLegends;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.List;
import java.util.Optional;

public class MazeStructure extends Structure {

	public static final MapCodec<MazeStructure> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Structure.settingsCodec(i)
	).apply(i, MazeStructure::new));

	protected MazeStructure(StructureSettings settings) {
		super(settings);
	}

	@Override
	protected Optional<GenerationStub> findGenerationPoint(GenerationContext ctx) {
		BlockPos pos = ctx.chunkPos().getMiddleBlockPosition(0);
		int topLandY = ctx.chunkGenerator().getFirstFreeHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, ctx.heightAccessor(), ctx.randomState());
		BlockPos blockpos = pos.above(topLandY);
		MazeConfig config = new MazeConfig();
		config.invariant = 2;
		config.survive = 4;
		config.INVARIANCE_RIM = new int[][]{{0, 1, 2, 3, 4, 5, 6, 7}, {0, 4, 8, 12, 1, 2, 3, 5, 6, 7, 9, 10, 11, 13, 14, 15}};
		List<StructurePiece> list = Lists.newArrayList();
		var gen = new MazeGenerator(MyRoomType.values(), LostLegends.loc("maze/"));
		gen.addPieces(ctx.structureTemplateManager(), blockpos, list, ctx.random(), config);
		var builder = new StructurePiecesBuilder();
		list.forEach(builder::addPiece);
		return Optional.of(new GenerationStub(pos, Either.right(builder)));
	}

	@Override
	public StructureType<?> type() {
		return DeepNether.WG.MAZE.get();
	}

}
