package live.qsmc.quipt.core.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskScheduler {


    /**
     * Private constructor to prevent instantiation
     */
    private TaskScheduler() {
        throw new IllegalAccessError("Utility class");
    }

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static boolean isShuttingDown = false;

    /**
     * Schedules a task to run asynchronously
     *
     * @param task  The task to run
     * @param delay The delay before the task runs
     * @param unit  The unit of time for the delay
     */
    public static void scheduleAsyncTask(Runnable task, long delay, TimeUnit unit) {
        if (!isShuttingDown) {
            try {
                scheduler.schedule(task, delay, unit);
            } catch (Exception e) {
                // Silently fail if scheduler is shutting down
            }
        }
    }

    /**
     * Shuts down the task scheduler gracefully
     */
    public static void shutdown() {
        isShuttingDown = true;
        scheduler.shutdown();
        try {
            // Wait up to 5 seconds for tasks to complete
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                // If tasks don't complete, forcefully shut them down
                scheduler.shutdownNow();
                // Wait another 2 seconds for forced shutdown
                scheduler.awaitTermination(2, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            // Forcefully shut down if interrupted
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Checks if the scheduler is shutting down
     *
     * @return true if shutting down
     */
    public static boolean isShuttingDown() {
        return isShuttingDown;
    }
}
