package com.quiptmc2.discord.plugins.events.message;

import com.quiptmc2.discord.Bot;
import com.quiptmc2.discord.api.guild.QuiptGuild;
import com.quiptmc2.discord.api.guild.channel.QuiptMessageChannel;
import com.quiptmc2.discord.plugins.events.Event;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

public abstract class MessageEvent<T extends GenericMessageEvent> extends Event<T> {

    public MessageEvent(Bot bot, T event) {
        super(event, bot);
    }

    public String id(){
        return original().getMessageId();
    }

    public QuiptGuild guild(){
        return bot().qda().guilds().get(original().getGuild());
    }

    public QuiptMessageChannel<? extends MessageChannel> channel(){
        return new QuiptMessageChannel<>(original().getChannel());
    }



}
