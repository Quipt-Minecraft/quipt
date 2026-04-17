package live.qsmc.discord2.api.message;

import live.qsmc.discord2.api.Wrapper;
import live.qsmc.discord2.api.guild.QuiptGuild;
import live.qsmc.discord2.api.user.QuiptUser;
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
        return data().getContentRaw();
    }

    public OffsetDateTime created() {
        return data().getTimeCreated();
    }

    public OffsetDateTime edited() {
        return data().getTimeEdited();
    }

    public void delete() {
        data().delete().queue();
    }

    public QuiptUser author(QuiptGuild guild) {
        return guild.user(data().getAuthor());
    }





}
