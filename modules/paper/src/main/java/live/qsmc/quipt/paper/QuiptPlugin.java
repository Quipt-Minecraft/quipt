package live.qsmc.quipt.paper;

import live.qsmc.quipt.core.data.Metadata;
import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.minecraft.MinecraftIntegration;
import live.qsmc.quipt.paper.api.players.PaperPlayer;
import live.qsmc.quipt.paper.api.players.PaperPlayers;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

import java.io.File;

public abstract class QuiptPlugin extends JavaPlugin {

    protected PaperIntegration integration = null;




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



    public static abstract class PaperIntegration extends MinecraftIntegration<JavaPlugin, NamespacedKey, PaperPlayers, Player, PaperPlayer> {


        public PaperIntegration(Metadata metadata, JavaPlugin instance) {
            super(metadata, instance, PaperPlayers::new);
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
