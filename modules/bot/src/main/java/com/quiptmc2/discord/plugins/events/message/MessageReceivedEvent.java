package com.quiptmc2.discord.plugins.events.message;

import com.quiptmc2.discord.Bot;
import com.quiptmc2.discord.api.message.QuiptMessage;

public class MessageReceivedEvent extends MessageEvent<net.dv8tion.jda.api.events.message.MessageReceivedEvent> {

    public MessageReceivedEvent(Bot bot, net.dv8tion.jda.api.events.message.MessageReceivedEvent event) {
        super(bot, event);

    }

    public QuiptMessage message() {
        return new QuiptMessage(originalEvent.getMessage());
    }
}
