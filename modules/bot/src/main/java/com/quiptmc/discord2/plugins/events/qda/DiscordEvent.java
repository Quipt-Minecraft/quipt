package com.quiptmc.discord2.plugins.events.qda;

import com.quiptmc.discord2.Bot;
import com.quiptmc.discord2.plugins.events.Event;

public abstract class DiscordEvent<T extends net.dv8tion.jda.api.events.Event> extends Event<T> {

    Bot bot;

    public DiscordEvent(T original, Bot bot) {
        super(original);

        this.bot = bot;
    }


    public Bot bot(){
        return bot;
    }
}
