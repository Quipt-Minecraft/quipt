package com.quiptmc.discord2.plugins.events;

import com.quiptmc.discord2.plugins.events.qda.interaction.button.ButtonInteractionEvent;
import com.quiptmc.discord2.plugins.events.qda.message.MessageDeleteEvent;
import com.quiptmc.discord2.plugins.events.qda.message.MessageReceivedEvent;

public abstract class EventListener<T extends Event<?>> {

    Class<T> typeClass;

    public EventListener(Class<T> clazz) {
        typeClass = clazz;
    }

    public abstract void handle(T event);

    public void processs(Event<?> event) {
        if (typeClass.isInstance(event)) {
            handle(typeClass.cast(event));
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
