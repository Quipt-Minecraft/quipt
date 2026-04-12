package com.quiptmc.discord2.config;

import com.quiptmc.core2.QuiptIntegration;
import com.quiptmc.core2.config.Config;
import com.quiptmc.core2.config.ConfigTemplate;
import com.quiptmc.core2.config.ConfigValue;

import java.io.File;

@ConfigTemplate(name = "bot", ext = ConfigTemplate.Extension.JSON)
public class BotConfig extends Config {
    @ConfigValue
    public String token = "<YOUR_BOT_TOKEN>";

    /**
     * Creates a new config file
     *
     * @param file        The file to save to
     * @param name        The name of the config
     * @param extension   The extension of the config
     * @param integration The plugin that owns this config
     */
    public BotConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }
}
