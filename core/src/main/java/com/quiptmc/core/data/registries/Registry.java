package com.quiptmc.core.data.registries;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A generic registry for managing objects of type T.
 *
 * @param <T> the type of objects to be managed by this registry
 */
public class Registry<T> {

    /**
     * The internal map storing the registry entries.
     */
    private final Map<String, T> registry = new HashMap<>();

    /**
     * The class type of the objects managed by this registry.
     */
    private final Class<T> type;

    /**
     * Constructs a new Registry for the specified type.
     *
     * @param type the class type of the objects to be managed by this registry
     */
    public Registry(Class<T> type) {
        this.type = type;
    }

    /**
     * Returns the class type of the objects managed by this registry.
     *
     * @return the class type of the objects managed by this registry
     */
    public Class<T> type() {
        return type;
    }

    /**
     * Clears all entries from the registry.
     */
    public void clear() {
        registry.clear();
    }

    /**
     * Reloads the registry by clearing all entries.
     */
    public void reload() {
        clear();
    }

    /**
     * Returns the internal map of the registry.
     *
     * @return the internal map of the registry
     */
    public Map<String, T> toMap() {
        return registry;
    }

    /**
     * Retrieves an entry from the registry by its key, or returns a default value if the key is not found.
     *
     * @param key the key of the entry to retrieve
     * @param defaultValue the default value to return if the key is not found
     * @return the entry associated with the key, or the default value if the key is not found
     */
    public T getOrDefault(String key, T defaultValue) {
        return registry.getOrDefault(key, defaultValue);
    }

    /**
     * Registers a new entry in the registry with the given ID.
     *
     * @param id the ID of the entry to register
     * @param t the entry to register
     */
    public void register(String id, T t) {
        registry.put(id, t);
    }

    /**
     * Registers all public static fields of the specified class in the registry.
     *
     * @param clazz the class containing the fields to register
     */
    public void registerAll(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
                try {
                    register(field.getName(), (T) field.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Applies the specified consumer to each entry in the registry.
     *
     * @param consumer the consumer to apply to each entry
     */
    public void forEach(Consumer<T> consumer) {
        registry.forEach((s, t) -> consumer.accept(t));
    }

    /**
     * Retrieves an entry from the registry by its key.
     *
     * @param key the key of the entry to retrieve
     * @return an Optional containing the entry if found, or an empty Optional if not found
     */
    public Optional<T> get(String key) {
        return Optional.ofNullable(getOrDefault(key, null));
    }

    /**
     * Returns the number of entries in the registry.
     *
     * @return the number of entries in the registry
     */
    public int size() {
        return registry.size();
    }
}