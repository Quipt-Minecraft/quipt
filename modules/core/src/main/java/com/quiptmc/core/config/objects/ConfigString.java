package com.quiptmc.core.config.objects;

import com.quiptmc.core.config.ConfigObject;
import org.json.JSONObject;

public class ConfigString extends ConfigObject {

    String value;
    public ConfigString(String id, String value){
        this.value = value;
        super.id = id;
    }

    public ConfigString(JSONObject json){
        fromJson(json);
    }

    public String value(){
        return value;
    }

    @Override
    public String toString() {

        return value();
    }
}