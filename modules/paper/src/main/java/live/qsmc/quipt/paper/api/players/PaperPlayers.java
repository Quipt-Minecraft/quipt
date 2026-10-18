package live.qsmc.quipt.paper.api.players;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.Config;
import live.qsmc.quipt.core.config.ConfigTemplate;
import live.qsmc.quipt.core.config.objects.ConfigMap;
import live.qsmc.quipt.core.data.registries.Registry;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class PaperPlayers {

//    private final Registry<PaperPlayer> players;
    private final MinecraftIntegration<JavaPlugin, NamespacedKey> integration;
    private final PlayerConfig config;

    public PaperPlayers(MinecraftIntegration<JavaPlugin, NamespacedKey> integration){
        this.config = integration.configs().register(PlayerConfig.class);
//        players = Quipt.INSTANCE.registries().register("players", ()->null);
        this.integration = integration;
    }

    public PaperPlayer of(Player player){
        if(!config.players.contains(player.getUniqueId().toString()))
            return register(player);
        return config.players.get(player.getUniqueId().toString());
//        return players.get(player.getUniqueId().toString()).orElse(register(player));
    }

    private PaperPlayer register(Player player){
        PaperPlayer playerData = new PaperPlayer(integration, player);
        config.players.put(playerData);
        config.save();
        return playerData;
    }

    public void save(){
        config.save();
    }

    @ConfigTemplate(name = "players", ext = ConfigTemplate.Extension.JSON)
    public static class PlayerConfig extends Config {

        ConfigMap<PaperPlayer> players;

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
}
