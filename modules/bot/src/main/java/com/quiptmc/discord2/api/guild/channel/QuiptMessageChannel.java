package com.quiptmc.discord2.api.guild.channel;

import com.quiptmc.core2.discord.embed.Embed;
import com.quiptmc.discord2.api.Wrapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.data.DataObject;

public class QuiptMessageChannel<T extends MessageChannel> extends Wrapper<T> {


    public QuiptMessageChannel(T original) {
        super(original);
    }

    public String mention(){
        return data().getAsMention();
    }

    public void delete(){
        data().delete().queue();
    }


    /**
     * Gets the channel's display name.
     * @return the channel name
     */
    public String name() {
        return data().getName();
    }

    /**
     * Sends a plain text message to the channel.
     * @param s the message content
     */
    public void send(String s) {
        data().sendMessage(s).queue();
    }

    /**
     * Gets the channel ID as a string.
     * @return the channel ID
     */
    public String id() {
        return data().getId();
    }

    /**
     * Sends one or more Quipt Embed objects to the channel.
     * @param embeds the embeds to send
     */
    public void send(Embed... embeds) {
        for (Embed embed : embeds) {
            EmbedBuilder builder = new EmbedBuilder(EmbedBuilder.fromData(DataObject.fromJson(embed.json().toString())));
            data().sendMessageEmbeds(builder.build()).queue();
        }
    }
}
