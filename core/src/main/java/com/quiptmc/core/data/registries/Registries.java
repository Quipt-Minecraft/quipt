package com.quiptmc.core.data.registries;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Registries {

    /**
     * Singleton instance of the Registries class.
     */
    public static final Registries REGISTRAR = new Registries();

    /**
     * Singleton instance of the KeyRegistry class.
     */
    public static final KeyRegistry KEYS = new KeyRegistry();

    /**
     * Map to store registries with their corresponding keys.
     */
    private final Map<RegistryKey, Registry<?>> registries = new HashMap<>();

    /**
     * Adds a registry to the registries map with the given key.
     *
     * @param key the key to associate with the registry
     * @param registry the registry to add
     * @param <T> the type of the registry
     * @return the added registry
     */
    public <T> Registry<T> add(RegistryKey key, Registry<T> registry) {
        registries.put(key, registry);
        return registry;
    }

    /**
     * Retrieves a registry associated with the given key.
     *
     * @param key the key of the registry to retrieve
     * @return the registry associated with the key, or null if not found
     */
    public Registry<?> get(RegistryKey key) {
        return registries.getOrDefault(key, null);
    }

    /**
     * Retrieves a registry associated with the given key and type.
     *
     * @param key the key of the registry to retrieve
     * @param type the class type of the registry
     * @param <T> the type of the registry
     * @return an Optional containing the registry if found, or an empty Optional if not found
     */
    public <T> Optional<Registry<T>> get(RegistryKey key, Class<T> type) {
        return Optional.ofNullable((Registry<T>) registries.get(key));
    }
}