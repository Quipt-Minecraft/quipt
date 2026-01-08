package com.quiptmc2.core.config.objects;

import com.quiptmc2.core.QuiptIntegration;
import org.json.JSONObject;

public class ConfigString extends ConfigObject {

    String value;
    public ConfigString(QuiptIntegration integration, String id, String value){
        super(integration);
        this.value = value;
        super.id = id;
    }

    public ConfigString(QuiptIntegration integration, JSONObject json){
        super(integration, json);
    }

    public String value(){
        return value;
    }

    @Override
    public String toString() {

        return value();
    }
}