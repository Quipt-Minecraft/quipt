package live.qsmc.core.utils.queue;

import live.qsmc.core.QuiptIntegration;
import live.qsmc.core.heartbeat.HeartbeatUtils;
import live.qsmc.core.heartbeat.runnable.Heartbeat;

public class QueueManager {

    public static final Queue INSTANCE = new Queue();
    private static Heartbeat.FlutterTask task = null;

    public static void initialize(QuiptIntegration integration) {
        Heartbeat heartbeat = HeartbeatUtils.heartbeat(integration) == null ? HeartbeatUtils.init(integration) : HeartbeatUtils.heartbeat(integration);
        task = heartbeat.flutter(INSTANCE);
    }

    public Heartbeat.FlutterTask task(){
        return task;
    }
}
