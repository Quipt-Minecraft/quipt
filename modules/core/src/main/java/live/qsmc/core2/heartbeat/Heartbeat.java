package live.qsmc.core2.heartbeat;

import live.qsmc.core2.QuiptIntegration;
import live.qsmc.core2.heartbeat.flutter.Flutter;
import live.qsmc.core2.utils.TaskScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Heartbeat implements Runnable {

    private final List<FlutterTask> flutters_add_queue = new ArrayList<>();
    private final Map<Integer, FlutterTask> flutters = new HashMap<>();
    private final List<Integer> flutters_remove_queue = new ArrayList<>();
    private final QuiptIntegration integration;
    private int last_id = 0;

    public Heartbeat(QuiptIntegration integration) {
        this.integration = integration;
        run();
    }

    public final QuiptIntegration integration(){ return integration;}

    public final Map<Integer, FlutterTask> flutters() { return flutters;}

    @Override
    public void run() {
        for (FlutterTask task : flutters_add_queue)
            flutters.put(task.id(), task);
        flutters_add_queue.clear();
        for (int id : flutters_remove_queue)
            flutters.remove(id);
        flutters_remove_queue.clear();

        for (Map.Entry<Integer, FlutterTask> entry : flutters.entrySet()) {
            try {
                if (!entry.getValue().flutter().run()) {
                    flutters_remove_queue.add(entry.getKey());
                    integration.logger().log("Flutter " + entry.getKey(), "There was an error during this flutter. Removing from heartbeat.");
                }
            }catch (Exception e) {
                integration.logger().log("Flutter " + entry.getKey(), "There was an exception during this flutter. Removing from heartbeat.");
                integration.logger().error(integration().name() + "-Heartbeat", "Exception during flutter " + entry.getKey(), e);
                flutters_remove_queue.add(entry.getKey());
            }
        }

        TaskScheduler.scheduleAsyncTask(this, 0, TimeUnit.SECONDS);

    }

    public FlutterTask flutter(Flutter flutter) {
        last_id = last_id + 1;
        FlutterTask task = new FlutterTask(last_id, flutter);
        flutters_add_queue.add(task);
        return task;
    }

    public void remove(FlutterTask task) {
        remove(task.id());
    }

    public void remove(int id) {
        flutters_remove_queue.add(id);
    }

    public List<FlutterTask> queue() {
        return flutters_add_queue;
    }

    public List<Integer> disposal(){
        return flutters_remove_queue;
    }

    public static class FlutterTask {

        private final int id;
        private final Flutter flutter;

        protected FlutterTask(int id, Flutter flutter) {
            this.id = id;
            this.flutter = flutter;
        }

        public int id() {
            return id;
        }

        public Flutter flutter() {
            return flutter;
        }
    }
}
