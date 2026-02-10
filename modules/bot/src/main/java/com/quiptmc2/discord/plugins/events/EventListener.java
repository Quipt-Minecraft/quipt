package com.quiptmc2.discord.plugins.events;

import com.quiptmc2.discord.plugins.events.qda.interaction.button.ButtonInteractionEvent;
import com.quiptmc2.discord.plugins.events.qda.message.MessageDeleteEvent;
import com.quiptmc2.discord.plugins.events.qda.message.MessageReceivedEvent;

public abstract class EventListener<T extends Event<?>> {

    Class<T> typeClass;

    public EventListener(Class<T> clazz) {
        typeClass = clazz;
    }

    public abstract void onMessageReceived(T event);

    public void handle(Event<?> event) {
        if (typeClass.isInstance(event)) {
            onMessageReceived(typeClass.cast(event));
        }
    }

    public static abstract class MessageReceivedListener extends EventListener<MessageReceivedEvent> {
        public MessageReceivedListener() {
            super(MessageReceivedEvent.class);
        }
    }

    public static abstract class MessageDeleteListener extends EventListener<MessageDeleteEvent> {
        public MessageDeleteListener() {
            super(MessageDeleteEvent.class);
        }
    }

    public static abstract class ButtonInteractionListener extends EventListener<ButtonInteractionEvent> {
        public ButtonInteractionListener() {
            super(ButtonInteractionEvent.class);
        }
    }
}
