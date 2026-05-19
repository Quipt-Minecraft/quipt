package live.qsmc.quipt.fabric.listener;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.utils.ThreadDumper;
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
        // Initiate graceful shutdown of Quipt on the server main thread and dump threads for debugging
        try {
            System.out.println("[Quipt] SERVER_STOPPING invoked. Thread dump:\n" + ThreadDumper.dumpAllThreads());

            // Ensure shutdown logic runs on the Minecraft server thread to avoid blocking Fabric internals
            server.execute(() -> {
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
                } finally {
                    System.out.println("[Quipt] Shutdown tasks executed on server thread. Thread dump:\n" + ThreadDumper.dumpAllThreads());
                }
            });
        } catch (Exception e) {
            System.err.println("[Quipt] Error scheduling shutdown on server thread: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

