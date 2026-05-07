package live.qsmc.quipt.paper;

import live.qsmc.quipt.core.data.Metadata;
import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;
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

    public static abstract class PaperIntegration extends MinecraftIntegration<JavaPlugin> {


        public PaperIntegration(Metadata metadata, JavaPlugin instance) {
            super(metadata, instance);
        }

        public JavaPlugin plugin() {
            return instance();
        }

    }


}
