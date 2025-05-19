package com.quiptmc.core.data.registries;

import com.quiptmc.core.logger.QuiptLogger;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Registries {

    private final static Map<RegistryKey, Registry<?>> registries = new HashMap<>();
    private final static Map<String, RegistryKey> keys = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(Registries.class);

    public static <T> Registry<T> register(String key, Supplier<T> defaultSupplier) {
        if (keys.containsKey(key)) throw new IllegalArgumentException("Key already registered: " + key);
        RegistryKey registryKey = new RegistryKey(key);
        keys.put(key, registryKey);
        Registry<T> registry = new Registry<>(registryKey);
        registries.put(registryKey, registry);
        return registry;
    }

    public static RegistryKey key(String key) {

        if (!keys.containsKey(key)) throw new IllegalArgumentException("Key not registered: " + key);
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

    public static JSONObject dump() {
        JSONObject root = new JSONObject();
        for (Map.Entry<String, RegistryKey> keyEntry : keys.entrySet()) {
            String skey = keyEntry.getKey();
            RegistryKey key = keyEntry.getValue();
            Registry<?> registry = registries.get(key);
            JSONObject json = new JSONObject();
            registry.forEach((s, instance) -> {
                json.put(s, instance.toString());
            });
            root.put(skey, json);
        }
        return root;
    }

    public static void dump(QuiptLogger logger) {
        for (Map.Entry<String, RegistryKey> keyEntry : keys.entrySet()) {
            String skey = keyEntry.getKey();
            RegistryKey key = keyEntry.getValue();
            Registry<?> registry = registries.get(key);
            logger.log("Dumping Registry: " + skey);
            registry.forEach((s, instance) -> {
                logger.log("Key: '" + skey + ":" + s + "'");
                logger.log("Value: " + instance.toString());
            });
        }
    }
}