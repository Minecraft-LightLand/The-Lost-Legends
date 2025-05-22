package dev.xkmc.lostlegends.modules.deepnether.block.misc;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.lostlegends.modules.deepnether.init.DeepNether;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class SoulBlobBlock extends Block {

	private static final VoxelShape SHAPE;

	static {
		SHAPE = Shapes.or(
				Block.box(6, 0, 5, 10, 1, 11),
				Block.box(4, 0, 6, 8, 5, 10),
				Block.box(9, 0, 4, 12, 4, 7),
				Block.box(9, 0, 10, 11, 3, 12)
		);
	}

	public SoulBlobBlock(Properties prop) {
		super(prop);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	public static void buildBlockState(DataGenContext<Block, SoulBlobBlock> ctx, RegistrateBlockstateProvider pvd) {
		var tex = DeepNether.BLOCKS.blockLoc(ctx.getName());
		pvd.simpleBlock(ctx.get(), pvd.models().getBuilder("block/" + ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/soul_blob")))
				.texture("all", tex)
				.texture("particle", tex.withSuffix("_particle"))
				.renderType("cutout")
		);
	}

}
