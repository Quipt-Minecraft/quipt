package live.qsmc.minecraft.config.factories;

import live.qsmc.core.config.ConfigObject;
import live.qsmc.minecraft.config.objects.ConfigLocation;
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
