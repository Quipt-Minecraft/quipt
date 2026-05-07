package live.qsmc.core2.events;

import java.util.ArrayList;
import java.util.List;

public class EventHandleResult<E extends Event<D>, D extends EventData<R>, R> {

    private boolean cancelled = false;

    List<R> results = new ArrayList<>();

    public void process(EventListener<E, D, R> listener, E event) {
        R result = listener.processs(event);
        if (result != null)
            results.add(result);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public List<R> results() {
        return results;
    }
}
