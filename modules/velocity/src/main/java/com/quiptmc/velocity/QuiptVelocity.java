package com.quiptmc.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import java.nio.file.Path;


@Plugin(id = "qsync", name = "QSync", version = "1.0-SNAPSHOT")
public class QuiptVelocity extends QuiptProxy {

    @Inject
    public QuiptVelocity(ProxyServer server, @DataDirectory Path dataDirectory) {
        super(server, dataDirectory);
    }



    @Override
    public void enable() {

    }
}
