package live.qsmc.quipt.core.events;

import java.util.ArrayList;
import java.util.List;

public class EventHandleResult {


    private boolean cancelled = false;

    List<EventProperties> results = new ArrayList<>();

    EventHandleResult(){

    }

    public void process(EventListener<?,?> listener, Event<?> event) {
        EventProperties result = listener.process(event);
        if (result != null)
            results.add(result);
    }

    public boolean cancelled() {
        return cancelled;
    }

    public void cancel(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public List<EventProperties> results() {
        return results;
    }
}
