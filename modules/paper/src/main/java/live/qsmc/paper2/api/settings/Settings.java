package live.qsmc.paper2.api.settings;

import live.qsmc.core2.QuiptIntegration;
import live.qsmc.core2.config.objects.ConfigMap;
import live.qsmc.core2.config.objects.ConfigObject;

public class Settings extends ConfigObject {

    ConfigMap<Setting> settings;

    public Settings(QuiptIntegration integration) {
        super(integration);
        settings = new ConfigMap<>(integration);
    }
}
