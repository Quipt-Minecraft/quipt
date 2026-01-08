package com.quiptmc.fabric.blocks.abstracts;

import com.quiptmc.fabric.blocks.abstracts.properties.BlockProperty;
import net.minecraft.block.Block;

public interface QuiptBlock {
    Block block();

    BlockProperty<?>[] properties();

}
