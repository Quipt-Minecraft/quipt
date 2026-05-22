package live.qsmc.quipt.fabric.blocks.abstracts;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

public abstract class QuiptCarpetBlock extends Block implements QuiptBlock {

    public static final MapCodec<CarpetBlock> CODEC = simpleCodec(CarpetBlock::new);
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    public QuiptCarpetBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        return super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public MapCodec<? extends CarpetBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }


    @Override
    public Block block() {
        return this;
    }

}
