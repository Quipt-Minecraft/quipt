package com.quiptmc.core.config;

import com.quiptmc.core.QuiptIntegration;
import org.json.JSONObject;

public abstract class NestedConfig<T extends Config> extends Config{

    private final T parent;
    /**
     * Creates a new nested config file
     *
     * @param name        The name of the config
     * @param integration The plugin that owns this config
     */
    public NestedConfig(T parent, String name, QuiptIntegration integration) {
        super(null, name, null, integration);
        this.parent = parent;
    }

    public Config parent() {
        return parent;
    }

    @Override
    public void save() {
        parent.save();
    }

    @Override
    public JSONObject json() {
        JSONObject json = super.json();
        if (json.has("version"))
            json.remove("version");

        return json;
    }
}
