package com.quiptmc.core2.config.files;

import com.quiptmc.core2.QuiptIntegration;
import com.quiptmc.core2.config.Config;
import com.quiptmc.core2.config.ConfigTemplate;
import com.quiptmc.core2.config.ConfigValue;
import org.json.JSONObject;

import java.io.File;

import static com.quiptmc.core2.config.ConfigTemplate.Extension.QPT;

@ConfigTemplate(name = "message", ext = QPT)
public class MessagesConfig extends Config {

    @ConfigValue
    public JSONObject messages = new JSONObject();

    public MessagesConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }
}
