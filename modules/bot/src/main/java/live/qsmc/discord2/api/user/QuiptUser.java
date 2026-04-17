package live.qsmc.discord2.api.user;

import live.qsmc.discord2.api.Wrapper;
import net.dv8tion.jda.api.entities.User;

import java.time.OffsetDateTime;

public class QuiptUser extends Wrapper<User> {

    public QuiptUser(net.dv8tion.jda.api.entities.User original) {
        super(original);
    }

    public String name(){
        return data().getName();
    }

    public String discriminator(){
        return data().getDiscriminator();
    }

    public String mention(){
        return data().getAsMention();
    }

    public OffsetDateTime created(){
        return data().getTimeCreated();
    }

    public String id(){
        return data().getId();
    }

    public String tag(){
        return data().getAsTag();
    }

    public boolean bot(){
        return data().isBot();
    }
}
