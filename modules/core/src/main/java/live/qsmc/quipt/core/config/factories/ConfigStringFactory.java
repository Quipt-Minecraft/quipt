package live.qsmc.quipt.core.config.factories;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.objects.ConfigObject;
import live.qsmc.quipt.core.config.objects.ConfigString;
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
