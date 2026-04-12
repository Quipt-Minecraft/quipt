package com.quiptmc.core2.data.registries;

import com.quiptmc.core2.QuiptIntegration;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Registries {

    private final Map<RegistryKey, Registry<?>> registries = new HashMap<>();
    private final Map<String, RegistryKey> keys = new HashMap<>();

    public <T> Registry<T> register(String key, Supplier<T> defaultSupplier) {
        if (keys.containsKey(key)) throw new IllegalArgumentException("Key already registered: " + key);
        RegistryKey registryKey = new RegistryKey(key);
        keys.put(key, registryKey);
        Registry<T> registry = new Registry<>(registryKey);
        registries.put(registryKey, registry);
        return registry;
    }

    public RegistryKey key(String key) {

        if (!keys.containsKey(key))
            return null;

        return keys.get(key);
    }

    @Deprecated
    public Registry<?> get(String key) {
        return registries.get(key(key));
    }

    @Deprecated
    public <T> Registry<T> get(String key, Class<T> type) {
        return get(key(key), type);
    }


    public <T> Registry<T> get(RegistryKey key, Class<T> type) throws ClassCastException {
        return (Registry<T>) registries.get(key);
    }


    public Registry<?> get(RegistryKey key) {
        return registries.get(key);
    }

    public void reset() {
        for (Registry<?> registry : registries.values()) {
            registry.clear();
        }
        keys.clear();
    }

    public JSONObject dump() {
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

    public void dump(QuiptIntegration.Logger logger) {
        for (Map.Entry<String, RegistryKey> keyEntry : keys.entrySet()) {
            String skey = keyEntry.getKey();
            RegistryKey key = keyEntry.getValue();
            Registry<?> registry = registries.get(key);
            logger.log("Registries", "Dumping Registry: " + skey);
            registry.forEach((s, instance) -> {
                logger.log("Registries", "Key: '" + skey + ":" + s + "'");
                logger.log("Registries", "Value: " + instance.toString());
            });
        }
    }
}