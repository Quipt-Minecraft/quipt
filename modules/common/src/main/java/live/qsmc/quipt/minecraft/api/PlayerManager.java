package live.qsmc.quipt.minecraft.api;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.factories.ConfigStringFactory;
import live.qsmc.quipt.core.config.factories.GenericFactory;
import live.qsmc.quipt.core.config.objects.ConfigObject;
import live.qsmc.quipt.minecraft.MinecraftIntegration;
import live.qsmc.quipt.minecraft.config.files.PlayerConfig;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public abstract class PlayerManager<P extends Audience, O extends MinecraftPlayer<P>> {


    private final QuiptIntegration integration;
    private final Function<P, O> playerConverter;


    private final PlayerConfig<O> config;

    public PlayerManager(QuiptIntegration integration, Function<P, O> playerConverter, ConfigObject.Factory<O> playerConfigFactory){
        Quipt.INSTANCE.configs().factory(playerConfigFactory);
        this.config = Quipt.INSTANCE.configs().register(PlayerConfig.class);
        this.integration = integration;
        this.playerConverter = playerConverter;
    }

    public O of(P player){
        Optional<UUID> uuid = player.get(Identity.UUID);
        return config.players.contains(uuid.orElseThrow().toString()) ?
                config.players.get(uuid.orElseThrow().toString()) :
                register(player);
    }

    private O register(P player){
        O playerData = playerConverter.apply(player);
        config.players.put(playerData);
        config.save();
        return playerData;
    }

    public void save(){
        config.save();
    }


}
