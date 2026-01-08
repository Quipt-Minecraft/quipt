package com.quiptmc.tests.core.config;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.config.ConfigTemplate;

import java.io.File;

@ConfigTemplate(name = "test_config", ext = ConfigTemplate.Extension.JSON)
public class TestConfigJson extends TestConfig {
    /**
     * Creates a new config file
     *
     * @param file        The file to save to
     * @param name        The name of the config
     * @param extension   The extension of the config
     * @param integration The plugin that owns this config
     */
    public TestConfigJson(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }
}
