package live.qsmc.quipt.minecraft.api;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.config.factories.GenericFactory;
import live.qsmc.quipt.minecraft.MinecraftIntegration;
import live.qsmc.quipt.minecraft.config.files.PlayerConfig;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;

public abstract class PlayerManager<I, K, P extends Audience, O extends MinecraftPlayer> {


    private final MinecraftIntegration<I, K> integration;
    private final PlayerFactory<P, O> playerFactory;


    private final PlayerConfig<O> config;

    public PlayerManager(MinecraftIntegration<I, K> integration, PlayerFactory<P, O> playerFactory, Class<O> minecraftPlayerClass){
        this.config = Quipt.INSTANCE.configs().register(PlayerConfig.class);
        this.integration = integration;
        this.playerFactory = playerFactory;
        integration.configs().factory(new GenericFactory<>(minecraftPlayerClass));
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


}
