package com.quiptmc2.discord.api;

import com.quiptmc2.discord.Bot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class QuiptListenerAdapter extends ListenerAdapter {

    private final Bot bot;

    public QuiptListenerAdapter(Bot bot){
        this.bot = bot;
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        bot.plugins().eventHandler().handle(new com.quiptmc2.discord.plugins.events.message.MessageReceivedEvent(event));
    }
}
