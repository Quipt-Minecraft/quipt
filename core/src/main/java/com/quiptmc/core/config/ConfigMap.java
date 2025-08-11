package com.quiptmc.core.config;

import com.quiptmc.core.data.JsonSerializable;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ConfigMap<T extends ConfigObject> implements JsonSerializable {

    Map<String, T> configMap = new HashMap<>();


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

                    System.out.println(obj.toString(2));
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
}
