package live.qsmc.quipt.paper;

import live.qsmc.quipt.core.data.Metadata;
import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.File;

public abstract class QuiptPlugin extends JavaPlugin {

    PaperIntegration integration = null;


    @Override
    public void onEnable() {
        JSONObject data = new JSONObject();
        data.put("name", getPluginMeta().getName());
        data.put("version", getPluginMeta().getVersion());
        data.put("id", getPluginMeta().getName().toLowerCase().replace(" ", "_"));
        data.put("folder", getDataFolder());
        Metadata metadata = Metadata.of(data);
        integration = new PaperIntegration(metadata, this) {
            @Override
            public void enable() {
                QuiptPlugin.this.enable();
            }
        };
        Quipt.INSTANCE.enable(integration);
    }

    public PaperIntegration integration() {
        return integration;
    }

    public abstract void enable();

    public static abstract class PaperIntegration extends MinecraftIntegration<JavaPlugin, NamespacedKey> {


        public PaperIntegration(Metadata metadata, JavaPlugin instance) {
            super(metadata, instance);
        }

        public JavaPlugin plugin() {
            return instance();
        }

        public File addons() {
            return new File("plugins");
        }

        public NamespacedKey identifier(String name) {
            if(name.contains(":"))
                return NamespacedKey.fromString(name);
            return new NamespacedKey(plugin(), name);
        }

    }


}
