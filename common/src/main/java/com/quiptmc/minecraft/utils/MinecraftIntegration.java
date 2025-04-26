package com.quiptmc.minecraft.utils;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.server.QuiptServer;
import com.quiptmc.minecraft.utils.heartbeat.HeartbeatUtils;
import com.quiptmc.minecraft.utils.loaders.ServerLoader;
import com.quiptmc.minecraft.web.CallbackHandler;
import com.quiptmc.minecraft.web.ResourcePackHandler;

import java.io.File;
import java.io.IOException;

public class MinecraftIntegration<T> extends QuiptIntegration {

    public final String NAME;
    private final File dataFolder;
    private final ServerLoader<?> loader;

    private ResourcePackHandler packHandler;
    private QuiptServer server;
    private CallbackHandler callbackHandler;

    public MinecraftIntegration(String name, ServerLoader<?> loader) {
        this.NAME = name;
        this.loader = loader;
        this.dataFolder = new File("plugins/" + name());
    }

    public void enable() {

        if (!dataFolder.exists())
            log("Initializer", "Attempting to create data folder (" + dataFolder.getPath() + "): " + dataFolder.mkdirs());
        HeartbeatUtils.init(this);

    }

    @Override
    public File dataFolder() {
        return dataFolder;
    }

    @Override
    public String name() {
        return NAME;
    }

    public ServerLoader<?> loader() {
        return loader;
    }

    @Override
    public String version() {
        return "dev";
    }


    public ResourcePackHandler packHandler() {
        return packHandler;
    }


    @Override
    public void destroy() throws IOException {
        super.destroy();
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}
