package com.quiptmc.core.config;

import com.quiptmc.core.data.JsonSerializable;
import org.json.JSONObject;

public class ConfigObject implements JsonSerializable {

    public String id;
    public String className;

    public ConfigObject() {
        this.className = this.getClass().getName();
    }


    public interface Factory<T extends ConfigObject> {
        String getClassName();
        T createFromJson(JSONObject json);
    }
}
