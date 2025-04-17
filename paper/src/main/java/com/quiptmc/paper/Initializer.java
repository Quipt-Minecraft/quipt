package com.quiptmc.paper;

import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.minecraft.CoreUtils;
import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.minecraft.api.MinecraftMaterial;
import com.quiptmc.minecraft.utils.loaders.ServerLoader;
import com.quiptmc.paper.commands.CommandManager;
import com.quiptmc.paper.data.PaperPlayer;
import com.quiptmc.paper.listeners.EventListener;
import com.quiptmc.paper.listeners.PlayerListener;
import com.quiptmc.paper.listeners.SessionListener;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public final class Initializer extends JavaPlugin {

    @Override
    public void onEnable() {
        PaperPlayer.init();
        MinecraftMaterial.init();
        Registry<MinecraftMaterial> materialRegistry = MinecraftMaterial.registry().orElseThrow();
        for (Material bukkitMat : Material.values()) {
            if (bukkitMat.isLegacy()) continue;
            materialRegistry.register(bukkitMat.name(), new MinecraftMaterial(bukkitMat.translationKey(), bukkitMat.name(), bukkitMat.getMaxStackSize(), bukkitMat.isBlock(), bukkitMat.isItem(), bukkitMat.isAir()));
        }



        CoreUtils.init(new QuiptPaperIntegration("Quipt", new ServerLoader<>(ServerLoader.Type.PAPER, this)));

        CommandManager.init(this);

        new PlayerListener(this);
        new EventListener(this);
        new SessionListener(this);

    }

    @Override
    public void onDisable() {
        ConfigManager.saveAll();
    }
}
