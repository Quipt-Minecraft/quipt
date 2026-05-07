package live.qsmc.quipt.core.config.files;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.Config;
import live.qsmc.quipt.core.config.ConfigTemplate;
import live.qsmc.quipt.core.config.ConfigValue;
import org.json.JSONObject;

import java.io.File;

import static live.qsmc.quipt.core.config.ConfigTemplate.Extension.JSON;

@ConfigTemplate(name = "message", ext = JSON)
public class MessagesConfig extends Config {

    @ConfigValue
    public JSONObject messages = new JSONObject();

    public MessagesConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }
}
