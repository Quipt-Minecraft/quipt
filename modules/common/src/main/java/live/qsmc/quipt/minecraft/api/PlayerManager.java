package live.qsmc.quipt.minecraft.api;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.config.objects.ConfigObject;
import live.qsmc.quipt.minecraft.config.files.PlayerConfig;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public abstract class PlayerManager<P extends Audience, O extends MinecraftPlayer<P>> {

    private final Function<UUID, O> defaultGenerator;
    private final Function<P, O> playerConverter;
    private final PlayerConfig<O> config;

    public PlayerManager(Function<UUID, O> defaultGenerator, Function<P, O> playerConverter, ConfigObject.Factory<O> playerConfigFactory) {
        Quipt.INSTANCE.configs().factory(playerConfigFactory);
        this.config = Quipt.INSTANCE.configs().register(PlayerConfig.class);
        this.playerConverter = playerConverter;
        this.defaultGenerator = defaultGenerator;
    }

    public O of(P player) {
        Optional<UUID> uuid = player.get(Identity.UUID);
        return of(uuid.orElseThrow());
    }

    public O of(UUID uuid) {
        return config.players.contains(uuid.toString()) ?
                config.players.get(uuid.toString()) :
                register(uuid);
    }

    private O register(UUID uuid) {
        return save(defaultGenerator.apply(uuid));
    }

    private O register(P player) {
        return save(playerConverter.apply(player));

    }

    private O save(O playerData) {
        config.players.put(playerData);
        config.save();
        return playerData;
    }

    public void save() {
        config.save();
    }

    public PlayerConfig<O> config() {
        return config;
    }


}
