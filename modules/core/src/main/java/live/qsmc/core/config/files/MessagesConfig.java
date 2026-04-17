package live.qsmc.core.config.files;

import live.qsmc.core.QuiptIntegration;
import live.qsmc.core.config.Config;
import live.qsmc.core.config.ConfigTemplate;
import live.qsmc.core.config.ConfigValue;
import org.json.JSONObject;

import java.io.File;

import static live.qsmc.core.config.ConfigTemplate.Extension.QPT;

@ConfigTemplate(name = "message", ext = QPT)
public class MessagesConfig extends Config {

    @ConfigValue
    public JSONObject messages = new JSONObject();

    public MessagesConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }
}
