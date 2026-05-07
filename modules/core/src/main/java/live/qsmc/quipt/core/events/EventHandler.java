package live.qsmc.quipt.core.events;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.data.registries.Registry;

public class EventHandler {

    protected Registry<EventListener<?,?,?>> listeners;

    public EventHandler(QuiptIntegration integration) {
        this(integration, "default");
    }

    public EventHandler(QuiptIntegration integration, String key) {
        listeners = Quipt.INSTANCE.registries().register(integration.name() + ":" + key + ":listeners", () -> null);
    }

    public void register(EventListener<?,?,?> listener) {
        listeners.register(listener.toString(), listener);
    }

    @SuppressWarnings("unchecked")
    public <E extends Event<D>, D extends Event.Data, R> EventHandleResult<E, D, R> handle(E event) {
        EventHandleResult<E, D, R> result = new EventHandleResult<>();
        listeners.forEach((key, listener) -> {
            if (listener.eventClass().equals(event.getClass()))
                result.process((EventListener<E,D,R>) listener, event);
        });
        return result;
    }
}
