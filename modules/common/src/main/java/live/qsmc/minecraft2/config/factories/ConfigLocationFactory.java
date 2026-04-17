package live.qsmc.minecraft2.config.factories;

import live.qsmc.core2.config.objects.ConfigObject;
import live.qsmc.minecraft2.config.objects.ConfigLocation;

public class ConfigLocationFactory implements ConfigObject.Factory<ConfigLocation> {
    @Override
    public String getClassName() {
        return ConfigLocation.class.getName();
    }

}
