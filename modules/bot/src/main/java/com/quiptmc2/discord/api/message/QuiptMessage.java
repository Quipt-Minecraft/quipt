package com.quiptmc2.discord.api.message;

import com.quiptmc2.discord.api.Wrapper;
import com.quiptmc2.discord.api.guild.QuiptGuild;
import com.quiptmc2.discord.api.user.QuiptUser;
import net.dv8tion.jda.api.entities.Message;

import java.time.OffsetDateTime;

public class QuiptMessage extends Wrapper<Message> {


    public QuiptMessage(Message message) {
        super(message);
    }

    @Override
    public String toString() {
        return content();
    }

    public String content() {
        return original().getContentRaw();
    }

    public OffsetDateTime created() {
        return original().getTimeCreated();
    }

    public OffsetDateTime edited() {
        return original().getTimeEdited();
    }

    public void delete() {
        original().delete().queue();
    }

    public QuiptUser author(QuiptGuild guild) {
        return guild.user(original().getAuthor());
    }





}
