package com.quiptmc.paper.api;

import com.quiptmc.minecraft.utils.MinecraftIntegration;
import com.quiptmc.core.heartbeat.HeartbeatUtils;
import com.quiptmc.minecraft.utils.loaders.ServerLoader;
import com.quiptmc.minecraft.utils.teleportation.LocationUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperIntegration extends MinecraftIntegration<ServerLoader<JavaPlugin>> {


    public PaperIntegration(String name, ServerLoader<JavaPlugin> loader) {
        super(name, loader);
    }

    public JavaPlugin plugin(){
        return (JavaPlugin) loader().instance();
    }

}
