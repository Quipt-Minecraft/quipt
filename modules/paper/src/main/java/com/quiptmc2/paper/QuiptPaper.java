package com.quiptmc2.paper;

import com.quiptmc2.core.data.annotations.Nullable;
import com.quiptmc2.minecraft.config.files.ResourceConfig;
import com.quiptmc2.paper.api.players.PaperPlayers;

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
        if(integration.configs().config(ResourceConfig.class) == null)
            integration.configs().register(ResourceConfig.class);
        ResourceConfig config = integration.configs().config(ResourceConfig.class);
        if(config.enabled){
            integration.packHandler().start();
        }
        integration.logger().log("Quipt", "Quipt Plugin for Paper enabled!");
//        PaperPlayers.of(null);
    }

    public PaperPlayers players() {
        if(players == null) {
            integration().logger().log("Players", "Initializing players...");
            players = new PaperPlayers(integration());
        }
        return players;
    }

}
