package live.qsmc.quipt.fabric.blocks.abstracts;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class QuiptPillarBlock extends RotatedPillarBlock implements QuiptBlock {


    public QuiptPillarBlock(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Override
    public Block block() {
        return this;
    }
}
