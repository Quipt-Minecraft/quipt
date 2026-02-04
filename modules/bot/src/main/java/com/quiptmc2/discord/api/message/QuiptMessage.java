package com.quiptmc2.discord.api.message;

import net.dv8tion.jda.api.entities.Message;

public class QuiptMessage {

    private final Message message;

    public QuiptMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message.getContentRaw();
    }
}
