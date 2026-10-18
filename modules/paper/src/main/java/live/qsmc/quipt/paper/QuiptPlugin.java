package live.qsmc.quipt.paper;

import live.qsmc.quipt.core.data.Metadata;
import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.minecraft.MinecraftIntegration;
import live.qsmc.quipt.paper.api.players.PaperPlayers;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.File;

public abstract class QuiptPlugin extends JavaPlugin {

    PaperIntegration integration = null;

    private PaperPlayers players = null;



    @Override
    public void onEnable() {
        listeners();
        commands();
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

    public abstract void listeners();

    public abstract void commands();

    public abstract void enable();

    public PaperPlayers players() {
        if (players == null) {
            integration().logger().log("Players", "Initializing players...");
            players = new PaperPlayers(integration());
        }
        return players;
    }

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
