package com.quiptmc.fabric;

import com.quiptmc.fabric.api.QuiptEntrypoint;
import com.quiptmc.fabric.blocks.QuiptBlocks;
import com.quiptmc.minecraft.CoreUtils;
import com.quiptmc.fabric.blocks.TestQuiptBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;

public class FabricLoaderEntrypoint extends QuiptEntrypoint {


    @Override
    public void onInitialize() {
        CoreUtils.init(this.integration());
        QuiptBlocks.register("test_block", TestQuiptBlock::new, AbstractBlock.Settings.copy(Blocks.GRASS_BLOCK), true);
    }
}