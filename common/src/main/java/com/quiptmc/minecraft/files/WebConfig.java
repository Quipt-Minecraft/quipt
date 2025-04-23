package com.quiptmc.minecraft.files;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.storage.Config;
import com.quiptmc.core.storage.ConfigTemplate;
import com.quiptmc.core.storage.ConfigValue;
import com.quiptmc.minecraft.files.web.HealthReportNestedConfig;

import java.io.File;

@ConfigTemplate(name = "web")
public class WebConfig extends Config {

    @ConfigValue
    public boolean enable = true;

    @ConfigValue
    public int port = 5050;

    @ConfigValue
    public String host = "127.0.0.1";

    @ConfigValue
    public String webRoot = "web";

    @ConfigValue
    public HealthReportNestedConfig<WebConfig> healthreport = null;

    public WebConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }

    /**
     * @param file The file to save to
     * @param name The name of the config
     */
}
