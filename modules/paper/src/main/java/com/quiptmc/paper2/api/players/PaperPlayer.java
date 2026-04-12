package com.quiptmc.paper2.api.players;

import com.quiptmc.minecraft.api.MinecraftEntityType;
import com.quiptmc.minecraft.api.MinecraftMaterial;
import com.quiptmc.minecraft.api.statistics.MinecraftStat;
import com.quiptmc.core2.QuiptIntegration;
import com.quiptmc.minecraft2.api.MinecraftPlayer;
//import com.quiptmc2.paper.api.settings.Settings;

import java.util.UUID;

public class PaperPlayer extends MinecraftPlayer {

    public UUID uuid;
    public long last_seen;
//    public Settings settings;

    private final org.bukkit.entity.Player player;

    public PaperPlayer(QuiptIntegration integration, org.bukkit.entity.Player player) {
        super(integration, player.getUniqueId());
        this.player = player;
        this.uuid = player.getUniqueId();
        this.last_seen = System.currentTimeMillis();
//        this.settings = new Settings(integration);
    }

//    public Settings settings() {
//        return settings;
//    }

    @Override
    public UUID uuid() {
        return uuid;
    }

    public long lastSeen() {
        return last_seen;
    }

    public org.bukkit.entity.Player player() {
        return player;
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
        player().teleport(((PaperPlayer) target).player());
    }





}