package com.quiptmc2.discord.api.message;

import com.quiptmc2.discord.api.Wrapper;
import net.dv8tion.jda.api.entities.Message;

public class QuiptMessage extends Wrapper<Message> {


    public QuiptMessage(Message message) {
        super(message);
    }

    @Override
    public String toString() {
        return original().getContentRaw();
    }
}
