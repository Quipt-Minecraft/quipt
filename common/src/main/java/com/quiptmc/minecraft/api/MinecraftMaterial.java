package com.quiptmc.minecraft.api;

import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;

import java.util.Optional;

public record MinecraftMaterial(String id, String name, int maxStackSize, boolean block, boolean item, boolean air) {

    private static Registry<MinecraftMaterial> registry = null;


    public static void init(){
        Registries.register("materials", MinecraftMaterial.class);
    }

    public static Optional<Registry<MinecraftMaterial>> registry(){
        return Optional.ofNullable(registry);
    }

    public static MinecraftMaterial[] values() {
        return null;
    }

    public String name() {
        return "";
    }
}
