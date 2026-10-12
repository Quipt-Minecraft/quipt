package live.qsmc.quipt.minecraft.config.factories;

import live.qsmc.quipt.core.config.objects.ConfigObject;
import live.qsmc.quipt.minecraft.config.objects.ConfigLocation;

public class ConfigLocationFactory implements ConfigObject.Factory<ConfigLocation> {
    @Override
    public String getClassName() {
        return ConfigLocation.class.getName();
    }

}
