package com.quiptmc2.paper.api.players;

import com.quiptmc2.core.data.registries.Registry;
import com.quiptmc2.minecraft.api.MinecraftIntegration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperPlayers {

    private final Registry<PaperPlayer> players;
    private final MinecraftIntegration<JavaPlugin> integration;

    public PaperPlayers(MinecraftIntegration<JavaPlugin> integration){
        players = integration.registries().register("players", ()->null);
        this.integration = integration;
    }

    public PaperPlayer of(Player player){
        return players.get(player.getUniqueId().toString()).orElse(register(player));
    }

    private PaperPlayer register(Player player){
        PaperPlayer playerData = new PaperPlayer(integration, player);
        players.register(player.getUniqueId().toString(), playerData);
        return playerData;
    }
}
