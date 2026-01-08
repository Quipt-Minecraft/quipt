package com.quiptmc2.paper;

import com.quiptmc.core.data.Metadata;
import com.quiptmc2.minecraft.api.MinecraftIntegration;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

public abstract class QuiptPlugin extends JavaPlugin {

    PaperIntegration integration = null;


    @Override
    public void onEnable() {
        JSONObject data = new JSONObject();
        data.put("name", getPluginMeta().getName());
        data.put("version", getPluginMeta().getVersion());
        data.put("folder", getDataFolder());
        Metadata metadata = Metadata.of(data);
        integration = new PaperIntegration(metadata, this);
        enable();
    }

    public abstract void enable();

    public static class PaperIntegration extends MinecraftIntegration<JavaPlugin> {
        public PaperIntegration(Metadata metadata, JavaPlugin instance) {
            super(metadata, instance);
        }

        public JavaPlugin plugin() {
            return instance();
        }
    }
}
