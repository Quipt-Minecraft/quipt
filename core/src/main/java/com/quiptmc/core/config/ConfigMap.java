package com.quiptmc.core.config;

import com.quiptmc.core.data.JsonSerializable;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ConfigMap<T extends ConfigObject> implements JsonSerializable {

    Map<String, T> configMap = new HashMap<>();

    @Override
    public JSONObject json() {
        JSONObject json = new JSONObject();
        for (String key : configMap.keySet()) {
            json.put(key, configMap.get(key).json());
        }
        return json;
    }

    @Override
    public void fromJson(JSONObject json) {
        for(String key : json.keySet()) {
            Object e = json.get(key);
            if (e instanceof JSONObject obj) {
                try {
                    // Try multiple classloaders to find the class
                    Class<T> tClass;
                    String className = obj.getString("className");
                    try {
                        // Try the current class loader
                        tClass = (Class<T>) Class.forName(className);
                    } catch (ClassNotFoundException e1) {
                        try {
                            // Try the thread context class loader
                            tClass = (Class<T>) Thread.currentThread().getContextClassLoader().loadClass(className);
                        } catch (ClassNotFoundException e2) {
                            // Log the error and skip this entry
                            System.err.println("Could not find class: " + className + ". Skipping this entry.");
                            continue;
                        }
                    }

                    T t = (T)(tClass.getDeclaredConstructor().newInstance());
                    t.fromJson(obj);
                    this.configMap.put(key, t);
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e1) {
                    System.err.println("Error creating instance of class: " + obj.getString("className"));
                    e1.printStackTrace();
                }
            }
        }
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
