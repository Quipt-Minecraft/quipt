package live.qsmc.core.config.files;


import live.qsmc.core.QuiptIntegration;
import live.qsmc.core.config.Config;
import live.qsmc.core.config.ConfigTemplate;
import live.qsmc.core.config.ConfigValue;

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
