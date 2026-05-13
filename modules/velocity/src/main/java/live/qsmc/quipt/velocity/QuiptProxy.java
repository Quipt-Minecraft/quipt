package live.qsmc.quipt.velocity;

import com.google.inject.Inject;
import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import live.qsmc.quipt.core.data.Metadata;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Path;

public abstract class QuiptProxy {

    private final ProxyServer server;
    private PluginDescription description;
    @DataDirectory final private Path dataDirectory;
    private VelocityIntegration integration;

    @Inject
    public QuiptProxy(ProxyServer server, @DataDirectory Path dataDirectory){
        this.server = server;
        this.dataDirectory = dataDirectory;
//        description = server.getPluginManager().fromInstance(this).get().getDescription();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getPluginManager().fromInstance(this).ifPresentOrElse(container -> {
            description = container.getDescription();
            JSONObject data = new JSONObject();
            data.put("name", description.getName());
            data.put("version", description.getVersion());
            data.put("folder", dataDirectory.toFile());
            Metadata metadata = Metadata.of(data);
            integration = new VelocityIntegration(metadata, description) {
                @Override
                public void enable() {
                    QuiptProxy.this.enable();
                }
            };
            Quipt.INSTANCE.enable(integration);
        }, () -> {System.out.println("!!!!!!!!No description!!!!!!!!");});
    }

    public abstract void enable();


    public ProxyServer proxy(){
        return server;
    }

    public PluginDescription description() {
        return description;
    }

    public VelocityIntegration integration() {
        return integration;
    }

    public abstract static class VelocityIntegration extends MinecraftIntegration<PluginDescription> {


        public VelocityIntegration(Metadata metadata, PluginDescription instance) {
            super(metadata, instance);
        }

        @Override
        public File addons() {
            return new File("plugins");
        }

    }

}
