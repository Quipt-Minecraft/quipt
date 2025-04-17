package com.quiptmc.paper;

import com.quiptmc.minecraft.utils.MinecraftIntegration;
import com.quiptmc.minecraft.utils.loaders.ServerLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class QuiptPaperIntegration<T extends JavaPlugin> extends MinecraftIntegration<T> {


    public QuiptPaperIntegration(String name, @Nullable ServerLoader<T> loader) {
        super(name, loader);
    }

    @Override
    public void enable() {
        super.enable();


//        minecraftServer = new MinecraftServer(apiManager.getIP(), apiConfig.secret, apiConfig.endpoint + "/server_status/");
    }


}