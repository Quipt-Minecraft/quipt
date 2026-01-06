package com.quiptmc2.core.config.files;

import com.quiptmc2.core.QuiptIntegration;
import com.quiptmc2.core.config.Config;
import com.quiptmc2.core.config.ConfigTemplate;
import com.quiptmc2.core.config.ConfigValue;
import com.quiptmc2.core.config.objects.ConfigObject;

import java.io.File;

@ConfigTemplate(name = "quipt", ext = ConfigTemplate.Extension.JSON)
public class QuiptConfig extends Config {
    
    @ConfigValue
    public WebData webData;


    /**
     * Creates a new config file
     *
     * @param file        The file to save to
     * @param name        The name of the config
     * @param extension   The extension of the config
     * @param integration The plugin that owns this config
     */
    public QuiptConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
        webData = new WebData(integration);
    }
    
    public static class WebData extends ConfigObject {
        public String protocol = "HTTP";
        public String host = "127.0.0.1";
        public int port = 5252;

        public WebData(QuiptIntegration integration) {
            super(integration);
        }
    }
}
