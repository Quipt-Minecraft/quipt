package com.quiptmc.minecraft.api.world;

import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;

public class WorldManager {

    private static final Registry<QuiptWorld> WORLD_REGISTRY = Registries.register("worlds", QuiptWorld.class);

    public static void register(String key, QuiptWorld world) {
        WORLD_REGISTRY.register(key, world);
    }

    public static QuiptWorld get(String key) {
        return WORLD_REGISTRY.get(key).orElseThrow(() -> new IllegalArgumentException("World not found: " + key));
    }


}
