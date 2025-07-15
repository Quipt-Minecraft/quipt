package com.quiptmc.paper.data;

import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.minecraft.api.MinecraftEntityType;
import com.quiptmc.minecraft.api.MinecraftMaterial;
import com.quiptmc.minecraft.api.MinecraftPlayer;
import com.quiptmc.minecraft.api.statistics.MinecraftStat;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PaperPlayer implements MinecraftPlayer {

    private static Registry<PaperPlayer> registry = Registries.register("players", ()->null);

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
    public String getName() {
        return player.getName();
    }

    @Override
    public int getStatistic(MinecraftStat stat) {
        return player.getStatistic(Statistic.valueOf(stat.name()));
    }

    @Override
    public int getStatistic(MinecraftStat stat, MinecraftMaterial material) {
        return player.getStatistic(Statistic.valueOf(stat.name()), Material.valueOf(material.name()));
    }

    @Override
    public int getStatistic(MinecraftStat stat, MinecraftEntityType entity) {
        return player.getStatistic(Statistic.valueOf(stat.name()), EntityType.valueOf(entity.name()));
    }

    @Override
    public void teleport(MinecraftPlayer target) {
        System.out.println("Teleporting " + player.getName() + " to " + target.getName());
        if (!(target instanceof PaperPlayer)) throw new IllegalArgumentException("Target must be a PaperPlayer");
        player.teleport(((PaperPlayer) target).player);
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        player.sendMessage(message);
    }

    @Override
    public void sendMessage(@NotNull Component message, ChatType.@NotNull Bound boundChatType) {
        player.sendMessage(message, boundChatType);
    }

    @Override
    public void sendMessage(@NotNull SignedMessage signedMessage, ChatType.@NotNull Bound boundChatType) {
        player.sendMessage(signedMessage, boundChatType);
    }

    @Override
    public void sendMessage(@NotNull ComponentLike message) {
        player.sendMessage(message);
    }

    @Override
    public void sendMessage(@NotNull ComponentLike message, ChatType.@NotNull Bound boundChatType) {
        player.sendMessage(message, boundChatType);
    }
}