package live.qsmc.fabric2;

import live.qsmc.core2.Quipt;
import live.qsmc.core2.data.Metadata;
import live.qsmc.minecraft2.api.MinecraftIntegration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
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
        data.put("folder", new File("config/" + fabricMetadata.getId()));
        return Metadata.of(data);
    }

    public FabricIntegration integration() {
        return integration;
    }

    public static class FabricIntegration extends MinecraftIntegration<ModContainer> {

        private String id;

        public FabricIntegration(Metadata metadata, ModContainer instance) {
            super(metadata, instance);
            id = instance.getMetadata().getId();
        }

        public String id() {
            return id;
        }

        public void enable() {
            logger().log("Initialization", "Initializing " + id() + ".");
        }
    }
}
