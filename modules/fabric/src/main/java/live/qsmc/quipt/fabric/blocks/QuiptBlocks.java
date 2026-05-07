package live.qsmc.quipt.fabric.blocks;

public class QuiptBlocks {

//    public static final live.qsmc.core2.data.registries.Registry<QuiptBlock> BLOCKS = live.qsmc.core.data.registries.Registries.register("blocks", ()->null);
//    public static final live.qsmc.core2.data.registries.Registry<BlockEntityType<? extends BlockEntity>> ENTITY_TYPES = live.qsmc.core.data.registries.Registries.register("block_entity", ()->null);
//    public static final live.qsmc.core2.data.registries.Registry<BlockProperty<?>> BLOCK_PROPERTIES = live.qsmc.core.data.registries.Registries.register("block_properties", ()->null);

//    public static QuiptBlock get(String name) {
//        return BLOCKS.get(name).orElseThrow(() -> new IllegalArgumentException("Block " + name + " not found"));
//    }

//    public static BlockEntityType<? extends BlockEntity> type(String name) {
//        return ENTITY_TYPES.get(name).orElseThrow(() -> new IllegalArgumentException("BlockEntityType " + name + " not found"));
//    }

//    public static QuiptBlock register(String name, Function<AbstractBlock.Settings, QuiptBlock> blockFactory, AbstractBlock.Settings settings, boolean shouldRegisterItem, @Nullable  FabricBlockEntityTypeBuilder.Factory<?> entityFactory) {
//         Create a registry key for the block
//        RegistryKey<Block> blockKey = keyOfBlock(name);
//         Create the block instance
//        QuiptBlock block = blockFactory.apply(settings.registryKey(blockKey));

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
//        if (shouldRegisterItem) {
            // Items need to be registered with a different type of registry key, but the ID
            // can be the same.
//            RegistryKey<Item> itemKey = keyOfItem(name);

//            BlockItem blockItem = new BlockItem(block.block(), new Item.Settings().registryKey(itemKey));
//            Registry.register(Registries.ITEM, itemKey, blockItem);
//        }
//        BLOCKS.register(name, block);
//        Registry.register(Registries.BLOCK, blockKey, block.block());
//        if(block instanceof QuiptBlockWithEntity){
//            ENTITY_TYPES.register(name, Registry.register(
//                    Registries.BLOCK_ENTITY_TYPE,
//                    Identifier.of("quipt", name),
//                    FabricBlockEntityTypeBuilder.create(entityFactory, block.block()).build()
//            ));
//        }
//        return block;
//    }

//    private static RegistryKey<Block> keyOfBlock(String name) {
//        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("quipt", name));
//    }

//    private static RegistryKey<Item> keyOfItem(String name) {
//        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of("quipt", name));
//    }

//    public static live.qsmc.core.data.registries.Registry<QuiptBlock> blocks() {
//        return BLOCKS;
//    }
}
