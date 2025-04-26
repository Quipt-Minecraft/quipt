package com.quiptmc.minecraft.listeners;

import com.quiptmc.minecraft.CoreUtils;
import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.core.entity.QuiptPlayer;
import com.quiptmc.core.events.QuiptPlayerChatEvent;
import com.quiptmc.core.events.QuiptPlayerDeathEvent;
import com.quiptmc.core.events.QuiptPlayerJoinEvent;
import com.quiptmc.core.events.QuiptPlayerLeaveEvent;
import com.quiptmc.core.events.listeners.Listener;
import com.quiptmc.core.utils.TaskScheduler;
import com.quiptmc.discord.Bot;
import com.quiptmc.discord.api.guild.QuiptGuild;
import com.quiptmc.discord.api.guild.channel.QuiptTextChannel;
import com.quiptmc.core.config.files.DiscordConfig;

import java.util.concurrent.TimeUnit;

public class QuiptPlayerListener implements Listener.QuiptPlayerJoinListener, Listener.QuiptPlayerLeaveListener, Listener.QuiptPlayerChatListener, Listener.QuiptPlayerDeathEventListener {


    public void onPlayerJoin(QuiptPlayerJoinEvent event) {
        QuiptPlayer player = event.player();
        String message = event.message();

        DiscordConfig config = ConfigManager.getConfig(CoreUtils.quipt(), DiscordConfig.class);
        if (config.enable_bot && config.announcements.join) {
            for (QuiptGuild guild : Bot.qda().getGuilds()) {
                for (QuiptTextChannel channel : guild.getTextChannels()) {
                    if (channel.getName().equalsIgnoreCase(config.channels.player_status) || channel.getId().equalsIgnoreCase(config.channels.player_status)) {
                        TaskScheduler.scheduleAsyncTask(() -> channel.sendPlayerMessage(player.uuid(), player.name(), message), 0, TimeUnit.SECONDS);
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerChat(QuiptPlayerChatEvent e) {

        DiscordConfig config = ConfigManager.getConfig(CoreUtils.quipt(), DiscordConfig.class);
        if (config.enable_bot && config.announcements.chat) {
            for (QuiptGuild guild : Bot.qda().getGuilds()) {
                for (QuiptTextChannel channel : guild.getTextChannels()) {
                    if (channel.getName().equalsIgnoreCase(config.channels.player_status) || channel.getId().equalsIgnoreCase(config.channels.player_status)) {
                        TaskScheduler.scheduleAsyncTask(() -> channel.sendPlayerMessage(e.player().uuid(), e.player().name(), e.message()), 0, TimeUnit.SECONDS);
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
                        TaskScheduler.scheduleAsyncTask(() -> channel.sendPlayerMessage(e.player().uuid(), e.player().name(), e.message()), 0, TimeUnit.SECONDS);
                    }
                }
            }
        }
    }

    @Override
    public void onPlayerDeath(QuiptPlayerDeathEvent e) {
        DiscordConfig config = ConfigManager.getConfig(CoreUtils.quipt(), DiscordConfig.class);
        if (config.enable_bot && config.announcements.death) {
            for (QuiptGuild guild : Bot.qda().getGuilds()) {
                for (QuiptTextChannel channel : guild.getTextChannels()) {
                    if (channel.getName().equalsIgnoreCase(config.channels.player_status) || channel.getId().equalsIgnoreCase(config.channels.player_status)) {
                        TaskScheduler.scheduleAsyncTask(() -> channel.sendPlayerMessage(e.player().uuid(), e.player().name(), e.message()), 0, TimeUnit.SECONDS);
                    }
                }
            }
        }
    }
}
