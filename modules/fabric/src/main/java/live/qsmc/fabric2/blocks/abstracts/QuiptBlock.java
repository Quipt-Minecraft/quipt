package live.qsmc.fabric2.blocks.abstracts;

import live.qsmc.fabric2.blocks.abstracts.properties.BlockProperty;
import net.minecraft.block.Block;

public interface QuiptBlock {
    Block block();

    BlockProperty<?>[] properties();

}
