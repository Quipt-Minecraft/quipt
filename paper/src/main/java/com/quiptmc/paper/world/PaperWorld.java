package com.quiptmc.paper.world;

import com.quiptmc.minecraft.api.world.QuiptChunkGenerator;
import com.quiptmc.minecraft.api.world.QuiptWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class PaperWorld implements QuiptWorld<World> {

    private final Builder worldBuilder;
    private World world = null;

    public PaperWorld(QuiptWorld.Builder worldBuilder) {
        this.worldBuilder = worldBuilder;
    }

    @Override
    public World generate() {
        WorldCreator wc = new WorldCreator(worldBuilder.name());
        wc.environment(World.Environment.valueOf(worldBuilder.environment()));
        wc.type(WorldType.valueOf(worldBuilder.type()));
        wc.generator(worldBuilder.chunkGenerator());
        wc.generateStructures(worldBuilder.generateStructures());
        world = Bukkit.createWorld(wc);
        return world;
    }
}
