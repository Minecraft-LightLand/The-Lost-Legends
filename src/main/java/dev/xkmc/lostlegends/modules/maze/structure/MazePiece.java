package dev.xkmc.lostlegends.modules.maze.structure;

import dev.xkmc.lostlegends.modules.maze.init.MazeModule;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class MazePiece extends TemplateStructurePiece {

	public enum ShiftType {
		FLAT(new BlockPos(1, 0, 1)),
		EDGE(new BlockPos(0, 0, 1)),
		NONE(new BlockPos(0, 0, 0));

		public final BlockPos pos;

		ShiftType(BlockPos pos) {
			this.pos = pos;
		}
	}

	private static StructurePlaceSettings makeSettings(boolean inner, Rotation rotation, Mirror mirror, ShiftType shift) {
		BlockIgnoreProcessor processor = inner ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
		return (new StructurePlaceSettings()).setIgnoreEntities(true)
				.setKnownShape(true)
				.addProcessor(processor).setLiquidSettings(inner ? LiquidSettings.IGNORE_WATERLOGGING : LiquidSettings.APPLY_WATERLOGGING)
				.setRotation(rotation).setMirror(mirror);
	}

	private static BlockPos shiftPos(StructurePlaceSettings settings, BlockPos pos, ShiftType shift, int scale) {
		if (shift == ShiftType.FLAT) {
			BlockPos center = shift.pos.multiply(scale);
			BlockPos actual = StructureTemplate.transform(center, settings.getMirror(), settings.getRotation(), settings.getRotationPivot());
			return pos.offset(center.subtract(actual));
		}
		return pos;
	}

	private final ShiftType shift;

	public MazePiece(
			MazeStructure parent, StructureTemplateManager manager, MazeGenerator.CellInstance ins, BlockPos pos, ShiftType shift, boolean inner) {
		this(parent, manager, ins, makeSettings(inner, ins.rot(), ins.mir(), shift), pos, shift);
	}

	private MazePiece(
			MazeStructure parent, StructureTemplateManager manager, MazeGenerator.CellInstance ins, StructurePlaceSettings settings, BlockPos pos, ShiftType shift) {
		super(MazeModule.MAZE_PIECE.get(), 0, manager, parent.loc(ins.id()), ins.id(), settings, shiftPos(settings, pos, shift, parent.scale));
		this.shift = shift;
	}

	public MazePiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(MazeModule.MAZE_PIECE.get(), tag, ctx.structureTemplateManager(), (id) ->
				makeSettings(tag.getBoolean("OW"),
						Rotation.valueOf(tag.getString("Rot")),
						Mirror.valueOf(tag.getString("Mir")),
						ShiftType.valueOf(tag.getString("Shift"))));
		this.shift = ShiftType.valueOf(tag.getString("Shift"));
	}

	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		super.addAdditionalSaveData(context, tag);
		tag.putString("Rot", this.placeSettings.getRotation().name());
		tag.putString("Mir", this.placeSettings.getMirror().name());
		tag.putBoolean("OW", this.placeSettings.getProcessors().getFirst() == BlockIgnoreProcessor.STRUCTURE_BLOCK);
		tag.putString("Shift", this.shift.name());
	}


	@Override
	protected void handleDataMarker(String id, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {
		if (id.startsWith("Chest")) {
			BlockPos blockpos = pos.below();
			if (box.isInside(blockpos)) {
				RandomizableContainer.setBlockEntityLootTable(level, random, blockpos, BuiltInLootTables.END_CITY_TREASURE);//TODO
			}
		}
	}

}
