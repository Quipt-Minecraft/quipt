package live.qsmc.core2.config.factories;

import live.qsmc.core2.QuiptIntegration;
import live.qsmc.core2.config.objects.ConfigObject;
import live.qsmc.core2.config.objects.ConfigString;
import org.json.JSONObject;

public class ConfigStringFactory implements ConfigObject.Factory<ConfigString> {
    @Override
    public String getClassName() {
        return ConfigString.class.getName();
    }

    @Override
    public ConfigString createFromJson(QuiptIntegration integration, JSONObject json) {
        json.remove("integration");
        return new ConfigString(integration, json);
    }
}
