package com.quiptmc2.discord.plugins.events.message;

import com.quiptmc2.discord.Bot;
import com.quiptmc2.discord.api.guild.QuiptGuild;
import com.quiptmc2.discord.plugins.events.Event;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

public abstract class MessageEvent<T extends GenericMessageEvent> extends Event {

    protected final T originalEvent;

    public MessageEvent(Bot bot, T event) {
        super(bot);
        this.originalEvent = event;

    }

    public String id(){
        return originalEvent.getMessageId();
    }

    public QuiptGuild guild(){
        return bot().qda().guilds().get(originalEvent.getGuild());
    }

}
