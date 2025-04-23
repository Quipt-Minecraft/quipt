package com.quiptmc.tests.core.config;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.storage.Config;
import com.quiptmc.core.storage.ConfigTemplate;
import com.quiptmc.core.storage.ConfigValue;
import com.quiptmc.core.storage.NestedConfig;
import org.json.JSONArray;

@ConfigTemplate(name = "nested")
public class TestNestedConfig<T extends Config> extends NestedConfig<T> {

    @ConfigValue
    public String nestedValue = "nestedValue";

    @ConfigValue
    public int nestedInt = 1;

    @ConfigValue
    public JSONArray nestedTestArray = new JSONArray().put(1).put("me/quickscythe/quipt/test");


    /**
     * Creates a new config file
     *
     * @param parent      The Config instance that holds this nested config.
     * @param name        The name of the config
     * @param integration The plugin that owns this config
     */
    public TestNestedConfig(T parent, String name, QuiptIntegration integration) {
        super(parent, name, integration);
    }
}
