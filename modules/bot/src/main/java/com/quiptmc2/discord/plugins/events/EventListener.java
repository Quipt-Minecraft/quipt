package com.quiptmc2.discord.plugins.events;

import com.quiptmc2.discord.plugins.events.message.MessageReceivedEvent;

public abstract class EventListener<T extends Event> {

    Class<T> typeClass;

    public EventListener(Class<T> clazz) {
        typeClass = clazz;
    }

    public abstract void onMessageReceived(T event);

    public void handle(Event event) {
        if (typeClass.isInstance(event)) {
            onMessageReceived(typeClass.cast(event));
        }
    }

    public static abstract class MessageReceivedListener extends EventListener<MessageReceivedEvent> {
        public MessageReceivedListener() {
            super(MessageReceivedEvent.class);
        }
    }
}
