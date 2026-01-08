package com.quiptmc.core.config.files;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.config.Config;
import com.quiptmc.core.config.ConfigTemplate;
import com.quiptmc.core.config.ConfigValue;
import com.quiptmc.core.data.JsonSerializable;

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
    public HealthReportConfig healthReport = new HealthReportConfig();

    public WebConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }

    public static class HealthReportConfig implements JsonSerializable {

        public boolean enable = true;
    }
}
