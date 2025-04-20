package com.quiptmc.fabric.client;

import com.quiptmc.fabric.blocks.QuiptBlocks;
import com.quiptmc.fabric.blocks.TestQuiptBlock;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;

public class EntrypointClient  implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
//        QuiptBlocks.register("test_block", TestQuiptBlock::new, AbstractBlock.Settings.copy(Blocks.GRASS_BLOCK), true);

    }
}
