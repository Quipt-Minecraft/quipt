package com.quiptmc.core.data.registries;

/**
 * The `RegistryKey` class represents a key used in a registry.
 */
public class RegistryKey {

    private String key;

    /**
     * Constructs a new `RegistryKey` with the specified key.
     *
     * @param key the key to be used in the registry
     */
    public RegistryKey(String key) {
        this.key = key;
    }

    /**
     * Returns the key of this `RegistryKey`.
     *
     * @return the key
     */
    public String key() {
        return key;
    }

    /**
     * Sets the key of this `RegistryKey`.
     *
     * @param key the new key
     */
    public void key(String key) {
        this.key = key;
    }
}