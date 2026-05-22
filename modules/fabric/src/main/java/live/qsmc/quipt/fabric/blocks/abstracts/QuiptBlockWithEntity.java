package live.qsmc.quipt.fabric.blocks.abstracts;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class QuiptBlockWithEntity extends BaseEntityBlock implements QuiptBlock {

    protected QuiptBlockWithEntity(BlockBehaviour.Properties settings) {
        super(settings);
    }
}