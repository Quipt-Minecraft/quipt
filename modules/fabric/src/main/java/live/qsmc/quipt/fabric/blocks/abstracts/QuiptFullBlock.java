package live.qsmc.quipt.fabric.blocks.abstracts;

import net.minecraft.block.Block;

public abstract class QuiptFullBlock extends Block implements QuiptBlock {
    public QuiptFullBlock(Settings settings) {
        super(settings);
    }

    @Override
    public Block block() {
        return this;
    }
}
