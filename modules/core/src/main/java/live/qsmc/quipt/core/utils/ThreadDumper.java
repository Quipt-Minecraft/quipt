package live.qsmc.quipt.core.utils;

import java.util.Map;

/**
 * Small utility to produce a thread dump string for debugging shutdown issues.
 */
public final class ThreadDumper {

    private ThreadDumper() { throw new IllegalStateException("Utility class"); }

    public static String dumpAllThreads() {
        StringBuilder sb = new StringBuilder();
        Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
        for (Map.Entry<Thread, StackTraceElement[]> e : traces.entrySet()) {
            Thread t = e.getKey();
            StackTraceElement[] stack = e.getValue();
            sb.append("Thread: ")
                .append(t.getName())
                .append(" (id=")
                .append(t.getId())
                .append(") daemon=")
                .append(t.isDaemon())
                .append(" state=")
                .append(t.getState())
                .append('\n');
            for (StackTraceElement s : stack) {
                sb.append("    at ").append(s.toString()).append('\n');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}

