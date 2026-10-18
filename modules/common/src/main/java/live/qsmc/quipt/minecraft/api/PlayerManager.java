package live.qsmc.quipt.minecraft.api;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.Config;
import live.qsmc.quipt.core.config.ConfigTemplate;
import live.qsmc.quipt.core.config.ConfigValue;
import live.qsmc.quipt.core.config.objects.ConfigMap;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;

import java.io.File;
import java.util.function.Function;

public abstract class PlayerManager<I, K, P extends Audience, O extends MinecraftPlayer> {


    private final MinecraftIntegration<I, K> integration;
    private final PlayerFactory<P, O> playerFactory;


    private final PlayerConfig config;

    public PlayerManager(MinecraftIntegration<I, K> integration, PlayerFactory<P, O> playerFactory){
        this.config = Quipt.INSTANCE.configs().register(PlayerConfig.class);
        this.integration = integration;
        this.playerFactory = playerFactory;
    }

    public O of(P player){
        return config.players.contains(player.get(Identity.UUID).toString()) ? config.players.get(player.get(Identity.UUID).toString()) : register(player);
    }

    private O register(P player){
        O playerData = playerFactory.apply(player);
        config.players.put(playerData);
        config.save();
        return playerData;
    }

    public void save(){
        config.save();
    }

    @ConfigTemplate(name = "players", ext = ConfigTemplate.Extension.JSON)
    public class PlayerConfig extends Config {

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
}
