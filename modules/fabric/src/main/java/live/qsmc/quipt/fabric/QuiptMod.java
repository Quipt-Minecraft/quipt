package live.qsmc.quipt.fabric;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.data.Metadata;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.resources.Identifier;
import org.json.JSONObject;

import java.io.File;

public abstract class QuiptMod implements ModInitializer {


    FabricIntegration integration = null;

    protected void initialize(ModContainer container){
        ModMetadata fabricMetadata = container.getMetadata();
        integration = new FabricIntegration(metadata(fabricMetadata), container);
    }

    public void run(EntrypointContainer<QuiptMod> entrypoint) {
        ModMetadata metadata = entrypoint.getProvider().getMetadata();
        integration = new FabricIntegration(metadata(metadata), entrypoint.getProvider());
        Quipt.INSTANCE.enable(integration);
    }

    private Metadata metadata(ModMetadata fabricMetadata){
        JSONObject data = new JSONObject();
        data.put("name", fabricMetadata.getName());
        data.put("version", fabricMetadata.getVersion().getFriendlyString());
        data.put("id", fabricMetadata.getId());
        data.put("folder", new File("config/" + fabricMetadata.getId()));
        return Metadata.of(data);
    }

    public FabricIntegration integration() {
        return integration;
    }

    public static class FabricIntegration extends MinecraftIntegration<ModContainer, Identifier> {

        public FabricIntegration(Metadata metadata, ModContainer instance) {
            super(metadata, instance);
        }

        @Override
        public File addons() {
            return new File("mods");
        }

        public void enable() {
            logger().log("Initialization", "Initializing " + id() + ".");
        }

        public Identifier identifier(String id) {
            if(id.contains(":"))
                return Identifier.tryParse(id);
            return Identifier.fromNamespaceAndPath(id(), id);
        }
    }
}
