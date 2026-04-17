package live.qsmc.core.config.factories;

import live.qsmc.core.config.ConfigObject;
import live.qsmc.core.config.objects.ConfigString;
import org.json.JSONObject;

public class ConfigStringFactory implements ConfigObject.Factory<ConfigString> {
    @Override
    public String getClassName() {
        return ConfigString.class.getName();
    }

    @Override
    public ConfigString createFromJson(JSONObject json) {
        return new ConfigString(json);
    }
}
