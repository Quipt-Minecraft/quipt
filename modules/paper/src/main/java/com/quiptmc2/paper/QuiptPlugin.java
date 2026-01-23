package com.quiptmc2.paper;

import com.quiptmc.core.data.Metadata;
import com.quiptmc.minecraft.api.MinecraftEntityType;
import com.quiptmc.minecraft.api.MinecraftMaterial;
import com.quiptmc.minecraft.api.statistics.MinecraftStat;
import com.quiptmc2.core.Quipt;
import com.quiptmc2.core.QuiptIntegration;
import com.quiptmc2.minecraft.api.MinecraftIntegration;
import com.quiptmc2.minecraft.api.MinecraftPlayer;
import com.quiptmc2.paper.api.PaperPlayers;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

public abstract class QuiptPlugin extends JavaPlugin {

    PaperIntegration integration = null;


    @Override
    public void onEnable() {
        JSONObject data = new JSONObject();
        data.put("name", getPluginMeta().getName());
        data.put("version", getPluginMeta().getVersion());
        data.put("folder", getDataFolder());
        Metadata metadata = Metadata.of(data);
        integration = new PaperIntegration(metadata, this) {
            @Override
            public void enable() {
                QuiptPlugin.this.enable();
            }
        };
        Quipt.INSTANCE.enable(integration);
    }

    public PaperIntegration integration() {
        return integration;
    }

    public abstract void enable();

    public static abstract class PaperIntegration extends MinecraftIntegration<JavaPlugin> {

        private PaperPlayers players;

        public PaperIntegration(Metadata metadata, JavaPlugin instance) {
            super(metadata, instance);
            players = new PaperPlayers(this);
        }

        public JavaPlugin plugin() {
            return instance();
        }

        public PaperPlayers players() {
            if(players == null) {
                logger().log("Players", "Initializing players...");
                players = new PaperPlayers(this);
            }
            return players;
        }

    }

    public static class PaperPlayer extends MinecraftPlayer {

        private final org.bukkit.entity.Player player;
        public PaperPlayer(QuiptIntegration integration, org.bukkit.entity.Player player) {
            super(integration);
            this.player = player;
        }

        @Override
        public int getStatistic(MinecraftStat stat) {
            return 0;
        }

        @Override
        public int getStatistic(MinecraftStat stat, MinecraftMaterial material) {
            return 0;
        }

        @Override
        public int getStatistic(MinecraftStat stat, MinecraftEntityType entity) {
            return 0;
        }

        @Override
        public void teleport(MinecraftPlayer target) {

        }
    }
}
