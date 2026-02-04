package com.quiptmc2.discord.plugins.events.message;

import com.quiptmc2.discord.api.message.QuiptMessage;
import com.quiptmc2.discord.plugins.events.Event;
import net.dv8tion.jda.api.hooks.EventListener;

public class MessageReceivedEvent extends Event {

    private final net.dv8tion.jda.api.events.message.MessageReceivedEvent originalEvent;

    public MessageReceivedEvent(net.dv8tion.jda.api.events.message.MessageReceivedEvent event) {
        this.originalEvent = event;
    }

    public QuiptMessage message() {
        return new QuiptMessage(originalEvent.getMessage());
    }
}
