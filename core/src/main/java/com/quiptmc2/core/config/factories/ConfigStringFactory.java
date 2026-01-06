package com.quiptmc2.core.config.factories;

import com.quiptmc2.core.QuiptIntegration;
import com.quiptmc2.core.config.objects.ConfigObject;
import com.quiptmc2.core.config.objects.ConfigString;
import org.json.JSONObject;

public class ConfigStringFactory implements ConfigObject.Factory<ConfigString> {
    @Override
    public String getClassName() {
        return ConfigString.class.getName();
    }

    @Override
    public ConfigString createFromJson(QuiptIntegration integration, JSONObject json) {
        return new ConfigString(integration, json);
    }
}
