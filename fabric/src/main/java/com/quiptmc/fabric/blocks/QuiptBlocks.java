package com.quiptmc.fabric.blocks;

import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.fabric.FabricLoaderEntrypoint;
import com.quiptmc.fabric.Initializer;
import com.quiptmc.minecraft.CoreUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;
public class QuiptBlocks {

    private static final Registry<QuiptBlock> blocks = Registries.register("blocks", QuiptBlock.class);

    public static QuiptBlock get(String name) {
        return blocks.get(name).orElseThrow(() -> new IllegalArgumentException("Block " + name + " not found"));
    }

    public static QuiptBlock register(String name, Function<AbstractBlock.Settings, QuiptBlock> blockFactory, AbstractBlock.Settings settings, boolean shouldRegisterItem) {
        // Create a registry key for the block
        RegistryKey<Block> blockKey = keyOfBlock(name);
        // Create the block instance
        QuiptBlock block = blockFactory.apply(settings.registryKey(blockKey));

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            // Items need to be registered with a different type of registry key, but the ID
            // can be the same.
            RegistryKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey));
            net.minecraft.registry.Registry.register(net.minecraft.registry.Registries.ITEM, itemKey, blockItem);
        }
        blocks.register(name, block);
        return net.minecraft.registry.Registry.register(net.minecraft.registry.Registries.BLOCK, blockKey, block);
    }

    private static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("quipt", name));
    }

    private static RegistryKey<Item> keyOfItem(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of("quipt", name));
    }

    public static Registry<QuiptBlock> blocks() {
        return blocks;
    }
}
