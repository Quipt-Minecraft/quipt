package live.qsmc.quipt.core.configs;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.Config;
import live.qsmc.quipt.core.config.ConfigTemplate;
import live.qsmc.quipt.core.config.ConfigValue;

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
