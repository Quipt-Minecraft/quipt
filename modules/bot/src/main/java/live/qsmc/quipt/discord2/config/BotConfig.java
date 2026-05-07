package live.qsmc.quipt.discord2.config;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.Config;
import live.qsmc.quipt.core.config.ConfigTemplate;
import live.qsmc.quipt.core.config.ConfigValue;

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
