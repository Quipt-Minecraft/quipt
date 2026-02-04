package com.quiptmc2.discord.plugins.events;

import com.quiptmc2.discord.Bot;
import com.quiptmc2.discord.api.Wrapper;

public abstract class Event<T extends net.dv8tion.jda.api.events.Event> extends Wrapper<T> {

    Bot bot;

    public Event(T original, Bot bot) {
        super(original);

        this.bot = bot;
    }


    public Bot bot(){
        return bot;
    }
}
