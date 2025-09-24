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

public class QuiptTextChannel {

    private final TextChannel channel;

    public QuiptTextChannel(TextChannel channel) {
        this.channel = channel;
    }

    public String getName() {
        return channel.getName();
    }

    public void sendMessage(String s) {
        channel.sendMessage(s).queue();
    }

    public String getId() {
        return channel.getId();
    }

    public long getIdLong() {
        return channel.getIdLong();
    }

    public void sendMessageEmbeds(Embed... embeds) {
        for (Embed embed : embeds) {
            EmbedBuilder builder = new EmbedBuilder(EmbedBuilder.fromData(DataObject.fromJson(embed.json().toString())));
            channel.sendMessageEmbeds(builder.build()).queue();
        }
    }

    public void sendMessage(Embed embed) {
        EmbedBuilder builder = new EmbedBuilder(EmbedBuilder.fromData(DataObject.fromJson(embed.json().toString())));
        channel.sendMessageEmbeds(builder.build()).queue();
    }

    public void sendPlayerMessage(UUID uid, String playerName, String message) {
        deleteOldWebhooks();
        Webhook hook = getWebhook(playerName, uid);

        hook.sendMessage(message).queue();
    }

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
