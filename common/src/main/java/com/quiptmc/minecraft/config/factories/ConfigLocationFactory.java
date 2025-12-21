package com.quiptmc.minecraft.config.factories;

import com.quiptmc.core.config.ConfigObject;
import com.quiptmc.minecraft.config.objects.ConfigLocation;
import org.json.JSONObject;

public class ConfigLocationFactory implements ConfigObject.Factory<ConfigLocation> {
    @Override
    public String getClassName() {
        return ConfigLocation.class.getName();
    }

    @Override
    public ConfigLocation createFromJson(JSONObject json) {
        return new ConfigLocation(json);
    }
}
