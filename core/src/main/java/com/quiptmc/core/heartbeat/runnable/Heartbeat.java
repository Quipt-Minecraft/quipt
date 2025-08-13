package com.quiptmc.core.heartbeat.runnable;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.heartbeat.Flutter;
import com.quiptmc.core.utils.TaskScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Heartbeat implements Runnable {

    private final List<FlutterTask> FLUTTERS_ADD = new ArrayList<>();
    private final Map<Integer, FlutterTask> FLUTTERS = new HashMap<>();
    private final List<Integer> FLUTTERS_REMOVE = new ArrayList<>();
    private final QuiptIntegration plugin;
    private int last_id = 0;

    public Heartbeat(QuiptIntegration plugin) {
        this.plugin = plugin;
        run();
    }

    public final QuiptIntegration plugin(){ return plugin;}

    public final Map<Integer, FlutterTask> flutters() { return FLUTTERS;}

    @Override
    public void run() {
        plugin.logger().log("Heartbeat test");
        for (FlutterTask task : FLUTTERS_ADD)
            FLUTTERS.put(task.id(), task);
        FLUTTERS_ADD.clear();
        for (int id : FLUTTERS_REMOVE)
            FLUTTERS.remove(id);
        FLUTTERS_REMOVE.clear();

        for (Map.Entry<Integer, FlutterTask> entry : FLUTTERS.entrySet()) {
            if (!entry.getValue().flutter().run()) {
                FLUTTERS_REMOVE.add(entry.getKey());
                plugin.log("Flutter " + entry.getKey(), "There was an error during this flutter. Removing from heartbeat.");
            }
        }

        TaskScheduler.scheduleAsyncTask(this, 0, TimeUnit.SECONDS);

    }

    public FlutterTask flutter(Flutter flutter) {
        last_id = last_id + 1;
        FlutterTask task = new FlutterTask(last_id, flutter);
        FLUTTERS_ADD.add(task);
        return task;
    }

    public void remove(FlutterTask task) {
        remove(task.id());
    }

    public void remove(int id) {
        FLUTTERS_REMOVE.add(id);
    }

    public List<FlutterTask> queue() {
        return FLUTTERS_ADD;
    }

    public List<Integer> disposal(){
        return FLUTTERS_REMOVE;
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
