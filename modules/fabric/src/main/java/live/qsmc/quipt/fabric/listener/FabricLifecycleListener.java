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
            // Start a short-lived watchdog to force JVM exit if non-daemon threads remain after timeout.
            try {
                boolean watchdogEnabled = Boolean.parseBoolean(System.getProperty("quipt.force_shutdown_watchdog", "true"));
                if (watchdogEnabled) {
                    int timeoutSeconds = Integer.parseInt(System.getProperty("quipt.shutdown_watchdog_timeout", "15"));
                    Thread wd = new Thread(() -> {
                        try {
                            Thread.sleep(timeoutSeconds * 1000L);
                        } catch (InterruptedException ignored) {
                        }
                        // If JVM hasn't exited by now, log remaining non-daemon threads and force exit
                        String dump = ThreadDumper.dumpAllThreads();
                        boolean hasNonDaemon = Thread.getAllStackTraces().keySet().stream().anyMatch(t -> !t.isDaemon() && t.getName() != null && !t.getName().startsWith("Quipt-Shutdown-Watchdog"));
                        if (hasNonDaemon) {
                            System.err.println("[Quipt] Shutdown watchdog triggered after " + timeoutSeconds + "s. Forcing JVM exit. Remaining threads:\n" + dump);
                            // Force exit to ensure hosted providers detect shutdown
                            try {
                                System.exit(0);
                            } catch (Throwable t) {
                                Runtime.getRuntime().halt(0);
                            }
                        }
                    }, "Quipt-Shutdown-Watchdog");
                    wd.setDaemon(true);
                    wd.start();
                }
            } catch (Exception e) {
                System.err.println("[Quipt] Failed to start shutdown watchdog: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("[Quipt] Error scheduling shutdown on server thread: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

