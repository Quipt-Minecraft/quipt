package com.quiptmc2.discord.plugins.events;

import com.quiptmc2.discord.Bot;

public abstract class Event {

    Bot bot;

    public Event(Bot bot){
        this.bot = bot;
    }

    public Bot bot(){
        return bot;
    }
}
