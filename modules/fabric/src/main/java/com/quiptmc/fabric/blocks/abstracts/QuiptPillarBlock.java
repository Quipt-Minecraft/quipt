package com.quiptmc.fabric.blocks.abstracts;

import net.minecraft.block.Block;
import net.minecraft.block.PillarBlock;

public abstract class QuiptPillarBlock extends PillarBlock implements QuiptBlock {


    public QuiptPillarBlock(Settings settings) {
        super(settings);
    }

    @Override
    public Block block() {
        return this;
    }
}
