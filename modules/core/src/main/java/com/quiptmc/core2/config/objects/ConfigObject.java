package com.quiptmc.core2.config.objects;

import com.quiptmc.core.data.JsonSerializable;
import com.quiptmc.core2.QuiptIntegration;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

public abstract class ConfigObject implements JsonSerializable {

    public String id;
    public String className;
    public final QuiptIntegration integration;

    public ConfigObject(QuiptIntegration integration) {
        this.integration = integration;
        this.className = getClass().getName();
    }

    public ConfigObject(QuiptIntegration integration, JSONObject json) {
        this.integration = integration;
        fromJson(json);
    }

    public QuiptIntegration integration() {
        return integration;
    }


    public String id() {
        return id;
    }


    public interface Factory<T extends ConfigObject> {
        String getClassName();
        default T createFromJson(QuiptIntegration integration, JSONObject json){
            try {
                T instance = (T) Class.forName(getClassName()).getDeclaredConstructor(QuiptIntegration.class).newInstance(integration);
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
