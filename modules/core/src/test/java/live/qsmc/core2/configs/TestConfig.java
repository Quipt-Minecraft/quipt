package live.qsmc.core2.configs;

import live.qsmc.core2.QuiptIntegration;
import live.qsmc.core2.config.Config;
import live.qsmc.core2.config.ConfigTemplate;
import live.qsmc.core2.config.ConfigValue;

import java.io.File;

@ConfigTemplate(name = "test_config", ext = ConfigTemplate.Extension.JSON)
public class TestConfig extends Config {


    @ConfigValue
    public String test = "test";

    @ConfigValue
    public int number = 1;

    @ConfigValue
    public boolean bool = true;

    public TestConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }
}
