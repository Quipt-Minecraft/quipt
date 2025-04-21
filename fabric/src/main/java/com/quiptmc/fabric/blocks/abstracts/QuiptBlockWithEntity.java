package com.quiptmc.fabric.blocks.abstracts;

import net.minecraft.block.*;

public abstract class QuiptBlockWithEntity extends BlockWithEntity implements QuiptBlock {

    protected QuiptBlockWithEntity(Settings settings) {
        super(settings);
    }
}