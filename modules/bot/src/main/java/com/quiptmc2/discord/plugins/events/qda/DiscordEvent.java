package com.quiptmc2.discord.plugins.events.qda;

import com.quiptmc2.discord.Bot;
import com.quiptmc2.discord.plugins.events.Event;

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
