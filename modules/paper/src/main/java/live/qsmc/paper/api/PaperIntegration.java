package live.qsmc.paper.api;

import live.qsmc.minecraft.utils.MinecraftIntegration;
import live.qsmc.minecraft.utils.loaders.ServerLoader;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperIntegration extends MinecraftIntegration<ServerLoader<JavaPlugin>> {


    public PaperIntegration(String name, ServerLoader<JavaPlugin> loader) {
        super(name, loader);
    }

    public JavaPlugin plugin() {
        return (JavaPlugin) loader().instance();
    }

}
