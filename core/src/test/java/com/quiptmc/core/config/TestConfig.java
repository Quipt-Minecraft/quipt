package com.quiptmc.core.config;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.objects.JsonTest;

import java.io.File;

@ConfigTemplate(name = "test")
public class TestConfig extends Config {

    @ConfigValue
    public JsonTest jsonTest = new JsonTest("test",3);

    /**
     * Creates a new config file
     *
     * @param file        The file to save to
     * @param name        The name of the config
     * @param extension   The extension of the config
     * @param integration The plugin that owns this config
     */
    public TestConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }
}
