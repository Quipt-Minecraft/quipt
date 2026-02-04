/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.quiptmc2.discord.api.guild.channel;

import com.quiptmc2.core.discord.embed.Embed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.data.DataObject;

/**
 * Wrapper around JDA's TextChannel providing convenience helpers used by Quipt.
 * <p>Offers simple message sending, embed support, and per-player webhook
 * messaging with Minecraft avatar icons.</p>
 */
public class QuiptTextChannel extends QuiptMessageChannel<TextChannel>{

    /**
     * Creates a new wrapper for the given JDA TextChannel.
     * @param channel the underlying JDA TextChannel
     */
    public QuiptTextChannel(TextChannel channel) {
        super(channel);
    }

    /**
     * Gets the channel's display name.
     * @return the channel name
     */
    public String getName() {
        return original().getName();
    }

    /**
     * Sends a plain text message to the channel.
     * @param s the message content
     */
    public void sendMessage(String s) {
        original().sendMessage(s).queue();
    }

    /**
     * Gets the channel ID as a string.
     * @return the channel ID
     */
    public String getId() {
        return original().getId();
    }

    /**
     * Gets the channel ID as a long.
     * @return the numeric channel ID
     */
    public long getIdLong() {
        return original().getIdLong();
    }

    /**
     * Sends one or more Quipt Embed objects to the channel.
     * @param embeds the embeds to send
     */
    public void sendMessageEmbeds(Embed... embeds) {
        for (Embed embed : embeds) {
            EmbedBuilder builder = new EmbedBuilder(EmbedBuilder.fromData(DataObject.fromJson(embed.json().toString())));
            original().sendMessageEmbeds(builder.build()).queue();
        }
    }

    /**
     * Sends a single Quipt Embed to the channel.
     * @param embed the embed to send
     */
    public void sendMessage(Embed embed) {
        EmbedBuilder builder = new EmbedBuilder(EmbedBuilder.fromData(DataObject.fromJson(embed.json().toString())));
        original().sendMessageEmbeds(builder.build()).queue();
    }

}
