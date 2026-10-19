package live.qsmc.quipt.minecraft;

import live.qsmc.quipt.core.data.Metadata;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.factories.GenericFactory;
import live.qsmc.quipt.minecraft.api.MinecraftPlayer;
import live.qsmc.quipt.minecraft.api.PlayerManager;
import live.qsmc.quipt.minecraft.config.files.PartyConfig;
import live.qsmc.quipt.minecraft.events.party.Party;
import net.kyori.adventure.audience.Audience;

import java.io.File;
import java.util.function.Function;

public abstract class MinecraftIntegration<T, I, M extends PlayerManager<P, O>, P extends Audience, O extends MinecraftPlayer<P>> extends QuiptIntegration {

    private PartyConfig parties = null;
    private final Metadata metadata;
    private final String name;
    private final String id;
    private final String version;
    private final File folder;
    private final T instance;
    private M players = null;
    private final Function<QuiptIntegration, M> playerManager;


    public MinecraftIntegration(Metadata metadata, T instance, Function<QuiptIntegration, M> playerManager) {
        this.metadata = metadata;
        this.instance = instance;
        this.playerManager = playerManager;
        this.name = metadata.value("name", String.class);
        this.id = metadata.value("id", String.class);
        this.version = metadata.value("version", String.class);
        this.folder = metadata.value("folder", File.class);
    }

    public M players() {
        if (players == null) {
            logger().log("Players", "Initializing players...");
            players = playerManager.apply(this);
        }
        return players;
    }

    public T instance() {
        return instance;
    }

    public String id() {
        return id;
    }

    public Metadata metadata() {
        return metadata;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String version() {
        return version;
    }

    @Override
    public File folder() {
        return folder;
    }



    public PartyConfig parties(){
        if(parties == null){
            logger().log("Parties", "Initializing Party Handler...");
            if(configs().config(PartyConfig.class) == null){
                logger().log("Parties", "Initializing Webhook Config...");
                configs().factory(new GenericFactory<>(Party.class));
            }
            parties = configs().register(PartyConfig.class);

        }
        return parties;
    }

    public abstract I identifier(String id);


    public abstract File addons();
}
