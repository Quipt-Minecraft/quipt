package com.quiptmc.fabric;

import com.quiptmc.fabric.api.QuiptEntrypoint;
import com.quiptmc.fabric.blocks.QuiptBlocks;
import com.quiptmc.minecraft.CoreUtils;
import com.quiptmc.fabric.blocks.TestQuiptBlock;
import net.minecraft.block.AbstractBlock;

public class FabricLoaderEntrypoint extends QuiptEntrypoint {


    @Override
    public void onInitialize() {
        CoreUtils.init(this.integration());
        QuiptBlocks.register("test_block", TestQuiptBlock::new, AbstractBlock.Settings.create(), true);
    }
}