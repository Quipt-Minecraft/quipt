package com.quiptmc.core.config;

import com.quiptmc.core.data.JsonSerializable;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ConfigMap<T extends ConfigObject> implements JsonSerializable {

    Map<String, T> configMap = new HashMap<>();

    public Collection<T> values() {
        return configMap.values();
    }

    public int size() {
        return configMap.size();
    }

    public boolean isEmpty() {
        return configMap.isEmpty();
    }

    public T get(String id) {
        return configMap.getOrDefault(id, null);
    }

    public void remove(String id) {
        configMap.remove(id);
    }

    public void remove(T configObject) {
        configMap.remove(configObject.id);
    }

    public void put(T configObject) {
        configMap.put(configObject.id, configObject);
    }

    public boolean contains(String id) {
        return get(id) != null;
    }

    public boolean contains(T configObject) {
        return configMap.containsValue(configObject);
    }

    public boolean containsAll(T[] configObjects) {
        for (T obj : configObjects) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }


    public void fromJson(JSONObject json) {
        for(String key : json.keySet()) {
            Object e = json.get(key);
            if (e instanceof JSONObject obj) {
                try {
                    String className = obj.getString("className");
                    ConfigObject.Factory<T> factory = (ConfigObject.Factory<T>) ConfigManager.factories().get(className);

                    if (factory == null) {
                        System.err.println("No registered factory for class: " + className);
                        continue;
                    }

                    T t = factory.createFromJson(obj);
                    this.configMap.put(key, t);
                } catch (Exception ex) {
                    System.err.println("Error creating instance: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public JSONObject json() {
        JSONObject json = new JSONObject();
        for (String key : configMap.keySet()) {
            json.put(key, configMap.get(key).json());
        }
        return json;
    }


}
