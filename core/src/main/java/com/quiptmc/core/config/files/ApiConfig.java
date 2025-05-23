package com.quiptmc.core.config.files;


import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.config.Config;
import com.quiptmc.core.config.ConfigTemplate;
import com.quiptmc.core.config.ConfigValue;

import java.io.File;
import java.util.UUID;

@ConfigTemplate(name = "api")
public class ApiConfig extends Config {

    @ConfigValue
    public String endpoint = "https://quipt-api.azurewebsites.net/api";

//    @ConfigValue
//    public String endpoint = "https://api.quiptmc.com";

    @ConfigValue
    public String secret = UUID.randomUUID().toString();

    @ConfigValue
    public String id = "default";

    public ApiConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }
}
