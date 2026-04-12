package com.quiptmc.paper2.api.settings;

import com.quiptmc.core2.QuiptIntegration;
import com.quiptmc.core2.config.objects.ConfigMap;
import com.quiptmc.core2.config.objects.ConfigObject;

public class Settings extends ConfigObject {

    ConfigMap<Setting> settings;

    public Settings(QuiptIntegration integration) {
        super(integration);
        settings = new ConfigMap<>(integration);
    }
}
