package live.qsmc.quipt.fabric.blocks.abstracts;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public interface QuiptBlock {
    Block block();

    BlockBehaviour.Properties properties();

}
