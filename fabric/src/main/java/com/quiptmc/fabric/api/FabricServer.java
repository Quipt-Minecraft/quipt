package com.quiptmc.fabric.api;

import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.minecraft.api.QuiptMinecraftServer;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;

public class FabricServer implements QuiptMinecraftServer {

    public static final Registry<FabricServer> registry = Registries.register("servers", ()->null);
    private static final Map<String, FabricServer> playerMap = new HashMap<>();

    private final MinecraftServer server;

    private FabricServer(MinecraftServer server) {
        this.server = server;
        registry.register(server.getServerIp(), this);
    }

    public static FabricServer of(MinecraftServer server) {
        return playerMap.computeIfAbsent(server.getServerIp(), ip -> new FabricServer(server));
    }

    public MinecraftServer getMinecraftServer() {
        return server;
    }

}
