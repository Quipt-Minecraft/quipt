package live.qsmc.quipt.minecraft.config.files;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.Config;
import live.qsmc.quipt.core.config.ConfigTemplate;
import live.qsmc.quipt.core.config.ConfigValue;
import live.qsmc.quipt.core.config.objects.ConfigMap;
import live.qsmc.quipt.minecraft.api.MinecraftPlayer;

import java.io.File;

@ConfigTemplate(name = "players", ext = ConfigTemplate.Extension.JSON)
public class PlayerConfig<O extends MinecraftPlayer> extends Config {

    @ConfigValue
    public ConfigMap<O> players;

    /**
     * Creates a new config file
     *
     * @param file        The file to save to
     * @param name        The name of the config
     * @param extension   The extension of the config
     * @param integration The plugin that owns this config
     */
    public PlayerConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
        players = new ConfigMap<>(integration);
    }
}