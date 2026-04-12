/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.quiptmc.discord2.api.guild.channel;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

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



}
