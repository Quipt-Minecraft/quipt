package com.quiptmc.minecraft2.config.factories;

import com.quiptmc.core2.config.objects.ConfigObject;
import com.quiptmc.minecraft2.config.objects.ConfigLocation;

public class ConfigLocationFactory implements ConfigObject.Factory<ConfigLocation> {
    @Override
    public String getClassName() {
        return ConfigLocation.class.getName();
    }

}
