package com.quiptmc2.minecraft.api;

import com.quiptmc.core.data.Metadata;
import com.quiptmc.minecraft.web.ResourcePackHandler;
import com.quiptmc2.core.QuiptIntegration;
import com.quiptmc2.core.config.factories.GenericFactory;
import com.quiptmc2.minecraft.config.files.PartyConfig;
import com.quiptmc2.minecraft.events.party.Party;
import com.quiptmc2.minecraft.utils.chat.MessageUtils;

import java.io.File;

public abstract class MinecraftIntegration<T> extends QuiptIntegration {

    private MessageUtils messages = null;
    private ResourcePackHandler packHandler = null;
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

    public MessageUtils messages(){
        if(messages == null){
            logger().log(name() + "-Messages", "Initializing Messages...");
            messages = new MessageUtils(this);
        }
        return messages;
    }

    public ResourcePackHandler packHandler() {
        if(packHandler == null){
            logger().log(name() + "-ResourcePackHandler", "Initializing Resource Pack Handler...");
            packHandler = new ResourcePackHandler(server());
            server().handler().handle("resources", packHandler, "resources/*");
//            packHandler.setUrl(resourceConfig.repo_url);
        }
        return packHandler;
    }

    public PartyConfig parties(){
        if(parties == null){
            logger().log(name() + "-Parties", "Initializing Party Handler...");
            if(configs().config(PartyConfig.class) == null){
                logger().log(name() + "-Parties", "Initializing Webhook Config...");
                configs().factory(new GenericFactory<>(Party.class));
                parties = configs().register(PartyConfig.class);
            }

        }
        return parties;
    }

}
