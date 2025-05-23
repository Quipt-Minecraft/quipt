package com.quiptmc.fabric.blocks;

import com.quiptmc.core.annotations.Nullable;
import com.quiptmc.fabric.blocks.abstracts.QuiptBlock;
import com.quiptmc.fabric.blocks.abstracts.QuiptBlockWithEntity;
import com.quiptmc.fabric.blocks.abstracts.properties.BlockProperty;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;

import java.util.function.Function;
public class QuiptBlocks {

    public static final com.quiptmc.core.data.registries.Registry<QuiptBlock> BLOCKS = com.quiptmc.core.data.registries.Registries.register("blocks", ()->null);
    public static final com.quiptmc.core.data.registries.Registry<BlockEntityType<? extends BlockEntity>> ENTITY_TYPES = com.quiptmc.core.data.registries.Registries.register("block_entity", ()->null);
    public static final com.quiptmc.core.data.registries.Registry<BlockProperty<?>> BLOCK_PROPERTIES = com.quiptmc.core.data.registries.Registries.register("block_properties", ()->null);

    public static QuiptBlock get(String name) {
        return BLOCKS.get(name).orElseThrow(() -> new IllegalArgumentException("Block " + name + " not found"));
    }

    public static BlockEntityType<? extends BlockEntity> type(String name) {
        return ENTITY_TYPES.get(name).orElseThrow(() -> new IllegalArgumentException("BlockEntityType " + name + " not found"));
    }

    public static QuiptBlock register(String name, Function<AbstractBlock.Settings, QuiptBlock> blockFactory, AbstractBlock.Settings settings, boolean shouldRegisterItem, @Nullable  FabricBlockEntityTypeBuilder.Factory<?> entityFactory) {
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

            BlockItem blockItem = new BlockItem(block.block(), new Item.Settings().registryKey(itemKey));
            Registry.register(Registries.ITEM, itemKey, blockItem);
        }
        BLOCKS.register(name, block);
        Registry.register(Registries.BLOCK, blockKey, block.block());
        if(block instanceof QuiptBlockWithEntity){
            ENTITY_TYPES.register(name, Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    Identifier.of("quipt", name),
                    FabricBlockEntityTypeBuilder.create(entityFactory, block.block()).build()
            ));
        }
        return block;
    }

    private static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("quipt", name));
    }

    private static RegistryKey<Item> keyOfItem(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of("quipt", name));
    }

    public static com.quiptmc.core.data.registries.Registry<QuiptBlock> blocks() {
        return BLOCKS;
    }
}
