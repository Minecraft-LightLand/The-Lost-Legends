package dev.xkmc.lostlegends.modules.maze.structure;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.lostlegends.modules.maze.init.MazeModule;
import dev.xkmc.lostlegends.modules.maze.logic.generator.MazeConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.List;
import java.util.Optional;

public class MazeStructure extends Structure {

	public static final MapCodec<MazeStructure> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Structure.settingsCodec(i),
			ResourceLocation.CODEC.fieldOf("path").forGetter(e -> e.id),
			Codec.STRING.listOf().fieldOf("rooms").forGetter(e -> List.of(e.rooms)),
			Codec.INT.fieldOf("cell_width").forGetter(e -> e.cellWidth),
			Codec.INT.fieldOf("cell_height").forGetter(e -> e.cellHeight),
			Codec.INT.fieldOf("maze_width").forGetter(e -> e.mazeWidth),
			Codec.INT.fieldOf("maze_height").forGetter(e -> e.mazeHeight)
	).apply(i, MazeStructure::new));

	public final ResourceLocation id;
	public final String[] rooms;
	public final int cellWidth, cellHeight, mazeWidth, mazeHeight;
	public int scale;

	public MazeStructure(
			StructureSettings settings, ResourceLocation id, List<String> rooms,
			int cellWidth, int cellHeight, int mazeWidth, int mazeHeight
	) {
		super(settings);
		this.id = id;
		this.rooms = rooms.toArray(String[]::new);
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		this.mazeWidth = mazeWidth;
		this.mazeHeight = mazeHeight;
		scale = cellWidth / 2;
	}

	@Override
	protected Optional<GenerationStub> findGenerationPoint(GenerationContext ctx) {
		BlockPos pos = ctx.chunkPos().getMiddleBlockPosition(0);
		int topLandY = ctx.chunkGenerator().getFirstFreeHeight(pos.getX(), pos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, ctx.heightAccessor(), ctx.randomState());
		BlockPos blockpos = pos.above(topLandY);
		MazeConfig config = new MazeConfig();
		config.invariant = 2;
		config.survive = 4;
		config.invarianceRim = new int[][]{{0, 1, 2, 3, 4, 5, 6, 7}, {0, 4, 8, 12, 1, 2, 3, 5, 6, 7, 9, 10, 11, 13, 14, 15}};
		List<StructurePiece> list = Lists.newArrayList();
		var gen = new MazeGenerator(this);
		gen.addPieces(ctx.structureTemplateManager(), blockpos, list, ctx.random(), config);
		var builder = new StructurePiecesBuilder();
		list.forEach(builder::addPiece);
		return Optional.of(new GenerationStub(pos, Either.right(builder)));
	}

	@Override
	public StructureType<?> type() {
		return MazeModule.MAZE.get();
	}

	public ResourceLocation loc(String path) {
		return id.withSuffix("/" + path);
	}

}
