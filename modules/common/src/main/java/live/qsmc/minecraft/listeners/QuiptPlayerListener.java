package live.qsmc.minecraft.listeners;

import live.qsmc.minecraft.CoreUtils;
import live.qsmc.core.config.ConfigManager;
import live.qsmc.minecraft.api.MinecraftPlayer;
import live.qsmc.minecraft.api.events.QuiptPlayerChatEvent;
import live.qsmc.minecraft.api.events.QuiptPlayerDeathEvent;
import live.qsmc.minecraft.api.events.QuiptPlayerJoinEvent;
import live.qsmc.minecraft.api.events.QuiptPlayerLeaveEvent;
import live.qsmc.minecraft.api.events.listeners.Listener;
import live.qsmc.core.utils.TaskScheduler;
import live.qsmc.discord.Bot;
import live.qsmc.discord.api.guild.QuiptGuild;
import live.qsmc.discord.api.guild.channel.QuiptTextChannel;
import live.qsmc.core.config.files.DiscordConfig;
import live.qsmc.minecraft.utils.chat.MessageUtils;
import live.qsmc.minecraft.utils.sessions.SessionManager2;

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
        SessionManager2.startSession(player);
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
        SessionManager2.finishSession(e.player());
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
