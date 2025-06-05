package com.quiptmc.core.heartbeat;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.core.heartbeat.runnable.Heartbeat;

public class HeartbeatUtils {

    private static final Registry<Heartbeat> registry = Registries.register("heartbeat", ()->null);

    public static void init(QuiptIntegration plugin) {
        Heartbeat heartbeat = new Heartbeat(plugin);
        registry.register(plugin.name(), heartbeat);
//        return Bukkit.getScheduler().runTaskLater(plugin.plugin().get(), heartbeat, 30);
    }

    public static Heartbeat heartbeat(QuiptIntegration plugin) {
        return registry.getOrDefault(plugin.name(), null);
    }
}
