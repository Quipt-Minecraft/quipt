package com.quiptmc2.discord.plugins.events;

import com.quiptmc2.core.data.registries.Registry;
import com.quiptmc2.discord.Bot;

public class BotEventHandler {

    Registry<EventListener<? extends Event>> listeners;

    public BotEventHandler(Bot bot) {
        listeners = bot.registries().register("listeners", () -> null);
    }

    public void register(String key, EventListener<?> listener) {
        listeners.register(key, listener);
    }

    public void handle(Event event) {
        listeners.forEach((key,listener)->{
            listener.handle(event);
        });
    }
}
