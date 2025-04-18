package com.quiptmc.paper.data;

import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.minecraft.api.MinecraftEntityType;
import com.quiptmc.minecraft.api.MinecraftMaterial;
import com.quiptmc.minecraft.api.MinecraftPlayer;
import com.quiptmc.minecraft.api.MinecraftStatistic;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PaperPlayer implements MinecraftPlayer {

    private static Registry<PaperPlayer> registry;

    public static void init() {
        Registries.KEYS.register("paper_players").ifPresent(registryKey -> Registries.REGISTRAR.add(registryKey, new Registry<>(PaperPlayer.class)));
    }

    public static PaperPlayer of(Player player) {
        return registry.get(player.getUniqueId().toString()).orElseGet(() -> {
            PaperPlayer paperPlayer = new PaperPlayer(player);
            registry.register(player.getUniqueId().toString(), paperPlayer);
            return paperPlayer;
        });
    }

    private final Player player;

    public PaperPlayer(Player player) {
        this.player = player;
    }

    @Override
    public int getStatistic(MinecraftStatistic stat) {
        return player.getStatistic(Statistic.valueOf(stat.name()));
    }

    @Override
    public int getStatistic(MinecraftStatistic stat, MinecraftMaterial material) {
        return player.getStatistic(Statistic.valueOf(stat.name()), Material.valueOf(material.name()));
    }

    @Override
    public int getStatistic(MinecraftStatistic stat, MinecraftEntityType entity) {
        return player.getStatistic(Statistic.valueOf(stat.name()), EntityType.valueOf(entity.name()));
    }

    @Override
    public void teleport(MinecraftPlayer target) {
        if (!(target instanceof PaperPlayer)) throw new IllegalArgumentException("Target must be a PaperPlayer");
        player.teleport(((PaperPlayer) target).player);
    }
}