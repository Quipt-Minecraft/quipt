package com.quiptmc.paper2;

import com.quiptmc.core2.data.annotations.Nullable;
import com.quiptmc.minecraft2.config.files.ResourceConfig;
import com.quiptmc.minecraft2.utils.chat.MessageUtils;
import com.quiptmc.paper2.api.players.PaperPlayers;
import com.quiptmc.paper2.commands.CommandExecutor;
import com.quiptmc.paper2.commands.executors.UpdateCommand;
import com.quiptmc.paper2.commands.executors.WebhookCommand;
import net.kyori.adventure.text.Component;

import static net.kyori.adventure.text.Component.text;

public class QuiptPaper extends QuiptPlugin {

    private PaperPlayers players = null;

    private static QuiptPaper instance = null;

    @Nullable
    public static QuiptPaper instance() {
        return instance;
    }


    @Override
    public void enable() {
        instance = this;
        MessageUtils.register("cmd.update.usage", text("Usage: /update <group> <plugin> <build> <artifact>"));
        MessageUtils.register("cmd.webhook.usage", text("Usage: /webhook <add|remove> <webhook> <url>"));

        if(integration().configs().config(ResourceConfig.class) == null)
            integration().configs().register(ResourceConfig.class);
        ResourceConfig config = integration().configs().config(ResourceConfig.class);
        if(config.enabled){
            integration().packHandler().start();
        }
        new CommandExecutor.Builder(new WebhookCommand(this)).setDescription("Alter webhooks").register();
        new CommandExecutor.Builder(new UpdateCommand(this)).setDescription("Update plugins from https://ci.qsmc.live").register();
        integration().logger().log("Paper", "QuiptPaper for Paper enabled!");
    }

    public PaperPlayers players() {
        if(players == null) {
            integration().logger().log("Players", "Initializing players...");
            players = new PaperPlayers(integration());
        }
        return players;
    }

}
