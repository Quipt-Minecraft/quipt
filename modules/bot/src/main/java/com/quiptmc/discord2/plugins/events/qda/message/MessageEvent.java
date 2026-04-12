package com.quiptmc.discord2.plugins.events.qda.message;

import com.quiptmc.discord2.Bot;
import com.quiptmc.discord2.api.guild.QuiptGuild;
import com.quiptmc.discord2.api.guild.channel.QuiptMessageChannel;
import com.quiptmc.discord2.plugins.events.qda.DiscordEvent;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;

public abstract class MessageEvent<T extends GenericMessageEvent> extends DiscordEvent<T> {

    public MessageEvent(Bot bot, T event) {
        super(event, bot);
    }

    public String id(){
        return data().getMessageId();
    }

    public QuiptGuild guild(){
        return bot().qda().guilds().get(data().getGuild());
    }

    public QuiptMessageChannel<? extends MessageChannel> channel(){
        return new QuiptMessageChannel<>(data().getChannel());
    }



}
