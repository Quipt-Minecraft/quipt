package live.qsmc.minecraft.utils;

import live.qsmc.core.QuiptIntegration;
import live.qsmc.core.server.QuiptServer;
import live.qsmc.core.heartbeat.HeartbeatUtils;
import live.qsmc.minecraft.utils.loaders.ServerLoader;
import live.qsmc.minecraft.utils.teleportation.LocationUtils;
import live.qsmc.minecraft.web.CallbackHandler;
import live.qsmc.minecraft.web.ResourcePackHandler;

import java.io.File;

public class MinecraftIntegration<T extends ServerLoader<?>> extends QuiptIntegration {

    public final String NAME;
    private final File dataFolder;
    private final T loader;

    private ResourcePackHandler packHandler;
    private QuiptServer server;
    private CallbackHandler callbackHandler;

    public MinecraftIntegration(String name, T loader) {
        this.NAME = name;
        this.loader = loader;
        this.dataFolder = new File("plugins/" + name());
    }

    public void enable() {

        if (!dataFolder.exists())
            log("Initializer", "Attempting to create data folder (" + dataFolder.getPath() + "): " + dataFolder.mkdirs());
        HeartbeatUtils.init(this);
        LocationUtils.start(this);


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
    public void destroy() throws Exception {
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
