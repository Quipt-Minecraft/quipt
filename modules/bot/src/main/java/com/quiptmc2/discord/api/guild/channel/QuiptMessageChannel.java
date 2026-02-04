package com.quiptmc2.discord.api.guild.channel;

import com.quiptmc2.discord.api.Wrapper;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public class QuiptMessageChannel<T extends MessageChannel> extends Wrapper<T> {


    public QuiptMessageChannel(T original) {
        super(original);
    }
}
