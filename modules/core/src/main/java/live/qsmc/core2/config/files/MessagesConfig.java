package live.qsmc.core2.config.files;

import live.qsmc.core2.QuiptIntegration;
import live.qsmc.core2.config.Config;
import live.qsmc.core2.config.ConfigTemplate;
import live.qsmc.core2.config.ConfigValue;
import org.json.JSONObject;

import java.io.File;

import static live.qsmc.core2.config.ConfigTemplate.Extension.QPT;

@ConfigTemplate(name = "message", ext = QPT)
public class MessagesConfig extends Config {

    @ConfigValue
    public JSONObject messages = new JSONObject();

    public MessagesConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }
}
