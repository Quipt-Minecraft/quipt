package live.qsmc.quipt.fabric.blocks.abstracts;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class QuiptFullBlock extends Block implements QuiptBlock {
    public QuiptFullBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public Block block() {
        return this;
    }
}
