package com.quiptmc.core.data.registries;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Registries {

    private final static Map<RegistryKey, Registry<?>> registries = new HashMap<>();
    private final static Map<String, RegistryKey> keys = new HashMap<>();

    public static <T> Registry<T> register(String key, Class<T> registryTypeClass) {
        if(keys.containsKey(key)) throw new IllegalArgumentException("Key already registered: " + key);
        RegistryKey registryKey = new RegistryKey(key);
        keys.put(key, registryKey);
        Registry<T> registry = new Registry<>(registryKey, registryTypeClass);
        registries.put(registryKey, registry);
        return registry;
    }
    public static RegistryKey key(String key) {
        if(!keys.containsKey(key)) throw new IllegalArgumentException("Key not registered: " + key);
        return keys.get(key);
    }

    @Deprecated
    public static Registry<?> get(String key) {
        return registries.get(key(key));
    }

    @Deprecated
    public static <T> Registry<T> get(String key, Class<T> type) {
        return get(key(key), type);
    }


    public static <T> Registry<T> get(RegistryKey key, Class<T> type) throws ClassCastException {
        return (Registry<T>) registries.get(key);
    }


    public static Registry<?> get(RegistryKey key) {
        return registries.get(key);
    }

    public static void reset() {
        for (Registry<?> registry : registries.values()) {
            registry.clear();
        }
        keys.clear();
    }

//
//
//
//
//
//
//    /**
//     * Singleton instance of the Registries class.
//     */
////    public static final Registries REGISTRAR = new Registries();
//
//    /**
//     * Singleton instance of the KeyRegistry class.
//     */
//    public static final KeyRegistry KEYS = new KeyRegistry();
//
//    /**
//     * Map to store registries with their corresponding keys.
//     */
//    private static final Map<RegistryKey, Registry<?>> registries = new HashMap<>();
//
//
//
//    /**
//     * Adds a registry to the registries map with the given key.
//     *
//     * @param key the key to associate with the registry
//     * @param registry the registry to add
//     * @param <T> the type of the registry
//     * @return the added registry
//     */
//    public static <T> Registry<T> add(RegistryKey key, Registry<T> registry) {
//        registries.put(key, registry);
//        return registry;
//    }
//
//    public static <T> Registry<T> register(String key, Class<T> registryTypeClass) {
//        KEYS.register(key);
//        RegistryKey rkey = KEYS.get(key).orElseThrow();
//        return add(rkey, new Registry<T>(registryTypeClass));
//    }
//
//    /**
//     * Retrieves a registry associated with the given key.
//     *
//     * @param key the key of the registry to retrieve
//     * @return the registry associated with the key, or null if not found
//     */
//    public static Registry<?> get(RegistryKey key) {
//        return registries.getOrDefault(key, null);
//    }
//
//    /**
//     * Retrieves a registry associated with the given key and type.
//     *
//     * @param key the key of the registry to retrieve
//     * @param type the class type of the registry
//     * @param <T> the type of the registry
//     * @return an Optional containing the registry if found, or an empty Optional if not found
//     */
//    public static <T> Optional<Registry<T>> get(RegistryKey key, Class<T> type) {
//        return Optional.ofNullable((Registry<T>) registries.get(key));
//    }
}