package live.qsmc.core2.events;

import live.qsmc.core2.Quipt;
import live.qsmc.core2.QuiptIntegration;
import live.qsmc.core2.data.registries.Registry;

public class EventHandler {

    protected Registry<EventListener<? extends Event<?>>> listeners;

    public EventHandler(QuiptIntegration integration) {
        this(integration, "default");
    }

    public EventHandler(QuiptIntegration integration, String key) {
        listeners = Quipt.INSTANCE.registries().register( integration.name() + ":" + key + ":listeners", () -> null);
    }

    public void register(EventListener<?> listener) {
        listeners.register(listener.toString(), listener);
    }

    public void handle(Event<?> event) {
        listeners.forEach((key,listener)->{
            listener.processs(event);
        });
    }
}
