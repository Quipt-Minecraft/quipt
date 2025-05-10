package com.quiptmc.fabric.api;

import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.core.config.ConfigTemplate;
import com.quiptmc.core.config.files.*;
import com.quiptmc.core.config.files.discord.AnnouncementsNestedConfig;
import com.quiptmc.core.config.files.discord.ChannelsNestedConfig;
import com.quiptmc.core.config.files.resource.AuthNestedConfig;
import com.quiptmc.core.config.files.resource.HashesNestedConfig;
import com.quiptmc.core.config.files.web.HealthReportNestedConfig;
import com.quiptmc.core.discord.embed.Embed;
import com.quiptmc.minecraft.api.events.EventHandler;
import com.quiptmc.core.server.QuiptServer;
import com.quiptmc.core.utils.TaskScheduler;
import com.quiptmc.discord.Bot;
import com.quiptmc.discord.api.guild.QuiptGuild;
import com.quiptmc.discord.api.guild.channel.QuiptTextChannel;
import com.quiptmc.discord.api.plugins.BotPlugin;
import com.quiptmc.discord.api.plugins.BotPluginLoader;
import com.quiptmc.minecraft.listeners.QuiptPlayerListener;
import com.quiptmc.minecraft.utils.MinecraftIntegration;
import com.quiptmc.minecraft.utils.chat.MessageUtils;
import com.quiptmc.minecraft.utils.chat.placeholder.PlaceholderUtils;
import com.quiptmc.minecraft.utils.heartbeat.Flutter;
import com.quiptmc.minecraft.utils.heartbeat.HeartbeatUtils;
import com.quiptmc.minecraft.utils.loaders.ServerLoader;
import com.quiptmc.minecraft.utils.sessions.SessionManager;
import com.quiptmc.minecraft.web.CallbackHandler;
import com.quiptmc.minecraft.web.HealthReportHandler;
import com.quiptmc.minecraft.web.ResourcePackHandler;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FabricIntegration extends MinecraftIntegration<ServerLoader<ModMetadata>> {






    public FabricIntegration(String name, ServerLoader<ModMetadata> loader) {
        super(name, loader);
    }



    @Override
    public void enable() {
        super.enable();
        HeartbeatUtils.init(this);


    }

}
