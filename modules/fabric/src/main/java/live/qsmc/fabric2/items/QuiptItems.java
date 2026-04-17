package live.qsmc.fabric2.items;

public class QuiptItems {

//    private static final live.qsmc.core.data.registries.Registry<QuiptItem> items = live.qsmc.core.data.registries.Registries.register("items", ()->null);
//
//    public static QuiptItem get(String name) {
//        return items.get(name).orElseThrow(() -> new IllegalArgumentException("Block " + name + " not found"));
//    }
//    public static QuiptItem register(String name, Function<Item.Settings, QuiptItem> itemFactory, Item.Settings settings) {
//        // Create the item key.
//        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of("quipt", name));
//
//        // Create the item instance.
//        QuiptItem item = itemFactory.apply(settings.registryKey(itemKey));
//
//        // Register the item.
//        items.register(name, item);
//        Registry.register(Registries.ITEM, itemKey, item.item());
//
//        return item;
//    }
}
