/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package live.qsmc.discord2.api.guild;

import live.qsmc.discord2.api.Wrapper;
import live.qsmc.discord2.api.guild.channel.QuiptTextChannel;
import live.qsmc.discord2.api.user.QuiptUser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuiptGuild extends Wrapper<Guild> {

    private final Map<User, QuiptUser> userCache = new HashMap<>();

    public QuiptGuild(Guild guild) {
        super(guild);
    }

    public List<QuiptTextChannel> getTextChannels() {
        List<QuiptTextChannel> channels = new ArrayList<>();
        data().getTextChannels().forEach(channel -> channels.add(new QuiptTextChannel(channel)));
        return channels;
    }

    public QuiptUser user(User author) {
        return userCache.computeIfAbsent(author, QuiptUser::new);
    }
}
