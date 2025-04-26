package com.quiptmc.minecraft.utils.heartbeat.runnable;

import com.quiptmc.minecraft.CoreUtils;
import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.utils.TaskScheduler;
import com.quiptmc.minecraft.utils.heartbeat.Flutter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Heartbeat implements Runnable {

    QuiptIntegration plugin;

    private final Map<Integer, FlutterTask> FLUTTERS = new HashMap<>();
    private final List<Integer> FLUTTERS_REMOVE = new ArrayList<>();
    final List<FlutterTask> FLUTTERS_ADD = new ArrayList<>();
    private int last_id = 0;

    public Heartbeat(QuiptIntegration plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {

        for (FlutterTask task : FLUTTERS_ADD)
            FLUTTERS.put(task.getId(), task);
        FLUTTERS_ADD.clear();
        for (int id : FLUTTERS_REMOVE)
            FLUTTERS.remove(id);
        FLUTTERS_REMOVE.clear();

        for (Map.Entry<Integer, FlutterTask> entry : FLUTTERS.entrySet()) {
            if (!entry.getValue().getFlutter().run()) {
                FLUTTERS_REMOVE.add(entry.getKey());
                CoreUtils.quipt().logger().log("Flutter " + entry.getKey(), "There was an error during this flutter. Removing from heartbeat.");
            }
        }

        TaskScheduler.scheduleAsyncTask(this, 0, TimeUnit.SECONDS);

    }

    public FlutterTask addFlutter(Flutter flutter) {
        last_id = last_id + 1;
        FlutterTask task = new FlutterTask(last_id, flutter);
        FLUTTERS_ADD.add(task);
        return task;
    }

    public void removeFlutter(FlutterTask task) {
        removeFlutter(task.getId());
    }

    public void removeFlutter(int id) {
        FLUTTERS_REMOVE.add(id);
    }

    public Iterable<? extends FlutterTask> getAddQueue() {
        return FLUTTERS_ADD;
    }

    public static class FlutterTask {

        private final int id;
        private final Flutter flutter;

        protected FlutterTask(int id, Flutter flutter) {
            this.id = id;
            this.flutter = flutter;
        }

        public int getId() {
            return id;
        }

        public Flutter getFlutter() {
            return flutter;
        }
    }
}
