package live.qsmc.core.config.files;

import live.qsmc.core.QuiptIntegration;
import live.qsmc.core.config.Config;
import live.qsmc.core.config.ConfigTemplate;
import live.qsmc.core.config.ConfigValue;
import live.qsmc.core.data.JsonSerializable;

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
