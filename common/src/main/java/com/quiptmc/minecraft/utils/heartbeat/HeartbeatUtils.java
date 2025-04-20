package com.quiptmc.minecraft.utils.heartbeat;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.minecraft.utils.MinecraftIntegration;
import com.quiptmc.minecraft.utils.heartbeat.runnable.Heartbeat;

public class HeartbeatUtils {

    private static final Registry<Heartbeat> registry = Registries.register("heartbeat", Heartbeat.class);

    public static void init(QuiptIntegration plugin) {
        Heartbeat heartbeat = new Heartbeat(plugin);
        registry.register(plugin.name(), heartbeat);
//        return Bukkit.getScheduler().runTaskLater(plugin.plugin().get(), heartbeat, 30);
    }

    public static Heartbeat heartbeat(MinecraftIntegration plugin) {
        return registry.getOrDefault(plugin.name(), null);
    }
}
