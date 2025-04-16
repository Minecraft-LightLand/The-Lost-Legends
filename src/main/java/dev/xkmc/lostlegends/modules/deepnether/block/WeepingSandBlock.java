package dev.xkmc.lostlegends.modules.deepnether.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WeepingSandBlock extends Block {
    public static final MapCodec<WeepingSandBlock> CODEC = simpleCodec(WeepingSandBlock::new);
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);

    @Override
    public MapCodec<WeepingSandBlock> codec() {
        return CODEC;
    }

    public WeepingSandBlock(BlockBehaviour.Properties p_56672_) {
        super(p_56672_);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState p_56702_, BlockGetter p_56703_, BlockPos p_56704_, CollisionContext p_56705_) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getBlockSupportShape(BlockState p_56707_, BlockGetter p_56708_, BlockPos p_56709_) {
        return Shapes.block();
    }

    @Override
    protected VoxelShape getVisualShape(BlockState p_56684_, BlockGetter p_56685_, BlockPos p_56686_, CollisionContext p_56687_) {
        return Shapes.block();
    }
    @Override
    protected boolean isPathfindable(BlockState p_56679_, PathComputationType p_56682_) {
        return false;
    }

    @Override
    protected float getShadeBrightness(BlockState p_222462_, BlockGetter p_222463_, BlockPos p_222464_) {
        return 0.2F;
    }
}
