package live.qsmc.quipt.fabric.blocks.abstracts;

import live.qsmc.quipt.fabric.blocks.abstracts.properties.BlockProperty;
import net.minecraft.block.Block;

public interface QuiptBlock {
    Block block();

    BlockProperty<?>[] properties();

}
