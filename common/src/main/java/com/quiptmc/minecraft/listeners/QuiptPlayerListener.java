package com.quiptmc.minecraft.listeners;

import com.quiptmc.minecraft.CoreUtils;
import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.minecraft.api.MinecraftPlayer;
import com.quiptmc.minecraft.api.events.QuiptPlayerChatEvent;
import com.quiptmc.minecraft.api.events.QuiptPlayerDeathEvent;
import com.quiptmc.minecraft.api.events.QuiptPlayerJoinEvent;
import com.quiptmc.minecraft.api.events.QuiptPlayerLeaveEvent;
import com.quiptmc.minecraft.api.events.listeners.Listener;
import com.quiptmc.core.utils.TaskScheduler;
import com.quiptmc.discord.Bot;
import com.quiptmc.discord.api.guild.QuiptGuild;
import com.quiptmc.discord.api.guild.channel.QuiptTextChannel;
import com.quiptmc.core.config.files.DiscordConfig;
import com.quiptmc.minecraft.utils.chat.MessageUtils;
import com.quiptmc.minecraft.utils.sessions.SessionManager;

import java.util.concurrent.TimeUnit;

public class QuiptPlayerListener implements Listener.QuiptPlayerJoinListener, Listener.QuiptPlayerLeaveListener, Listener.QuiptPlayerChatListener, Listener.QuiptPlayerDeathEventListener {


    public void onPlayerJoin(QuiptPlayerJoinEvent event) {
        MinecraftPlayer player = event.player();
        String message = event.message();

        DiscordConfig config = ConfigManager.getConfig(CoreUtils.quipt(), DiscordConfig.class);
        if (config.enable_bot && config.announcements.join) {
            for (QuiptGuild guild : Bot.qda().getGuilds()) {
                for (QuiptTextChannel channel : guild.getTextChannels()) {
                    if (channel.getName().equalsIgnoreCase(config.channels.player_status) || channel.getId().equalsIgnoreCase(config.channels.player_status)) {
                        TaskScheduler.scheduleAsyncTask(() -> channel.sendPlayerMessage(player.uuid(), MessageUtils.plainText(player.name()), message), 0, TimeUnit.SECONDS);
                    }
                }
            }
        }
        SessionManager.startSession(player);
    }

    @Override
    public void onPlayerChat(QuiptPlayerChatEvent e) {

        DiscordConfig config = ConfigManager.getConfig(CoreUtils.quipt(), DiscordConfig.class);
        if (config.enable_bot && config.announcements.chat) {
            for (QuiptGuild guild : Bot.qda().getGuilds()) {
                for (QuiptTextChannel channel : guild.getTextChannels()) {
                    if (channel.getName().equalsIgnoreCase(config.channels.player_status) || channel.getId().equalsIgnoreCase(config.channels.player_status)) {
                        TaskScheduler.scheduleAsyncTask(() -> channel.sendPlayerMessage(e.player().uuid(), MessageUtils.plainText(e.player().name()), e.message()), 0, TimeUnit.SECONDS);
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerLeave(QuiptPlayerLeaveEvent e) {
        DiscordConfig config = ConfigManager.getConfig(CoreUtils.quipt(), DiscordConfig.class);
        if (config.enable_bot && config.announcements.leave) {
            for (QuiptGuild guild : Bot.qda().getGuilds()) {
                for (QuiptTextChannel channel : guild.getTextChannels()) {
                    if (channel.getName().equalsIgnoreCase(config.channels.player_status) || channel.getId().equalsIgnoreCase(config.channels.player_status)) {
                        TaskScheduler.scheduleAsyncTask(() -> channel.sendPlayerMessage(e.player().uuid(), MessageUtils.plainText(e.player().name()), e.message()), 0, TimeUnit.SECONDS);
                    }
                }
            }
        }
        SessionManager.finishSession(e.player());
    }

    @Override
    public void onPlayerDeath(QuiptPlayerDeathEvent e) {
        DiscordConfig config = ConfigManager.getConfig(CoreUtils.quipt(), DiscordConfig.class);
        if (config.enable_bot && config.announcements.death) {
            for (QuiptGuild guild : Bot.qda().getGuilds()) {
                for (QuiptTextChannel channel : guild.getTextChannels()) {
                    if (channel.getName().equalsIgnoreCase(config.channels.player_status) || channel.getId().equalsIgnoreCase(config.channels.player_status)) {
                        TaskScheduler.scheduleAsyncTask(() -> channel.sendPlayerMessage(e.player().uuid(), MessageUtils.plainText(e.player().name()), e.message()), 0, TimeUnit.SECONDS);
                    }
                }
            }
        }
    }
}
