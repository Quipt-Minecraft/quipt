package com.quiptmc2.minecraft.config.factories;

import com.quiptmc2.core.config.objects.ConfigObject;
import com.quiptmc2.minecraft.config.objects.ConfigLocation;
import org.json.JSONObject;

public class ConfigLocationFactory implements ConfigObject.Factory<ConfigLocation> {
    @Override
    public String getClassName() {
        return ConfigLocation.class.getName();
    }

}
