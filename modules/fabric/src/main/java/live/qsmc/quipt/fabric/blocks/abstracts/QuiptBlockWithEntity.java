package live.qsmc.quipt.fabric.blocks.abstracts;

import net.minecraft.block.*;

public abstract class QuiptBlockWithEntity extends BlockWithEntity implements QuiptBlock {

    protected QuiptBlockWithEntity(Settings settings) {
        super(settings);
    }
}