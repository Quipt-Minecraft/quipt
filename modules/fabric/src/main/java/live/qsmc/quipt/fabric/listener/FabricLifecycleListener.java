package live.qsmc.quipt.fabric.listener;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.fabric.QuiptFabric;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

/**
 * Fabric server lifecycle listener that handles graceful shutdown.
 * Ensures that Quipt resources are properly cleaned up when the server stops.
 */
public class FabricLifecycleListener implements ServerLifecycleEvents.ServerStopping {

    @Override
    public void onServerStopping(MinecraftServer server) {
        // Initiate graceful shutdown of Quipt
        try {
            QuiptFabric fabricMod = QuiptFabric.instance();
            if (fabricMod != null && fabricMod.integration() != null) {
                fabricMod.integration().shutdown();
            }

            // Also shut down the core Quipt instance
            if (Quipt.INSTANCE != null) {
                Quipt.INSTANCE.shutdown();
            }
        } catch (Exception e) {
            System.err.println("[Quipt] Error during shutdown: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

