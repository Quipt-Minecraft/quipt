package com.quiptmc2.discord.api.user;

import com.quiptmc2.discord.api.Wrapper;
import net.dv8tion.jda.api.entities.User;

public class QuiptUser extends Wrapper<User> {

    public QuiptUser(net.dv8tion.jda.api.entities.User original) {
        super(original);
    }

    public String name(){
        return original().getName();
    }

    public String discriminator(){
        return original().getDiscriminator();
    }

    public String tag(){
        return original().getAsTag();
    }

    public boolean bot(){
        return original().isBot();
    }
}
