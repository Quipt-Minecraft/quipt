package com.quiptmc.minecraft.utils.sessions;

import com.quiptmc.core.annotations.Nullable;
import com.quiptmc.minecraft.api.MinecraftPlayer;

import java.util.UUID;

public class Session {

    private final Type type;
    private final UUID id;
    @Nullable
    private final MinecraftPlayer player;

    public Session(UUID id) {
        this.id = id;
        this.type = Type.GLOBAL;
        this.player = null;
    }

    public Session(UUID id, MinecraftPlayer player) {
        this.id = id;
        this.type = Type.PLAYER;
        this.player = player;
    }

    public Type type() {
        return type;
    }

    public UUID id() {
        return id;
    }

    @Nullable
    public MinecraftPlayer player() {
        return player;
    }

    public enum Type {
        PLAYER,
        GLOBAL
    }

    public record Data(Type type, @Nullable MinecraftPlayer player) {

    }
}
