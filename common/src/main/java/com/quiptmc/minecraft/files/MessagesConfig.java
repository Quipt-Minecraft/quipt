package com.quiptmc.minecraft.files;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.storage.Config;
import com.quiptmc.core.storage.ConfigTemplate;
import com.quiptmc.core.storage.ConfigValue;
import org.json.JSONObject;

import java.io.File;

import static com.quiptmc.core.storage.ConfigTemplate.Extension.QPT;

@ConfigTemplate(name = "message", ext = QPT)
public class MessagesConfig extends Config {

    @ConfigValue
    public JSONObject messages = new JSONObject();

    public MessagesConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }
}
