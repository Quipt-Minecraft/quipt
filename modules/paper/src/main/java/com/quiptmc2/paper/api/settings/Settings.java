package com.quiptmc2.paper.api.settings;

import com.quiptmc2.core.QuiptIntegration;
import com.quiptmc2.core.config.objects.ConfigMap;
import com.quiptmc2.core.config.objects.ConfigObject;
import com.quiptmc2.core.data.JsonSerializable;

public class Settings extends ConfigObject {

    ConfigMap<Setting> settings;

    public Settings(QuiptIntegration integration) {
        super(integration);
        settings = new ConfigMap<>(integration);
    }
}
