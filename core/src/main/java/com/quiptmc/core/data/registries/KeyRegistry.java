package com.quiptmc.core.data.registries;

import java.util.Optional;

/**
 * A registry for managing {@link RegistryKey} objects.
 */
public class KeyRegistry extends Registry<RegistryKey> {

    /**
     * Constructs a new KeyRegistry.
     */
    public KeyRegistry() {
        super(RegistryKey.class);
    }

    /**
     * Registers a new {@link RegistryKey} with the given ID.
     *
     * @param id the ID of the key to register
     * @return an {@link Optional} containing the registered {@link RegistryKey}, or an empty {@link Optional} if the key could not be registered
     */
    public Optional<RegistryKey> register(String id) {
        register(id, new RegistryKey(id));
        return get(id);
    }
}