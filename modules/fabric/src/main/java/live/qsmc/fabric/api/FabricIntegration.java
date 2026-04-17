package live.qsmc.fabric.api;

import live.qsmc.minecraft.utils.MinecraftIntegration;
import live.qsmc.minecraft.utils.loaders.ServerLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;

public class FabricIntegration extends MinecraftIntegration<ServerLoader<ModMetadata>> {

    public FabricIntegration(String name, ServerLoader<ModMetadata> loader) {
        super(name, loader);
    }

}
