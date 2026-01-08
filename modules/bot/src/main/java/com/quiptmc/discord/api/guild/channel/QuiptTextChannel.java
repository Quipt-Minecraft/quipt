/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.quiptmc.discord.api.guild.channel;

import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.discord.logger.LoggerUtils;
import com.quiptmc.core.discord.embed.Embed;
import com.quiptmc.core.utils.net.NetworkUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.restaction.WebhookAction;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.io.*;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Wrapper around JDA's TextChannel providing convenience helpers used by Quipt.
 * <p>Offers simple message sending, embed support, and per-player webhook
 * messaging with Minecraft avatar icons.</p>
 */
public class QuiptTextChannel {

    private final TextChannel channel;

    /**
     * Creates a new wrapper for the given JDA TextChannel.
     * @param channel the underlying JDA TextChannel
     */
    public QuiptTextChannel(TextChannel channel) {
        this.channel = channel;
    }

    /**
     * Gets the channel's display name.
     * @return the channel name
     */
    public String getName() {
        return channel.getName();
    }

    /**
     * Sends a plain text message to the channel.
     * @param s the message content
     */
    public void sendMessage(String s) {
        channel.sendMessage(s).queue();
    }

    /**
     * Gets the channel ID as a string.
     * @return the channel ID
     */
    public String getId() {
        return channel.getId();
    }

    /**
     * Gets the channel ID as a long.
     * @return the numeric channel ID
     */
    public long getIdLong() {
        return channel.getIdLong();
    }

    /**
     * Sends one or more Quipt Embed objects to the channel.
     * @param embeds the embeds to send
     */
    public void sendMessageEmbeds(Embed... embeds) {
        for (Embed embed : embeds) {
            EmbedBuilder builder = new EmbedBuilder(EmbedBuilder.fromData(DataObject.fromJson(embed.json().toString())));
            channel.sendMessageEmbeds(builder.build()).queue();
        }
    }

    /**
     * Sends a single Quipt Embed to the channel.
     * @param embed the embed to send
     */
    public void sendMessage(Embed embed) {
        EmbedBuilder builder = new EmbedBuilder(EmbedBuilder.fromData(DataObject.fromJson(embed.json().toString())));
        channel.sendMessageEmbeds(builder.build()).queue();
    }

    /**
     * Sends a message to the channel as a player using a per-player webhook.
     * If a webhook named after the player does not exist, one is created and
     * its avatar is set to the player's Minecraft head (via crafatar).
     *
     * @param uid unique identifier of the player (UUID)
     * @param playerName the player name to use for the webhook
     * @param message the message content
     */
    public void sendPlayerMessage(UUID uid, String playerName, String message) {
        deleteOldWebhooks();
        Webhook hook = getWebhook(playerName, uid);

        hook.sendMessage(message).queue();
    }

    /**
     * Retrieves an existing webhook matching the player name or creates one if missing.
     * When creating, attempts to set the avatar using the player's Minecraft head image.
     * @param playerName the desired webhook name
     * @param uid the player's UUID used for avatar lookup
     * @return the resolved webhook
     */
    private Webhook getWebhook(String playerName, UUID uid) {
        List<Webhook> webhooks = channel.retrieveWebhooks().complete();
        for (Webhook hook : webhooks)
            if (hook.getName().equals(playerName)) return hook;

        WebhookAction hookAction = channel.createWebhook(playerName);
        try {
            HttpResponse<String> response = NetworkUtils.get(NetworkUtils.DEFAULT, "https://crafatar.com/avatars/" + uid + "?size=128&overlay");
            File playerAssetFolder = new File("player_assets");
            if (!playerAssetFolder.exists())
                LoggerUtils.log("Bot", "Creating player assets folder: " + playerAssetFolder.mkdir());
            hookAction.setAvatar(Icon.from(new File("player_assets/" + uid + ".png"))).queue();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(playerAssetFolder))) {
                writer.write(response.body());
                System.out.println("String successfully written to " + playerAssetFolder.getPath());
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        } catch (IOException ex) {
            LoggerUtils.log("Bot", "Error downloading player asset");
        }
        return hookAction.complete();
    }

    /**
     * Deletes stale webhooks older than a defined retention period to prevent clutter.
     */
    private void deleteOldWebhooks() {
        for (Webhook hook : channel.retrieveWebhooks().complete()) {
            long creationEpoch = hook.getTimeCreated().toInstant().toEpochMilli();
            long currentEpoch = Instant.now().toEpochMilli();

            long diff = currentEpoch - creationEpoch;
            long check = TimeUnit.MILLISECONDS.convert(5, TimeUnit.DAYS);
            System.out.println("Diff: " + diff + " Check: " + check);
            if (diff > check) {
                System.out.println("Hook old, deleting.");
                hook.delete().queue();
            }

        }
    }

}
