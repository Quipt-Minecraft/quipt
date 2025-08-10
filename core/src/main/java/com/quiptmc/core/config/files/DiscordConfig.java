/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.quiptmc.core.config.files;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.config.Config;
import com.quiptmc.core.config.ConfigTemplate;
import com.quiptmc.core.config.ConfigValue;
import com.quiptmc.core.data.JsonSerializable;

import java.io.File;

@ConfigTemplate(name = "discord")
public class DiscordConfig extends Config {

    @ConfigValue
    public boolean enable_bot = false;

    @ConfigValue
    public String bot_token = "<token_here>";

    @ConfigValue
    public Announcements announcements = new Announcements();

    @ConfigValue
    public Channels channels = new Channels();

    public DiscordConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
    }

    public static class Announcements implements JsonSerializable {

        public boolean join = false;

        public boolean leave = false;

        public boolean death = false;

        public boolean chat = false;

        public boolean server_start = false;
    }

    public static class Channels implements JsonSerializable {

        public String player_status = "";

        public String server_status = "";
    }
}
