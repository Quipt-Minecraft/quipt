package com.quiptmc2.discord.api;

import com.quiptmc2.discord.Bot;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class QuiptListenerAdapter extends ListenerAdapter {

    private final Bot bot;

    public QuiptListenerAdapter(Bot bot){
        this.bot = bot;
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        bot.plugins().events().handle(new com.quiptmc2.discord.plugins.events.message.MessageReceivedEvent(bot, event));
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        bot.plugins().events().handle(new com.quiptmc2.discord.plugins.events.message.MessageDeleteEvent(bot, event));
    }
}
