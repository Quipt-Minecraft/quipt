package live.qsmc.minecraft2.api;

import live.qsmc.core2.Quipt;
import live.qsmc.core2.data.Metadata;
import live.qsmc.core2.QuiptIntegration;
import live.qsmc.core2.config.factories.GenericFactory;
import live.qsmc.minecraft2.config.files.PartyConfig;
import live.qsmc.minecraft2.events.party.Party;
import live.qsmc.minecraft2.server.ResourcePackHandler;

import java.io.File;

public abstract class MinecraftIntegration<T> extends QuiptIntegration {

    private PartyConfig parties = null;
    private final Metadata metadata;
    private final String name;
    private final String version;
    private final File folder;
    private final T instance;

    public MinecraftIntegration(Metadata metadata, T instance) {
        this.metadata = metadata;
        this.instance = instance;
        this.name = metadata.value("name", String.class);
        this.version = metadata.value("version", String.class);
        this.folder = metadata.value("folder", File.class);
    }

    public T instance() {
        return instance;
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

}
