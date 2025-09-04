package com.quiptmc.core.config;

import com.quiptmc.core.data.JsonSerializable;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

public class ConfigObject implements JsonSerializable {

    public String id;
    public String className;

    public ConfigObject() {
        this.className = this.getClass().getName();
    }


    public String id() {
        return id;
    }


    public interface Factory<T extends ConfigObject> {
        String getClassName();
        default T createFromJson(JSONObject json){
            try {
                T instance = (T) Class.forName(getClassName()).getDeclaredConstructor().newInstance();
                instance.fromJson(json);
                return instance;
            } catch (InstantiationException | IllegalAccessException |
                     NoSuchMethodException | ClassNotFoundException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
