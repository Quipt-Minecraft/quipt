package com.quiptmc.fabric.utils;

import com.quiptmc.core.entity.QuiptPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

public class FabricConversionUtils {

    public static QuiptPlayer player(PlayerEntity player) {
        if(player == null) return null;
        return new QuiptPlayer(player.getGameProfile().getName(), player.getUuid());
    }

    public static PlayerEntity player(MinecraftServer server, QuiptPlayer player) {
        return server.getPlayerManager().getPlayer(player.uuid());
    }
}
