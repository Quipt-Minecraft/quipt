package com.quiptmc.core.heartbeat;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.core.heartbeat.runnable.Heartbeat;

public class HeartbeatUtils {

    private static final Registry<Heartbeat> registry = Registries.register("heartbeat", ()->null);

    public static Heartbeat init(QuiptIntegration plugin) {
        if(registry.get(plugin.name()).isPresent()) {
            throw new IllegalStateException("Heartbeat for " + plugin.name() + " is already initialized.");
        }
        Heartbeat heartbeat = new Heartbeat(plugin);
        registry.register(plugin.name(), heartbeat);
        return heartbeat;
    }

    public static Heartbeat heartbeat(QuiptIntegration plugin) {
        return registry.getOrDefault(plugin.name(), null);
    }
}
