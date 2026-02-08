package com.quiptmc2.discord.api.guild.channel;

import com.quiptmc2.core.discord.embed.Embed;
import com.quiptmc2.discord.api.Wrapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.data.DataObject;

public class QuiptMessageChannel<T extends MessageChannel> extends Wrapper<T> {


    public QuiptMessageChannel(T original) {
        super(original);
    }

    public String mention(){
        return original().getAsMention();
    }

    public void delete(){
        original().delete().queue();
    }


    /**
     * Gets the channel's display name.
     * @return the channel name
     */
    public String name() {
        return original().getName();
    }

    /**
     * Sends a plain text message to the channel.
     * @param s the message content
     */
    public void send(String s) {
        original().sendMessage(s).queue();
    }

    /**
     * Gets the channel ID as a string.
     * @return the channel ID
     */
    public String id() {
        return original().getId();
    }

    /**
     * Sends one or more Quipt Embed objects to the channel.
     * @param embeds the embeds to send
     */
    public void send(Embed... embeds) {
        for (Embed embed : embeds) {
            EmbedBuilder builder = new EmbedBuilder(EmbedBuilder.fromData(DataObject.fromJson(embed.json().toString())));
            original().sendMessageEmbeds(builder.build()).queue();
        }
    }
}
