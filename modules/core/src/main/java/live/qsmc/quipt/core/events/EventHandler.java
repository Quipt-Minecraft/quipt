package live.qsmc.quipt.core.events;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.data.registries.Registry;

public class EventHandler {

    protected Registry<EventListener<?,?>> listeners;

    public EventHandler(QuiptIntegration integration) {
        this(integration, "default");
    }

    public EventHandler(QuiptIntegration integration, String key) {
        listeners = Quipt.INSTANCE.registries().register(integration.name() + ":" + key + ":listeners", () -> null);
    }

    public void register(EventListener<?,?> listener) {
        listeners.register(listener.toString(), listener);
    }

    @SuppressWarnings("unchecked")
    public EventHandleResult handle(Event<?> event) {
        EventHandleResult result = new EventHandleResult();
        listeners.forEach((key, listener) -> {
            // Stop processing if event is cancellable and has been cancelled
            if (event instanceof Event.Cancellable cancellable && cancellable.cancelled()) {
                return;
            }
            if (listener.eventClass().equals(event.getClass()))
                result.process(listener, event);
        });
        return result;
    }
}
