package live.qsmc.discord2.plugins.events;

import live.qsmc.core2.Quipt;
import live.qsmc.core2.data.registries.Registry;
import live.qsmc.discord2.Bot;

public class BotEventHandler {

    protected Registry<EventListener<? extends Event<?>>> listeners;

    public BotEventHandler(Bot bot, String registryKey) {
        listeners = Quipt.INSTANCE.registries().register("listeners-" + registryKey, () -> null);
    }

    public void register(String key, EventListener<?> listener) {
        listeners.register(key, listener);
    }

    public void handle(Event<?> event) {
        listeners.forEach((key,listener)->{
            listener.processs(event);
        });
    }
}
