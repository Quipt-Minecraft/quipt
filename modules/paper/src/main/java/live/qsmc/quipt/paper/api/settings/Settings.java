package live.qsmc.quipt.paper.api.settings;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.objects.ConfigMap;
import live.qsmc.quipt.core.config.objects.ConfigObject;

public class Settings extends ConfigObject {

    ConfigMap<Setting> settings;

    public Settings(QuiptIntegration integration) {
        super(integration);
        settings = new ConfigMap<>(integration);
    }
}
