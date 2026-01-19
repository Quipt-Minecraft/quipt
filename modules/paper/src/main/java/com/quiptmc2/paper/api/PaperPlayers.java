package com.quiptmc2.paper.api;

import com.quiptmc.core.data.registries.Registries;
import com.quiptmc2.core.data.registries.Registry;
import com.quiptmc2.minecraft.api.MinecraftIntegration;
import com.quiptmc2.paper.QuiptPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperPlayers {

    private final Registry<QuiptPlugin.PaperPlayer> players;
    private final MinecraftIntegration<JavaPlugin> integration;

    public PaperPlayers(MinecraftIntegration<JavaPlugin> integration){
        players = integration.registries().register("players", ()->null);
        this.integration = integration;
    }

    public QuiptPlugin.PaperPlayer of(Player player){
        return players.get(player.getUniqueId().toString()).orElse(register(player));
    }

    private QuiptPlugin.PaperPlayer register(Player player){
        QuiptPlugin.PaperPlayer playerData = new QuiptPlugin.PaperPlayer(integration, player);
        players.register(player.getUniqueId().toString(), playerData);
        return playerData;
    }
}
