package dev.xkmc.lostlegends.modules.deepnether.worldgen.structure.maze;

import dev.xkmc.lostlegends.foundation.maze.structure.MazeGenerator;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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

	private static final int SHIFT = 7;

	public enum ShiftType {
		FLAT(new BlockPos(SHIFT, 0, SHIFT)),
		EDGE(new BlockPos(0, 0, SHIFT)),
		NONE(new BlockPos(0, 0, 0));

		public final BlockPos pos;

		ShiftType(BlockPos pos) {
			this.pos = pos;
		}
	}

	private static StructurePlaceSettings makeSettings(boolean inner, Rotation rotation, Mirror mirror, ShiftType shift) {
		BlockIgnoreProcessor processor = inner ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
		return (new StructurePlaceSettings()).setIgnoreEntities(true)
				.addProcessor(processor).setLiquidSettings(inner ? LiquidSettings.IGNORE_WATERLOGGING : LiquidSettings.APPLY_WATERLOGGING)
				.setRotation(rotation).setMirror(mirror);
	}

	private static BlockPos shiftPos(StructurePlaceSettings settings, BlockPos pos, ShiftType shift) {
		if (shift == ShiftType.FLAT) {
			BlockPos center = shift.pos;
			BlockPos actual = StructureTemplate.transform(center, settings.getMirror(), settings.getRotation(), settings.getRotationPivot());
			return pos.offset(center.subtract(actual));
		}
		return pos;
	}

	private final ShiftType shift;

	public MazePiece(ResourceLocation prefix, StructureTemplateManager manager, MazeGenerator.CellInstance ins, BlockPos pos, ShiftType shift, boolean inner) {
		this(prefix, manager, ins, makeSettings(inner, ins.rot(), ins.mir(), shift), pos, shift);
	}

	private MazePiece(ResourceLocation prefix, StructureTemplateManager manager, MazeGenerator.CellInstance ins, StructurePlaceSettings settings, BlockPos pos, ShiftType shift) {
		super(DeepNether.WG.MAZE_PIECE.get(), 0, manager, prefix.withSuffix(ins.id()), ins.id(), settings, shiftPos(settings, pos, shift));
		this.shift = shift;
	}

	public MazePiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
		super(DeepNether.WG.MAZE_PIECE.get(), tag, ctx.structureTemplateManager(), (id) ->
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
