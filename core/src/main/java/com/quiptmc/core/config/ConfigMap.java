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
        for (String key : json.keySet()) {
                if (json.get(key) instanceof JSONObject obj) {
                    try {
                        Class<T> tClass = (Class<T>) Class.forName(obj.getString("className"));
                        T t = tClass.getDeclaredConstructor().newInstance();
                        t.fromJson(obj);
                        configMap.put(key, t);
                    } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                             IllegalAccessException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
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
