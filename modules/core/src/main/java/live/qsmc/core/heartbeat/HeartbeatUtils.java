package live.qsmc.core.heartbeat;

import live.qsmc.core.QuiptIntegration;
import live.qsmc.core.data.registries.Registries;
import live.qsmc.core.data.registries.Registry;
import live.qsmc.core.heartbeat.runnable.Heartbeat;

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
