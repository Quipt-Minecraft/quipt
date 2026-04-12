/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.quiptmc.discord2.api;

import com.quiptmc.discord2.api.guild.QuiptGuild;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.util.*;

public class QDA extends Wrapper<JDA> {

    private final GuildManager guilds = new GuildManager();

    public QDA(JDA jda){
        super(jda);
    }

    public GuildManager guilds(){
        return guilds;
    }

    public static class GuildManager {
        public Map<Guild, QuiptGuild> guilds = new HashMap<>();

        public QuiptGuild get(Guild guild){
            return guilds.computeIfAbsent(guild, QuiptGuild::new);
        }

        public Collection<QuiptGuild> all(){
            return guilds.values();
        }
    }



}
