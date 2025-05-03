package com.quiptmc.fabric.api;

import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.minecraft.api.MinecraftEntityType;
import com.quiptmc.minecraft.api.MinecraftMaterial;
import com.quiptmc.minecraft.api.MinecraftPlayer;
import com.quiptmc.minecraft.api.statistics.MinecraftStat;
import net.kyori.adventure.text.Component;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class FabricPlayer implements MinecraftPlayer {

    public static final Registry<FabricPlayer> registry = Registries.register("players", FabricPlayer.class);
    private static final Map<UUID, FabricPlayer> playerMap = new HashMap<>();

    private final ServerPlayerEntity player;

    private FabricPlayer(ServerPlayerEntity player) {
        this.player = player;
        registry.register(player.getUuidAsString(), this);
    }

    public static FabricPlayer of(ServerPlayerEntity player) {
        return playerMap.computeIfAbsent(player.getUuid(), uuid -> new FabricPlayer(player));
    }

    public ServerPlayerEntity getMinecraftPlayer() {
        return player;
    }

    @Override
    public int getStatistic(MinecraftStat stat) {
        return player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Identifier.of(stat.name())));
    }

    @Override
    public int getStatistic(MinecraftStat stat, MinecraftMaterial material) {
        return getStatistic(stat);
    }

    @Override
    public int getStatistic(MinecraftStat stat, MinecraftEntityType entity) {
        return getStatistic(stat);
    }

    @Override
    public void teleport(MinecraftPlayer target) {
        if (!(target instanceof FabricPlayer)) throw new IllegalArgumentException("Target must be a FabricPlayer");
        FabricPlayer serverTarget = (FabricPlayer) target;
        ServerPlayerEntity targetPlayer = serverTarget.getMinecraftPlayer();
        ServerWorld serverWorld = targetPlayer.getServerWorld();
        double x = targetPlayer.getX();
        double y = targetPlayer.getY();
        double z = targetPlayer.getZ();
        Set<PositionFlag> flags = Set.of(PositionFlag.X, PositionFlag.Y, PositionFlag.Z);
        float yaw = targetPlayer.getYaw();
        float pitch = targetPlayer.getPitch();
        player.teleport(serverWorld, x, y, z, flags, yaw, pitch, false);
//        player.teleport(targetPlayer.getServerWorld(), targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ(), Set.of(PositionFlag.ROTATE_DELTA), targetPlayer.getYaw(), targetPlayer.getPitch());
    }

    @Override
    public Component name() {
        return Component.text(player.getGameProfile().getName());
    }

    @Override
    public UUID uuid() {
        return player.getUuid();
    }
}