package com.quiptmc.velocity;

import com.google.inject.Inject;
import com.quiptmc.core2.Quipt;
import com.quiptmc.core2.QuiptIntegration;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import java.io.File;
import java.nio.file.Path;

public abstract class QuiptProxy extends QuiptIntegration {

    private final ProxyServer server;
    private PluginDescription description;
    @DataDirectory final private Path dataDirectory;

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
        }, () -> {System.out.println("!!!!!!!!No description!!!!!!!!");});
        Quipt.INSTANCE.enable(this);
      // Plugin initialization logic goes here
    }

    @Override
    public File folder() {
        return dataDirectory.toFile();
    }

    @Override
    public String name() {
        return description == null ? "Quipt-Velocity-Test-Plugin" : description.getName().orElse("TEST");
    }

    @Override
    public String version() {
        return description == null ? "0.0.1" : description.getVersion().orElse("vTEST");
    }

    public ProxyServer proxy(){
        return server;
    }

    public PluginDescription description() {
        return description;
    }

}
